package org.pcsoft.tools.image_fx.ui.windows.controllers;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.pcsoft.tools.image_fx.core.configuration.ConfigurationManager;
import org.pcsoft.tools.image_fx.core.threading.ImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.core.threading.manipulation.ImageMirrorWriterTask;
import org.pcsoft.tools.image_fx.core.threading.manipulation.ImageRotateWriterTask;
import org.pcsoft.tools.image_fx.plugins.common.exceptions.ImageFXPluginExecutionException;
import org.pcsoft.tools.image_fx.plugins.managing.PluginManager;
import org.pcsoft.tools.image_fx.plugins.managing.ToolingPluginManager;
import org.pcsoft.tools.image_fx.scripting.threading.JavaScriptImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayer;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.ui.dialogs.MaskStampDialogFactory;
import org.pcsoft.tools.image_fx.ui.dialogs.NewImageDialogFactory;
import org.pcsoft.tools.image_fx.ui.dialogs.RendererImageDialogFactory;
import org.pcsoft.tools.image_fx.ui.dialogs.ResizeImageDialogFactory;
import org.pcsoft.tools.image_fx.ui.dialogs.ScriptLayerManagerDialogFactory;
import org.pcsoft.tools.image_fx.ui.dialogs.ScriptManagerDialogFactory;
import org.pcsoft.tools.image_fx.ui.utils.HistoryStack;
import org.pcsoft.tools.image_fx.ui.utils.ImageUtils;
import org.pcsoft.tools.image_fx.ui.utils.actions.MultiHistoryAction;
import org.pcsoft.tools.image_fx.ui.utils.actions.UpdateImageHistoryAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * Created by pfeifchr on 13.08.2014.
 */
