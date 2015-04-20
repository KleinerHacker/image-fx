package org.pcsoft.tools.image_fx.ui.windows.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.dialog.Dialogs;
import org.pcsoft.tools.image_fx.common.listeners.ProgressListener;
import org.pcsoft.tools.image_fx.common.threads.DaemonThreadFactory;
import org.pcsoft.tools.image_fx.core.configuration.ConfigurationManager;
import org.pcsoft.tools.image_fx.scripting.threading.JavaScriptImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayer;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.ui.building.MainWindowUIBuilder;
import org.pcsoft.tools.image_fx.ui.components.controller.ImagePreviewController;
import org.pcsoft.tools.image_fx.ui.dialogs.ScriptManagerDialogFactory;
import org.pcsoft.tools.image_fx.ui.managing.XmlScriptPreviewManager;
import org.pcsoft.tools.image_fx.ui.utils.HistoryStack;
import org.pcsoft.tools.image_fx.ui.utils.ImageUtils;
import org.pcsoft.tools.image_fx.ui.utils.actions.UpdateImageHistoryAction;
import org.pcsoft.tools.image_fx.ui.windows.MainWindowFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

/**
 * Created by Christoph on 17.06.2014.
 */
public class MainWindowController extends AbstractController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindowController.class);

    static interface LoadImageListener {
        Image loadImage();
    }

    @FXML
    private Accordion accRenderers;
    @FXML
    private Accordion accEffects;
    @FXML
    private Accordion accLayers;
    @FXML
    private CheckBox ckbShowMask;
    @FXML
    private Slider sldMaskOpacity;
    @FXML
    private Slider sldOpacity;
    @FXML
    private Label lblMaskOpacity;
    @FXML
    private Label lblOpacity;
    @FXML
    private TitledPane tpMask;
    @FXML
    private TitledPane tpOpacity;

    @FXML
    protected ImageView imgPicture;
    @FXML
    protected ImageView imgMask;

    //Sub-Controller
    @FXML
    private MainWindowMenuController menuController;
    @FXML
    private MainWindowStatusBarController statusBarController;
    @FXML
    private MainWindowWorkerController workerController;

    protected final ObjectProperty<File> currentFile = new SimpleObjectProperty<>(null);
    protected final HistoryStack historyStack = new HistoryStack();

    public MainWindowController(Stage parent) {
        super(parent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LOGGER.info("Initialize UI: Main Window Controller (MAIN)");

        parent.titleProperty().bind(
                Bindings.when(currentFile.isNull())
                        .then(String.format(MainWindowFactory.TITLE_FORMAT, "Unnamed"))
                        .otherwise(currentFile.asString(MainWindowFactory.TITLE_FORMAT))
        );
        sldMaskOpacity.disableProperty().bind(ckbShowMask.selectedProperty().not());
        imgMask.opacityProperty().bind(sldMaskOpacity.valueProperty());
        imgMask.visibleProperty().bind(ckbShowMask.selectedProperty());
        imgMask.fitWidthProperty().bind(imgPicture.fitWidthProperty());
        imgMask.fitHeightProperty().bind(imgPicture.fitHeightProperty());
        tpMask.disableProperty().bind(imgPicture.imageProperty().isNull().or(imgMask.imageProperty().isNull()));
        tpOpacity.disableProperty().bind(imgPicture.imageProperty().isNull());
        lblMaskOpacity.textProperty().bind(sldMaskOpacity.valueProperty().multiply(100).asString("%.0f").concat("%"));
        lblOpacity.textProperty().bind(sldOpacity.valueProperty().multiply(100).asString("%.0f").concat("%"));
        imgPicture.imageProperty().addListener(new ChangeListener<Image>() {
            @Override
            public void changed(ObservableValue<? extends Image> observableValue, Image oldImage, Image newImage) {
                imgPicture.setFitWidth(newImage.getWidth());
                imgPicture.setFitHeight(newImage.getHeight());

                rebuildPreviews(newImage);
            }
        });

        Platform.runLater(() -> MainWindowUIBuilder.buildInitialPreviews(
                accEffects, accRenderers, accLayers, statusBarController.getWorkerProgress(), statusBarController.getWorkerLabel(),
                new ImagePreviewController.OnImageApplyListener() {
                    @Override
                    public void onUseEffect(ImageScriptElement scriptInfo, ImageScriptVariant variantInfo) {
                        if (imgPicture.getImage() == null)
                            return;

                        applyScriptOnImage(scriptInfo, variantInfo);
                    }

                    @Override
                    public void onPrepareEffect(ImageScriptElement scriptInfo) {
                        final ScriptManagerDialogFactory.Result result = ScriptManagerDialogFactory.show(
                                imgPicture.getImage() == null ? ImageUtils.createExamplePreviewThumbnail() : imgPicture.getImage(),
                                scriptInfo, imgPicture.getImage() != null
                        );

                        if (result != null) {
                            applyScriptOnImage(result.getScriptElement(), result.getScriptVariant());
                        }
                    }

                    @Override
                    public void onUseEffect(ImageScriptLayer scriptLayer) {
                        if (imgPicture.getImage() == null)
                            return;

                        applyScriptOnImage(scriptLayer);
                    }

                    @Override
                    public void onPrepareEffect(ImageScriptLayer scriptLayer) {
                        //TODO
                    }
                }
        ));

        menuController.update(imgPicture, imgMask, ckbShowMask, sldOpacity, historyStack, currentFile,
                new MainWindowMenuController.MenuListener() {
                    @Override
                    public void applyScriptOnImage(ImageScriptElement scriptInfo, ImageScriptVariant variantInfo) {
                        MainWindowController.this.applyScriptOnImage(scriptInfo, variantInfo);
                    }

                    @Override
                    public void applyScriptOnImage(ImageScriptLayer scriptLayer) {
                        MainWindowController.this.applyScriptOnImage(scriptLayer);
                    }

                    @Override
                    public void saveImage() {
                        MainWindowController.this.saveImage();
                    }

                    @Override
                    public void loadImage(String workText, File newCurrentFile, LoadImageListener listener) {
                        MainWindowController.this.loadImage(workText, newCurrentFile, listener);
                    }
                }
        );
    }

    private void applyScriptOnImage(ImageScriptElement scriptInfo, ImageScriptVariant variantInfo) {
        LOGGER.debug("Apply script on image (single)...");

//        disableUIForPreviewUpdate();
        workerController.showWorker();

        Executors.newCachedThreadPool(new DaemonThreadFactory("Image builder")).submit(() -> {
            //Synchrony call
            final WritableImage writableImage = JavaScriptImageWriterTaskRunner.runJavaScriptTask(
                    imgPicture.getImage(), imgMask.getImage(), sldOpacity.getValue(), scriptInfo, variantInfo, new ProgressListener() {
                        @Override
                        public void onStartProgress() {
                            Platform.runLater(() -> {
                                statusBarController.showWorker("Apply '" + scriptInfo.getName() + "' script on image...");
                            });
                        }

                        @Override
                        public void onProgressUpdate(double progress) {
                            Platform.runLater(() -> workerController.updateWorkerProgress(progress));
                        }

                        @Override
                        public void onFinishProgress() {

                        }
                    }
            );

            Platform.runLater(() -> {
                workerController.hideWorker();

                historyStack.addToStack(
                        new UpdateImageHistoryAction(imgPicture, "Apply '" + scriptInfo.getName() + "' script on image", writableImage, imgPicture.getImage())
                );
                imgPicture.setImage(writableImage);
                imgPicture.setFitWidth(writableImage.getWidth());
                imgPicture.setFitHeight(writableImage.getHeight());
            });
        });
    }

    private void applyScriptOnImage(ImageScriptLayer scriptLayer) {
        LOGGER.debug("Apply script on image (multi)...");

//        disableUIForPreviewUpdate();
        workerController.showWorker();

        Executors.newCachedThreadPool(new DaemonThreadFactory("Image builder")).submit(() -> {
            //Synchrony call
            Image image = JavaScriptImageWriterTaskRunner.runJavaScriptTask(imgPicture.getImage(), imgMask.getImage(), sldOpacity.getValue(), scriptLayer.getScriptHolderList(), new ProgressListener() {
                @Override
                public void onStartProgress() {
                    Platform.runLater(() -> {
                        statusBarController.showWorker("Apply '" + scriptLayer.getName() + "' script layer on image...");
                    });
                }

                @Override
                public void onProgressUpdate(double progress) {
                    Platform.runLater(() -> workerController.updateWorkerProgress(progress));
                }

                @Override
                public void onFinishProgress() {

                }
            });

            final Image resultImage = image;
            Platform.runLater(() -> {
                workerController.hideWorker();

                historyStack.addToStack(
                        new UpdateImageHistoryAction(imgPicture, "Apply '" + scriptLayer.getName() + "' script layer on image", resultImage, imgPicture.getImage())
                );
                imgPicture.setImage(resultImage);
                imgPicture.setFitWidth(image.getWidth());
                imgPicture.setFitHeight(image.getHeight());
            });
        });
    }

    private void rebuildPreviews(Image writableImage) {
        try {
            LOGGER.debug("> Update all previews...");
            XmlScriptPreviewManager.updateAllPreviewsFromImage(
                    writableImage,
                    new ProgressListener() {
                        @Override
                        public void onStartProgress() {
                            Platform.runLater(() -> {
//                                disableUIForPreviewUpdate();
                                statusBarController.showWorker("Rebuild previews...");
                            });
                        }

                        @Override
                        public void onProgressUpdate(double progress) {
                            Platform.runLater(() -> statusBarController.updateWorkerProgress(progress));
                        }

                        @Override
                        public void onFinishProgress() {
                            Platform.runLater(() -> {
                                statusBarController.hideWorker();
//                                enableUIForPreviewUpdate();
                            });
                        }
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO
        }
    }

    private void saveImage() {
        LOGGER.debug("Save image...");

        final BufferedImage bufferedImage = new BufferedImage(
                (int) imgPicture.getImage().getWidth(),
                (int) imgPicture.getImage().getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        SwingFXUtils.fromFXImage(imgPicture.getImage(), bufferedImage);

        try (FileOutputStream fout = new FileOutputStream(currentFile.get())) {
            ImageIO.write(bufferedImage, FilenameUtils.getExtension(currentFile.get().getAbsolutePath()), fout);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadImage(String workText, final File newCurrentFile, final LoadImageListener listener) {
        if (newCurrentFile != null) {
            LOGGER.debug("Load image...");
        } else {
            LOGGER.debug("Create image...");
        }

        statusBarController.showWorker(workText);

//        disableUIForPreviewUpdate();

        Executors.newCachedThreadPool(new DaemonThreadFactory("Image loader")).submit(() -> {
            final Image image;
            try {
                image = listener.loadImage();
            } catch (final Exception e) {
                LOGGER.error("Unexpected error", e);
                Platform.runLater(() -> Dialogs.create().title("Unexpected error").message("An unexpected error has thrown!").showException(e));
                return;
            }

            if (newCurrentFile != null) {
                ConfigurationManager.APP.getReopenConfiguration().addImageFile(newCurrentFile);
                ConfigurationManager.APP.save();
            }

            if (image == null) { //Error
//                enableUIForPreviewUpdate();
                statusBarController.hideWorker();

                return;
            }

            Platform.runLater(() -> {
                imgPicture.setImage(image);
                imgPicture.setFitWidth(image.getWidth());
                imgPicture.setFitHeight(image.getHeight());

                imgMask.setImage(null);

                historyStack.clearStack();
                historyStack.addToStack(new UpdateImageHistoryAction(imgPicture, "Load image", imgPicture.getImage(), null));
            });
        });

        currentFile.set(newCurrentFile);
    }

//    private void disableUIForPreviewUpdate() {
//        accEffects.setDisable(true);
//        accRenderers.setDisable(true);
//        accLayers.setDisable(true);
//    }
//
//    private void enableUIForPreviewUpdate() {
//        accEffects.setDisable(false);
//        accRenderers.setDisable(false);
//        accLayers.setDisable(false);
//    }
}
