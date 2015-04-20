package org.pcsoft.tools.image_fx.ui.dialogs.controller;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.commons.io.IOUtils;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.AbstractDialogAction;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.pcsoft.tools.image_fx.common.threads.DaemonThreadFactory;
import org.pcsoft.tools.image_fx.core.threading.ImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptParsingException;
import org.pcsoft.tools.image_fx.scripting.threading.JavaScriptImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptLayerProperty;
import org.pcsoft.tools.image_fx.ui.building.ScriptUIBuilder;
import org.pcsoft.tools.image_fx.ui.dialogs.ScriptManagerDialogFactory;
import org.pcsoft.tools.image_fx.ui.items.list.LayerFragmentListItemFactory;
import org.pcsoft.tools.image_fx.ui.utils.ImageUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by Christoph on 22.06.2014.
 */
public class ScriptLayerBuilderDialogController implements Initializable {
    private final Image origImage;

    @FXML
    private ListView<ImageScriptLayerProperty.ImageScriptHolderProperty> lstScripts;
    @FXML
    private Button btnAddScript;
    @FXML
    private Button btnRemoveScript;
    @FXML
    private Button btnUpScript;
    @FXML
    private Button btnDownScript;
    @FXML
    private Accordion accScriptSettings;
    @FXML
    private ImageView imgPreview;
    @FXML
    private ImageView imgMask;
    @FXML
    private Button btnLoadMaskFromHeightMap;
    @FXML
    private Button btnClearMask;
    @FXML
    private TitledPane tpMask;
    @FXML
    private Pane pnlWait;
    @FXML
    private TitledPane tpOpacity;
    @FXML
    private Slider sldOpacity;

    private IntegerProperty listSizeProperty;
    private final AbstractDialogAction okAction;