public class MainWindowMenuController extends AbstractController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindowMenuController.class);

    static interface MenuListener {
        void applyScriptOnImage(ImageScriptElement scriptInfo, ImageScriptVariant variantInfo);

        void applyScriptOnImage(ImageScriptLayer scriptLayer);

        void saveImage();

        void loadImage(String workText, File newCurrentFile, MainWindowController.LoadImageListener listener);
    }

    @FXML
    private MenuItem miCopy;
    @FXML
    private MenuItem miSaveAs;
    @FXML
    private MenuItem miSave;
    @FXML
    private MenuItem miRedo;
    @FXML
    private MenuItem miUndo;
    @FXML
    private CheckMenuItem miShowMask;
    @FXML
    private MenuItem miMaskFromHeightMapLoad;
    @FXML
    private MenuItem miMaskToHeightMapSave;
    @FXML
    private MenuItem miClearMask;
    @FXML
    private Menu mnuMask;
    @FXML
    private MenuItem miInvertMask;
    @FXML
    private MenuItem miSplitMaskSpectrum;
    @FXML
    private MenuItem miUndoMask;
    @FXML
    private MenuItem miRedoMask;
    @FXML
    private Menu mnuTools;
    @FXML
    private Menu mnuMaskFromHeightMapReopen;
    @FXML
    private MenuItem miMaskFromClipboard;
    @FXML
    private MenuItem miMaskFromStamp;
    @FXML
    private Menu mnuImage;
    @FXML
    private MenuItem miBlurMask;
    @FXML
    private Menu mnuReopen;
    @FXML
    private MenuItem miMaskRotateLeft;
    @FXML
    private MenuItem miMaskRotateRight;
    @FXML
    private MenuItem miMaskMirrorHorizontal;
    @FXML
    private MenuItem miMaskMirrorVertical;

    //Toolbar
    @FXML
    private Button btnUndo;
    @FXML
    private Button btnRedo;

    protected ObjectProperty<File> currentFile;
    protected HistoryStack historyStack;

    protected ImageView imgPicture;
    protected ImageView imgMask;

    private final DoubleProperty opacity = new SimpleDoubleProperty(1d);

    //Helpers
    private IntegerProperty mnuToolsSize = new SimpleIntegerProperty(0);
    private IntegerProperty mnuReopenSize = new SimpleIntegerProperty(0);
    private IntegerProperty mnuMaskFromHeightMapReopenSize = new SimpleIntegerProperty(0);
    private MenuListener menuListener = null;

    public MainWindowMenuController(Stage parent) {
        super(parent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LOGGER.info("Initialize UI: Main Window Controller (MENU)");
    }

    void update(ImageView imgPicture, ImageView imgMask, CheckBox ckbShowMask, Slider sldOpacity, HistoryStack historyStack,
                ObjectProperty<File> currentFile, MenuListener menuListener) {
        LOGGER.debug("Update UI: Main Window Controller (MENU)");

        this.imgPicture = imgPicture;
        this.imgMask = imgMask;
        this.historyStack = historyStack;
        this.currentFile = currentFile;
        this.menuListener = menuListener;

        miUndo.disableProperty().bind(historyStack.canUndoProperty().not());
        miUndo.textProperty().bind(historyStack.undoMessageProperty());
        final Tooltip undoTooltip = new Tooltip();
        undoTooltip.textProperty().bind(historyStack.undoMessageProperty());
        Tooltip.install(btnUndo, undoTooltip);
        miRedo.disableProperty().bind(historyStack.canRedoProperty().not());
        miRedo.textProperty().bind(historyStack.redoMessageProperty());
        final Tooltip redoTooltip = new Tooltip();
        redoTooltip.textProperty().bind(historyStack.redoMessageProperty());
        Tooltip.install(btnRedo, redoTooltip);
        miSave.disableProperty().bind(imgPicture.imageProperty().isNull());
        miSaveAs.disableProperty().bind(imgPicture.imageProperty().isNull());
        miCopy.disableProperty().bind(imgPicture.imageProperty().isNull());
        miClearMask.disableProperty().bind(imgPicture.imageProperty().isNull().or(imgMask.imageProperty().isNull()));
        miMaskRotateLeft.disableProperty().bind(imgPicture.imageProperty().isNull().or(imgMask.imageProperty().isNull()));
        miMaskRotateRight.disableProperty().bind(imgPicture.imageProperty().isNull().or(imgMask.imageProperty().isNull()));
        miMaskMirrorHorizontal.disableProperty().bind(imgPicture.imageProperty().isNull().or(imgMask.imageProperty().isNull()));
        miMaskMirrorVertical.disableProperty().bind(imgPicture.imageProperty().isNull().or(imgMask.imageProperty().isNull()));
        miInvertMask.disableProperty().bind(imgPicture.imageProperty().isNull().or(imgMask.imageProperty().isNull()));
        miSplitMaskSpectrum.disableProperty().bind(imgPicture.imageProperty().isNull().or(imgMask.imageProperty().isNull()));
        miBlurMask.disableProperty().bind(imgPicture.imageProperty().isNull().or(imgMask.imageProperty().isNull()));
        miMaskFromHeightMapLoad.disableProperty().bind(imgPicture.imageProperty().isNull());
        miMaskFromClipboard.disableProperty().bind(imgPicture.imageProperty().isNull());
        miMaskFromStamp.disableProperty().bind(imgPicture.imageProperty().isNull());
        mnuMaskFromHeightMapReopen.disableProperty().bind(imgPicture.imageProperty().isNull().or(mnuMaskFromHeightMapReopenSize.lessThanOrEqualTo(0)));
        mnuMaskFromHeightMapReopen.getItems().addListener((ListChangeListener<MenuItem>) change -> mnuMaskFromHeightMapReopenSize.set(mnuMaskFromHeightMapReopen.getItems().size()));
        mnuReopen.disableProperty().bind(mnuReopenSize.lessThanOrEqualTo(0));
        mnuReopen.getItems().addListener((ListChangeListener<MenuItem>) change -> mnuReopenSize.set(mnuReopen.getItems().size()));
        miMaskToHeightMapSave.disableProperty().bind(imgMask.imageProperty().isNull().or(imgPicture.imageProperty().isNull()));
        miShowMask.selectedProperty().bindBidirectional(ckbShowMask.selectedProperty());
        miShowMask.disableProperty().bind(imgMask.imageProperty().isNull());
        mnuMask.disableProperty().bind(imgPicture.imageProperty().isNull());
        mnuTools.visibleProperty().bind(mnuToolsSize.greaterThan(0));
        mnuTools.getItems().addListener((ListChangeListener<MenuItem>) change -> mnuToolsSize.setValue(mnuTools.getItems().size()));
        mnuImage.disableProperty().bind(imgPicture.imageProperty().isNull());
        opacity.bind(sldOpacity.valueProperty());

        ConfigurationManager.APP.getReopenConfiguration().getHeightMapFileList().addListener(
                (ListChangeListener<File>) change -> prepareHeightMapReopenMenu()
        );
        ConfigurationManager.APP.getReopenConfiguration().getImageFileList().addListener(
                (ListChangeListener<File>) change -> prepareImageReopenMenu()
        );

        prepareToolsMenu(imgPicture, imgMask, sldOpacity);
        prepareHeightMapReopenMenu();
        prepareImageReopenMenu();
    }

    private void prepareHeightMapReopenMenu() {
        mnuMaskFromHeightMapReopen.getItems().clear();
        for (final File file : ConfigurationManager.APP.getReopenConfiguration().getHeightMapFileList()) {
            final MenuItem miFile = new MenuItem(file.getAbsolutePath());
            miFile.setOnAction(actionEvent -> {
                try (final FileInputStream fin = new FileInputStream(file)) {
                    final Image image = ImageWriterTaskRunner.runMaskFromHeightMapTask(
                            new Image(fin, imgPicture.getImage().getWidth(), imgPicture.getImage().getHeight(), false, true)
                    );

                    historyStack.addToStack(
                            new UpdateImageHistoryAction(imgMask, "Reload mask from height map", image, imgMask.getImage())
                    );
                    imgMask.setImage(image);
                } catch (IOException e) {
                    Dialogs.create()
                            .title("Error while loading mask")
                            .message("There are problems while loading height map for mask.")
                            .showException(e);
                }
            });

            mnuMaskFromHeightMapReopen.getItems().add(miFile);
        }
    }

    private void prepareImageReopenMenu() {
        mnuReopen.getItems().clear();
        for (final File file : ConfigurationManager.APP.getReopenConfiguration().getImageFileList()) {
            final MenuItem miFile = new MenuItem(file.getAbsolutePath());
            miFile.setOnAction(actionEvent -> menuListener.loadImage("Reload image file...", file, () -> {
                try (final FileInputStream fin = new FileInputStream(file)) {
                    return new Image(fin);
                } catch (IOException e) {
                    Dialogs.create()
                            .title("Error while loading image")
                            .message("There are problems while loading image.")
                            .showException(e);
                    return null;
                }
            }));

            mnuReopen.getItems().add(miFile);
        }
    }

    private void prepareToolsMenu(ImageView imgPicture, ImageView imgMask, Slider sldOpacity) {
        final Map<String, List<ToolingPluginManager.Plugin>> groupedPluginMap = PluginManager.TOOLING_PLUGIN_MANAGER.getGroupedPluginMap();
        for (String group : groupedPluginMap.keySet()) {
            final Menu menu;
            if (group == null || group.isEmpty()) {
                menu = mnuTools;
            } else {
                menu = new Menu(group);
                mnuTools.getItems().add(menu);
            }

            final List<ToolingPluginManager.Plugin> pluginList = groupedPluginMap.get(group);
            for (final ToolingPluginManager.Plugin plugin : pluginList) {
                final MenuItem miPlugin = new MenuItem(plugin.getName());
                miPlugin.setOnAction(actionEvent -> {
                    try {
                        plugin.execute(
                                currentFile.get(), imgPicture.getImage(), imgMask.getImage(), sldOpacity.getOpacity()
                        );
                    } catch (ImageFXPluginExecutionException e) {
                        LOGGER.error("There are problems while executing the plugin: " + plugin.getName(), e);
                        Dialogs.create()
                                .title("Plugin Execution Error")
                                .message("There are problems while executing the plugin: " + plugin.getName())
                                .showException(e);
                    }
                });
                if (plugin.isNeedLoadedImage()) {
                    miPlugin.disableProperty().bind(imgPicture.imageProperty().isNull());
                }
                if (plugin.hasKeyCombination()) {
                    miPlugin.setAccelerator(KeyCombination.valueOf(plugin.getKeyCombination()));
                }
                if (plugin.hasIcon()) {
                    miPlugin.setGraphic(new ImageView(new Image(
                            Thread.currentThread().getContextClassLoader().getResourceAsStream(plugin.getIconPath())
                    )));
                }

                menu.getItems().add(miPlugin);
            }
        }
    }

    @FXML
    private void onActionOpen(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All known image formats", "*.bmp", "*.jpg", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bitmap", "*.bmp"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Portable Network Image", "*.png"));

        final File file = fileChooser.showOpenDialog(parent);
        if (file != null) {
            menuListener.loadImage("Load image...", file, () -> {
                try (FileInputStream in = new FileInputStream(file)) {
                    return new Image(in);
                } catch (IOException e) {
                    LOGGER.error("Cannot load image!", e);
                    Platform.runLater(() ->
                                    Dialogs.create()
                                            .title("Failed to load image")
                                            .message("Cannot load image: " + file.getAbsolutePath())
                                            .showException(e)
                    );

                    return null;
                }
            });
        }
    }

    @FXML
    private void onActionSave(ActionEvent actionEvent) {
        if (currentFile.get() == null) {
            onActionSaveAs(actionEvent);
            return;
        }

        menuListener.saveImage();
    }

    @FXML
    private void onActionSaveAs(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All known image formats", "*.bmp", "*.jpg", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bitmap", "*.bmp"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Portable Network Image", "*.png"));

        final File file = fileChooser.showSaveDialog(parent);
        if (file != null) {
            currentFile.set(file);
            menuListener.saveImage();
        }
    }

    @FXML
    private void onActionClose(ActionEvent actionEvent) {
        parent.close();
    }

    @FXML
    private void onActionUndo(ActionEvent actionEvent) {
        historyStack.undo();
    }

    @FXML
    private void onActionRedo(ActionEvent actionEvent) {
        historyStack.redo();
    }

    @FXML
    private void onActionCopy(ActionEvent actionEvent) {
        final BufferedImage bufferedImage = new BufferedImage(
                (int) imgPicture.getImage().getWidth(),
                (int) imgPicture.getImage().getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        SwingFXUtils.fromFXImage(imgPicture.getImage(), bufferedImage);

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                final DataFlavor[] flavors = new DataFlavor[1];
                flavors[0] = DataFlavor.imageFlavor;
                return flavors;
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                DataFlavor[] flavors = getTransferDataFlavors();
                for (int i = 0; i < flavors.length; i++) {
                    if (flavor.equals(flavors[i])) {
                        return true;
                    }
                }

                return false;
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (flavor.equals(DataFlavor.imageFlavor))
                    return bufferedImage;

                throw new UnsupportedFlavorException(flavor);
            }
        }, (clipboard, contents) -> {
        });
    }

    @FXML
    private void onActionNewEmpty(ActionEvent actionEvent) {
        final NewImageDialogFactory.Result dialogResult = NewImageDialogFactory.show();
        if (dialogResult != null) {
            menuListener.loadImage("Create image...", null, () -> {
                final WritableImage image = new WritableImage(dialogResult.getWidth(), dialogResult.getHeight());
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        image.getPixelWriter().setColor(x, y, dialogResult.getBackgroundColor());
                    }
                }

                return image;
            });
        }
    }

    @FXML
    private void onActionScriptManager(ActionEvent actionEvent) {
        final ScriptManagerDialogFactory.Result result = ScriptManagerDialogFactory.show(
                imgPicture.getImage() == null ?
                        new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/example.png")) :
                        imgPicture.getImage(),
                null, imgPicture.getImage() != null
        );

        if (result != null) {
            menuListener.applyScriptOnImage(result.getScriptElement(), result.getScriptVariant());
        }
    }

    @FXML
    private void onActionScriptGroupManager(ActionEvent actionEvent) {
        final ScriptLayerManagerDialogFactory.Result dialogResult = ScriptLayerManagerDialogFactory.show(
                imgPicture.getImage() == null ?
                        new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/example.png")) :
                        imgPicture.getImage(),
                imgPicture.getImage() != null
        );

        if (dialogResult != null) {
            menuListener.applyScriptOnImage(dialogResult.getScriptLayer());
        }
    }

    @FXML
    private void onActionNewFromClipboard(ActionEvent actionEvent) {
        menuListener.loadImage("Load from clipboard", null, () -> {
            try (final ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
                final BufferedImage bufferedImage = (BufferedImage) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.imageFlavor);
                ImageIO.write(bufferedImage, "png", bout);

                historyStack.clearStack();

                return new Image(new ByteArrayInputStream(bout.toByteArray()));
            } catch (Exception e) {
                LOGGER.warn("No image in clipboard!", e);
                Platform.runLater(() -> Dialogs.create().owner(parent).title("Error").message("Cannot use image in clipboard: no image found!").showError());

                return null;
            }
        });
    }

    @FXML
    private void onActionNewFromRenderer(ActionEvent actionEvent) {
        final RendererImageDialogFactory.Result result = RendererImageDialogFactory.show();
        if (result != null) {
            menuListener.loadImage("Create image from renderer...", null, () -> {
                final Image image = new WritableImage(result.getWidth(), result.getHeight());
                return JavaScriptImageWriterTaskRunner.runJavaScriptTask(
                        image, null, 1, result.getScriptElement(), result.getScriptVariant(), null);
            });
        }
    }

    @FXML
    private void onActionLoadMaskFromHeightMap(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All known image formats", "*.bmp", "*.jpg", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bitmap", "*.bmp"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Portable Network Image", "*.png"));

        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try (final FileInputStream inputStream = new FileInputStream(file)) {
                final Image image = ImageUtils.createMaskFromHeightMap(inputStream,
                        (int) imgPicture.getImage().getWidth(), (int) imgPicture.getImage().getHeight());
                historyStack.addToStack(new UpdateImageHistoryAction(imgMask, "Mask loaded from heightmap", image, imgMask.getImage()));
                imgMask.setImage(image);

                ConfigurationManager.APP.getReopenConfiguration().addHeightMapFile(file);
                ConfigurationManager.APP.save();
            } catch (IOException e) {
                LOGGER.error("Failed to load height map!", e);
                Dialogs.create().title("Error").message("Cannot load height map!").showException(e);
            }
        }
    }

    @FXML
    private void onActionClearMask(ActionEvent actionEvent) {
        historyStack.addToStack(new UpdateImageHistoryAction(imgMask, "Clear mask", null, imgMask.getImage()));
        imgMask.setImage(null);
    }

    @FXML
    private void onActionSaveMaskToHeightMap(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All known image formats", "*.bmp", "*.jpg", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bitmap", "*.bmp"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Portable Network Image", "*.png"));

        final File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            final WritableImage heightMapMask = ImageWriterTaskRunner.runMaskToHeightMapTask(imgMask.getImage());
            try {
                FileUtils.writeByteArrayToFile(file, ImageUtils.imageToByteArray(heightMapMask));
            } catch (IOException e) {
                LOGGER.error("Failed to save mask!", e);
                Dialogs.create().title("Error while saving mask").message("Cannot save mask to file!").showException(e);
            }
        }
    }

    @FXML
    private void onActionSetupMaskColor(ActionEvent actionEvent) {
        final Dialog dialog = new Dialog(null, "Choose mask color");
        dialog.setMasthead("Here you can setup the color to use for mask highlighting.");
        dialog.setResizable(false);
        dialog.getActions().addAll(Dialog.Actions.OK, Dialog.Actions.CANCEL);

        final ColorPicker colorPicker = new ColorPicker(ConfigurationManager.APP.getMaskConfiguration().getColor());
        colorPicker.setMaxWidth(Double.MAX_VALUE);
        dialog.setContent(new VBox(
                new Label("Choose a color for the mask:"),
                colorPicker
        ));

        if (dialog.show() == Dialog.Actions.OK) {
            ConfigurationManager.APP.getMaskConfiguration().setColor(colorPicker.getValue());
            ConfigurationManager.APP.save();

            if (imgMask.getImage() != null) {
                imgMask.setImage(ImageWriterTaskRunner.runMaskResetTask(imgMask.getImage()));
            }
        }
    }

    @FXML
    private void onActionSplitMaskSpectrum(ActionEvent actionEvent) {
        final WritableImage image = ImageWriterTaskRunner.runMaskSpectrumSplitterTask(imgMask.getImage());

        historyStack.addToStack(new UpdateImageHistoryAction(imgMask, "Apply 'Split Spectrum' on mask", image, imgMask.getImage()));
        imgMask.setImage(image);
    }

    @FXML
    private void onActionInvertMask(ActionEvent actionEvent) {
        final WritableImage image = ImageWriterTaskRunner.runMaskInverterTask(imgMask.getImage());

        historyStack.addToStack(new UpdateImageHistoryAction(imgMask, "Invert mask", image, imgMask.getImage()));
        imgMask.setImage(image);
    }

    @FXML
    private void onActionMaskFromClipboard(ActionEvent actionEvent) {
        try (final ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
            final BufferedImage bufferedImage = (BufferedImage) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.imageFlavor);
            ImageIO.write(bufferedImage, "png", bout);

            final Image image = ImageUtils.createMaskFromHeightMap(new ByteArrayInputStream(bout.toByteArray()),
                    (int) imgPicture.getImage().getWidth(), (int) imgPicture.getImage().getHeight());

            historyStack.addToStack(new UpdateImageHistoryAction(imgMask, "Insert mask from clipboard", image, imgMask.getImage()));
            imgMask.setImage(image);
        } catch (Exception e) {
            LOGGER.warn("No image in clipboard!", e);
            Platform.runLater(() -> Dialogs.create().owner(parent).title("Error").message("Cannot use image in clipboard: no image found!").showError());
        }
    }

    @FXML
    private void onActionMaskFromStamp(ActionEvent actionEvent) {
        final MaskStampDialogFactory.Result result = MaskStampDialogFactory.show();
        if (result != null) {
            final Image image = ImageUtils.createMaskFromHeightMap(new ByteArrayInputStream(ImageUtils.imageToByteArray(result.getImage())),
                    (int) imgPicture.getImage().getWidth(), (int) imgPicture.getImage().getHeight());

            historyStack.addToStack(new UpdateImageHistoryAction(imgMask, "Insert mask stamp", image, imgMask.getImage()));
            imgMask.setImage(image);
        }
    }

    @FXML
    private void onActionImageInformation(ActionEvent actionEvent) {

    }

    @FXML
    private void onActionImageRotateLeft(ActionEvent actionEvent) {
        final WritableImage image = ImageWriterTaskRunner.runImageRotateWriterTask(
                imgPicture.getImage(), imgMask.getImage(), opacity.get(),
                ImageRotateWriterTask.Rotation.Left
        );
        final UpdateImageHistoryAction imageHistoryAction =
                new UpdateImageHistoryAction(imgPicture, "Rotate image left", image, imgPicture.getImage());

        if (imgMask.getImage() != null) {
            final Image mask = new Image(
                    new ByteArrayInputStream(ImageUtils.imageToByteArray(imgMask.getImage())),
                    image.getWidth(), image.getHeight(), false, true
            );
            historyStack.addToStack(new MultiHistoryAction(
                    imageHistoryAction,
                    new UpdateImageHistoryAction(imgMask, "Resize mask", mask, imgMask.getImage())
            ));

            imgMask.setImage(mask);
        } else {
            historyStack.addToStack(imageHistoryAction);
        }

        imgPicture.setImage(image);
    }

    @FXML
    private void onActionImageRotateRight(ActionEvent actionEvent) {
        final WritableImage image = ImageWriterTaskRunner.runImageRotateWriterTask(
                imgPicture.getImage(), imgMask.getImage(), opacity.get(),
                ImageRotateWriterTask.Rotation.Right
        );
        final UpdateImageHistoryAction imageHistoryAction =
                new UpdateImageHistoryAction(imgPicture, "Rotate image right", image, imgPicture.getImage());

        if (imgMask.getImage() != null) {
            final Image mask = new Image(
                    new ByteArrayInputStream(ImageUtils.imageToByteArray(imgMask.getImage())),
                    image.getWidth(), image.getHeight(), false, true
            );
            historyStack.addToStack(new MultiHistoryAction(
                    imageHistoryAction,
                    new UpdateImageHistoryAction(imgMask, "Resize mask", mask, imgMask.getImage())
            ));

            imgMask.setImage(mask);
        } else {
            historyStack.addToStack(imageHistoryAction);
        }

        imgPicture.setImage(image);
    }

    @FXML
    private void onActionImageRotateExact(ActionEvent actionEvent) {
        Dialogs.create().title("Exact rotation").message("Not implemented!").showInformation();
    }

    @FXML
    private void onActionImageMirrorHorizontal(ActionEvent actionEvent) {
        final WritableImage image = ImageWriterTaskRunner.runImageMirrorWriterTask(
                imgPicture.getImage(), imgMask.getImage(), opacity.get(),
                ImageMirrorWriterTask.Orientation.Horizontal
        );

        historyStack.addToStack(new UpdateImageHistoryAction(imgPicture, "Mirror image in horizontal", image, imgPicture.getImage()));
        imgPicture.setImage(image);
    }

    @FXML
    private void onActionImageMirrorVertical(ActionEvent actionEvent) {
        final WritableImage image = ImageWriterTaskRunner.runImageMirrorWriterTask(
                imgPicture.getImage(), imgMask.getImage(), opacity.get(),
                ImageMirrorWriterTask.Orientation.Vertical
        );

        historyStack.addToStack(new UpdateImageHistoryAction(imgPicture, "Mirror image in vertical", image, imgPicture.getImage()));
        imgPicture.setImage(image);
    }

    @FXML
    private void onActionImageChangeDimension(ActionEvent actionEvent) {
        final ResizeImageDialogFactory.Result result =
                ResizeImageDialogFactory.show((int) imgPicture.getImage().getWidth(), (int) imgPicture.getImage().getHeight());

        if (result != null) {
            final Image image = new Image(
                    new ByteArrayInputStream(ImageUtils.imageToByteArray(imgPicture.getImage())),
                    result.getWidth(), result.getHeight(), false, result.isSmooth()
            );
            final UpdateImageHistoryAction imageHistoryAction =
                    new UpdateImageHistoryAction(imgPicture, "Resize image", image, imgPicture.getImage());

            if (imgMask.getImage() != null) {
                final Image mask = new Image(
                        new ByteArrayInputStream(ImageUtils.imageToByteArray(imgMask.getImage())),
                        result.getWidth(), result.getHeight(), false, true
                );
                historyStack.addToStack(new MultiHistoryAction(
                        imageHistoryAction,
                        new UpdateImageHistoryAction(imgMask, "Resize mask", mask, imgMask.getImage())
                ));

                imgMask.setImage(mask);
            } else {
                historyStack.addToStack(imageHistoryAction);
            }

            imgPicture.setImage(image);
        }
    }

    @FXML
    private void onActionImageChangeColorDepth(ActionEvent actionEvent) {
        Dialogs.create().title("Change color depth").message("Not implemented!").showInformation();
    }

    @FXML
    private void onActionBlurMask(ActionEvent actionEvent) {
        final WritableImage image = ImageWriterTaskRunner.runMaskBlurTask(imgMask.getImage());

        historyStack.addToStack(new UpdateImageHistoryAction(imgMask, "Apply 'Blur' on mask", image, imgMask.getImage()));
        imgMask.setImage(image);
    }

    @FXML
    private void onActionMaskRotateLeft(ActionEvent actionEvent) {
        final WritableImage writableImage =
                ImageWriterTaskRunner.runImageRotateWriterTask(
                        imgMask.getImage(), null, 1d, ImageRotateWriterTask.Rotation.Left);
        final Image image = new Image(
                new ByteArrayInputStream(ImageUtils.imageToByteArray(writableImage)),
                imgPicture.getImage().getWidth(), imgPicture.getImage().getHeight(), false, true
        );

        historyStack.addToStack(new UpdateImageHistoryAction(imgMask, "Rotate mask left", image, imgMask.getImage()));
        imgMask.setImage(image);
    }

    @FXML
    private void onActionMaskRotateRight(ActionEvent actionEvent) {
        final WritableImage writableImage =
                ImageWriterTaskRunner.runImageRotateWriterTask(
                        imgMask.getImage(), null, 1d, ImageRotateWriterTask.Rotation.Right);
        final Image image = new Image(
                new ByteArrayInputStream(ImageUtils.imageToByteArray(writableImage)),
                imgPicture.getImage().getWidth(), imgPicture.getImage().getHeight(), false, true
        );

        historyStack.addToStack(new UpdateImageHistoryAction(imgMask, "Rotate mask right", image, imgMask.getImage()));
        imgMask.setImage(image);
    }

    @FXML
    private void onActionMaskMirrorHorizontal(ActionEvent actionEvent) {
        final WritableImage image =
                ImageWriterTaskRunner.runImageMirrorWriterTask(
                        imgMask.getImage(), null, 1d, ImageMirrorWriterTask.Orientation.Horizontal);

        historyStack.addToStack(new UpdateImageHistoryAction(imgMask, "Mirror mask in horizontal", image, imgMask.getImage()));
        imgMask.setImage(image);
    }

    @FXML
    private void onActionMaskMirrorVertical(ActionEvent actionEvent) {
        final WritableImage image =
                ImageWriterTaskRunner.runImageMirrorWriterTask(
                        imgMask.getImage(), null, 1d, ImageMirrorWriterTask.Orientation.Vertical);

        historyStack.addToStack(new UpdateImageHistoryAction(imgMask, "Mirror mask in vertical", image, imgMask.getImage()));
        imgMask.setImage(image);
    }

    @FXML
    private void onActionImageInvert(ActionEvent actionEvent) {
        final WritableImage image = ImageWriterTaskRunner.runImageInvertWriterTask(
                imgPicture.getImage(), imgMask.getImage(), opacity.get());

        historyStack.addToStack(new UpdateImageHistoryAction(imgPicture, "Invert image", image, imgPicture.getImage()));
        imgPicture.setImage(image);
    }

    @FXML
    private void onActionImageGrayscale(ActionEvent actionEvent) {
        final WritableImage image = ImageWriterTaskRunner.runImageGrayscaleWriterTask(
                imgPicture.getImage(), imgMask.getImage(), opacity.get());

        historyStack.addToStack(new UpdateImageHistoryAction(imgPicture, "Gray scale image", image, imgPicture.getImage()));
        imgPicture.setImage(image);
    }

    @FXML
    private void onActionImageBlackWhite(ActionEvent actionEvent) {
        final WritableImage image = ImageWriterTaskRunner.runImageBlackWhiteWriterTask(
                imgPicture.getImage(), imgMask.getImage(), opacity.get());

        historyStack.addToStack(new UpdateImageHistoryAction(imgPicture, "Black-White image", image, imgPicture.getImage()));
        imgPicture.setImage(image);
    }
}