    public ScriptLayerBuilderDialogController(Image origImage, AbstractDialogAction okAction) {
        this.origImage = ImageUtils.createPreviewThumbnail(origImage);
        this.okAction = okAction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnClearMask.disableProperty().bind(imgMask.imageProperty().isNull());
        tpMask.disableProperty().bind(lstScripts.getSelectionModel().selectedItemProperty().isNull());
        tpOpacity.disableProperty().bind(lstScripts.getSelectionModel().selectedItemProperty().isNull());

        listSizeProperty = new SimpleIntegerProperty(lstScripts.getItems().size());
        lstScripts.itemsProperty().get().addListener((ListChangeListener<ImageScriptLayerProperty.ImageScriptHolderProperty>) change -> {
            listSizeProperty.setValue(lstScripts.getItems().size());
        });
        lstScripts.getSelectionModel().selectedItemProperty().addListener((observableValue, oldScriptHolder, newScriptHolder) -> {
            accScriptSettings.getPanes().clear();
            imgMask.setImage(null);

            if (newScriptHolder == null)
                return;

            try {
                ScriptUIBuilder.buildScriptSettingsPage(
                        newScriptHolder.getScriptInfo(), accScriptSettings, imgPreview, null, (parameterInfo, value) -> {
                            if (value instanceof Point2D) {
                                final Point2D point2D = (Point2D) value;
                                newScriptHolder.getVariantInfo().updateParameterReferenceValue(
                                        parameterInfo, point2D.getX() + "," + point2D.getY());
                            } else {
                                newScriptHolder.getVariantInfo().updateParameterReferenceValue(
                                        parameterInfo, value.toString());
                            }
                            updatePreview();
                        }
                );
                if (newScriptHolder.getMask() != null) {
                    imgMask.setImage(new Image(new ByteArrayInputStream(newScriptHolder.getMask())));
                }
            } catch (ImageFXScriptParsingException e) {
                e.printStackTrace();
                Dialogs.create()
                        .owner(null)
                        .title("Error")
                        .message("Cannot build script settings page: " + e.getMessage())
                        .showException(e);
            }
        });
        okAction.disabledProperty().bind(listSizeProperty.lessThanOrEqualTo(0).or(pnlWait.visibleProperty()));

        btnRemoveScript.disableProperty().bind(lstScripts.getSelectionModel().selectedItemProperty().isNull());
        btnUpScript.disableProperty().bind(Bindings.or(
                lstScripts.getSelectionModel().selectedItemProperty().isNull(),
                lstScripts.getSelectionModel().selectedIndexProperty().lessThanOrEqualTo(0)
        ));
        btnDownScript.disableProperty().bind(Bindings.or(
                lstScripts.getSelectionModel().selectedItemProperty().isNull(),
                lstScripts.getSelectionModel().selectedIndexProperty().greaterThanOrEqualTo(listSizeProperty.subtract(1))
        ));

        imgPreview.setImage(origImage);
        LayerFragmentListItemFactory.useForList(origImage, lstScripts);

        sldOpacity.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            lstScripts.getSelectionModel().getSelectedItem().setOpacity(newValue.doubleValue());
            updatePreview();
        });
    }

    public List<ImageScriptLayerProperty.ImageScriptHolderProperty> getLayerScriptElementList() {
        return new ArrayList<>(lstScripts.getItems());
    }

    private void updatePreview() {
        final Image image = JavaScriptImageWriterTaskRunner.runJavaScriptTask(
                origImage, null, 1,
                lstScripts.getItems().stream().map(imageScriptHolderProperty -> imageScriptHolderProperty.get()).collect(Collectors.toList()),
                null
        );
        imgPreview.setImage(image);
    }

    @FXML
    private void onActionAddScript(ActionEvent actionEvent) {
        final ScriptManagerDialogFactory.Result result =
                ScriptManagerDialogFactory.show(origImage, imgPreview.getImage(), null, true);

        if (result != null) {
            lstScripts.getItems().add(new ImageScriptLayerProperty.ImageScriptHolderProperty(result.getScriptElement(), result.getScriptVariant(), null, 1));
            updatePreview();
        }
    }

    @FXML
    private void onActionRemoveScript(ActionEvent actionEvent) {
        final Action action = Dialogs.create()
                .owner(null)
                .title("Delete")
                .message("You are sure to delete selected item?")
                .showConfirm();

        if (action != null && action == Dialog.Actions.YES) {
            lstScripts.getItems().remove(lstScripts.getSelectionModel().getSelectedIndex());
            updatePreview();
        }
    }

    @FXML
    private void onActionUpScript(ActionEvent actionEvent) {
        final int index = lstScripts.getSelectionModel().getSelectedIndex();

        final ImageScriptLayerProperty.ImageScriptHolderProperty item = lstScripts.getItems().remove(index);
        lstScripts.getItems().add(index - 1, item);

        updatePreview();
    }

    @FXML
    private void onActionDownScript(ActionEvent actionEvent) {
        final int index = lstScripts.getSelectionModel().getSelectedIndex();

        final ImageScriptLayerProperty.ImageScriptHolderProperty item = lstScripts.getItems().remove(index);
        lstScripts.getItems().add(index + 1, item);

        updatePreview();
    }

    @FXML
    private void onActionLoadMaskFromHeightMap(final ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All known image formats", "*.bmp", "*.jpg", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bitmap", "*.bmp"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Portable Network Image", "*.png"));

        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            pnlWait.setVisible(true);
            {
                final FadeTransition fadeTransition = new FadeTransition(new Duration(300), pnlWait);
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(1);
                fadeTransition.play();
            }

            try (final FileInputStream in = new FileInputStream(file)) {
                final byte[] bytes = IOUtils.toByteArray(in);

                Executors.newCachedThreadPool(new DaemonThreadFactory("Image builder")).submit(() -> {
                    final WritableImage image = ImageWriterTaskRunner.runMaskFromHeightMapTask(
                            new Image(new ByteArrayInputStream(bytes))
                    );
                    final byte[] maskBytes = ImageUtils.imageToByteArray(image);

                    Platform.runLater(() -> {
                        imgMask.setImage(image);
                        lstScripts.getSelectionModel().getSelectedItem().setMask(maskBytes);

                        updatePreview();

                        final FadeTransition fadeTransition = new FadeTransition(new Duration(300), pnlWait);
                        fadeTransition.setFromValue(1);
                        fadeTransition.setToValue(0);
                        fadeTransition.setOnFinished(ae -> pnlWait.setVisible(false));
                        fadeTransition.play();
                    });
                });
            } catch (IOException e) {
                e.printStackTrace();
                Dialogs.create().title("Error").message("Error while reading mask!").showException(e);
            }
        }
    }

    @FXML
    private void onActionClearMask(ActionEvent actionEvent) {
        imgMask.setImage(null);
        lstScripts.getSelectionModel().getSelectedItem().setMask(null);

        updatePreview();
    }
}
