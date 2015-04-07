package org.pcsoft.tools.image_fx.ui.dialogs.controller;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.util.Callback;
import org.controlsfx.dialog.AbstractDialogAction;
import org.controlsfx.dialog.Dialogs;
import org.pcsoft.tools.image_fx.scripting.XmlEffectScriptManager;
import org.pcsoft.tools.image_fx.scripting.XmlRendererScriptManager;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptParsingException;
import org.pcsoft.tools.image_fx.scripting.threading.JavaScriptImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.ui.building.ScriptUIBuilder;
import org.pcsoft.tools.image_fx.ui.components.addons.tree.ImageScriptElementTreeItem;
import org.pcsoft.tools.image_fx.ui.components.addons.tree.ImageScriptTypeTreeItem;
import org.pcsoft.tools.image_fx.ui.dialogs.ScriptManagerDialogFactory;
import org.pcsoft.tools.image_fx.ui.items.tree.ScriptTreeItemFactory;
import org.pcsoft.tools.image_fx.ui.utils.ImageUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Christoph on 22.06.2014.
 */
public class ScriptManagerDialogController implements Initializable {

    private static final String GROUP_DEFAULT = "group.default";

    @FXML
    private Accordion accScriptSettings;
    @FXML
    private TreeView<Object> tvScript;
    @FXML
    private ImageView imgPreview;
    @FXML
    private ImageView imgSecondPreview;
    @FXML
    private TitledPane tpScriptSettings;
    @FXML
    private ListView<ImageScriptVariant> lstVariants;
    @FXML
    private Button btnAddVariant;
    @FXML
    private Button btnRemoveVariant;

    private final Image origImage;
    private final Image secondOrigImage;
    private final ImageScriptElement scriptInfo;
    private final boolean applicable;
    private final AbstractDialogAction applyAction;
    private final ScriptManagerDialogFactory.ScriptView scriptView;
    private ImageScriptElement selectedScriptElement;
    private ImageScriptVariant scriptVariant;

    //****** SPECIAL ******\\
    private ChangeListener<ImageScriptVariant> imageScriptVariantChangeListener = null;

    public ScriptManagerDialogController(Image image, Image secondImage, ImageScriptElement scriptInfo, boolean applicable,
                                         ScriptManagerDialogFactory.ScriptView scriptView, AbstractDialogAction applyAction) {
        this.origImage = ImageUtils.createPreviewThumbnail(image);
        this.secondOrigImage = secondImage == null ? null : ImageUtils.createPreviewThumbnail(secondImage);
        this.scriptInfo = scriptInfo;
        this.applicable = applicable;
        this.scriptView = scriptView;
        this.applyAction = applyAction;

        this.selectedScriptElement = scriptInfo;
        if (this.selectedScriptElement != null) {
            scriptVariant = this.selectedScriptElement.getDefaultVariant();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imgPreview.setImage(origImage);
        imgSecondPreview.setImage(secondOrigImage);
        if (!applicable) {
            applyAction.setDisabled(true);
        } else {
            tvScript.getSelectionModel().selectedItemProperty().addListener(
                    (observableValue, oldValue, newValue) ->
                            applyAction.setDisabled(newValue == null || !(newValue instanceof ImageScriptElementTreeItem))
            );
        }

        ScriptTreeItemFactory.useForTree(origImage, tvScript);
        tvScript.setRoot(buildTreeItems());
        tvScript.setShowRoot(false);
        tvScript.getSelectionModel().selectedItemProperty().addListener((observableValue, oldTI, newTI) -> {
            accScriptSettings.getPanes().clear();
            imgPreview.setImage(origImage);
            imgSecondPreview.setImage(secondOrigImage);

            lstVariants.getItems().clear();
            lstVariants.setDisable(newTI == null || !(newTI instanceof ImageScriptElementTreeItem));
            btnAddVariant.setDisable(newTI == null || !(newTI instanceof ImageScriptElementTreeItem));
            btnRemoveVariant.setDisable(newTI == null || !(newTI instanceof ImageScriptElementTreeItem));

            if (newTI != null && newTI instanceof ImageScriptElementTreeItem) {
                final ImageScriptElementTreeItem imageScriptElementTreeItem = (ImageScriptElementTreeItem) newTI;

                if (imageScriptVariantChangeListener != null) {
                    lstVariants.getSelectionModel().selectedItemProperty().removeListener(imageScriptVariantChangeListener);
                }
                imageScriptVariantChangeListener = (ov, oldVI, newVI) -> {
                    btnRemoveVariant.setDisable(newVI == null || newVI.isReadOnly());
                    if (newVI != null) {
                        imageScriptElementTreeItem.setVariantInfo(new ImageScriptVariant(newVI.getVariant(), newVI.isReadOnly()));
                        updatePreviewImage(imageScriptElementTreeItem.getScriptElement(), imageScriptElementTreeItem.getScriptVariant());
                    }
                };
                lstVariants.getSelectionModel().selectedItemProperty().addListener(imageScriptVariantChangeListener);
                lstVariants.getItems().addAll(imageScriptElementTreeItem.getScriptElement().getVariantMap().values());
                lstVariants.getSelectionModel().select(imageScriptElementTreeItem.getScriptElement().getDefaultVariant());

                try {
                    ScriptUIBuilder.buildScriptSettingsPage(
                            imageScriptElementTreeItem.getScriptElement(), accScriptSettings, imgPreview, lstVariants, (parameterInfo, value) -> {
                                if (value instanceof Point2D) {
                                    final Point2D point2D = (Point2D) value;
                                    imageScriptElementTreeItem.getScriptVariant().updateParameterReferenceValue(
                                            parameterInfo, point2D.getX() + "," + point2D.getY());
                                } else {
                                    imageScriptElementTreeItem.getScriptVariant().updateParameterReferenceValue(
                                            parameterInfo, value.toString());
                                }
                                updatePreviewImage(imageScriptElementTreeItem.getScriptElement(), imageScriptElementTreeItem.getScriptVariant());
                            }
                    );
                    updatePreviewImage(imageScriptElementTreeItem.getScriptElement(), imageScriptElementTreeItem.getScriptVariant());
                } catch (ImageFXScriptParsingException e) {
                    e.printStackTrace();//TODO
                }

                selectedScriptElement = imageScriptElementTreeItem.getScriptElement();
                scriptVariant = imageScriptElementTreeItem.getScriptVariant();
            }
        });
        final TreeItem<Object> treeItem = (TreeItem<Object>) findTreeItem(scriptInfo, tvScript.getRoot());
        if (treeItem != null) {
            tvScript.getSelectionModel().select(treeItem);
        }

        lstVariants.setCellFactory(new Callback<ListView<ImageScriptVariant>, ListCell<ImageScriptVariant>>() {
            @Override
            public ListCell<ImageScriptVariant> call(ListView<ImageScriptVariant> xmlVariantInfoListView) {
                return new ListCell<ImageScriptVariant>() {
                    @Override
                    protected void updateItem(ImageScriptVariant imageScriptVariant, boolean empty) {
                        super.updateItem(imageScriptVariant, empty);

                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(imageScriptVariant.getName());
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }

    private TreeItem<Object> buildTreeItems() {
        final TreeItem<Object> rootItem = new TreeItem<>();

        if (scriptView == ScriptManagerDialogFactory.ScriptView.All ||
                scriptView == ScriptManagerDialogFactory.ScriptView.Effects) {
            final ImageScriptTypeTreeItem effectRootItem = new ImageScriptTypeTreeItem(
                    ImageScriptElement.ScriptType.Effect
            );
            ScriptUIBuilder.buildTreeFromScripts(XmlEffectScriptManager.getInstance().extractElementGroupList(),
                    XmlEffectScriptManager.getInstance().getScriptInfoList(), effectRootItem);
            rootItem.getChildren().add(effectRootItem);
        }

        if (scriptView == ScriptManagerDialogFactory.ScriptView.All ||
                scriptView == ScriptManagerDialogFactory.ScriptView.Renderer) {
            final ImageScriptTypeTreeItem rendererRootItem = new ImageScriptTypeTreeItem(
                    ImageScriptElement.ScriptType.Renderer
            );
            ScriptUIBuilder.buildTreeFromScripts(XmlRendererScriptManager.getInstance().extractElementGroupList(),
                    XmlRendererScriptManager.getInstance().getScriptInfoList(), rendererRootItem);
            rootItem.getChildren().add(rendererRootItem);
        }

        return rootItem;
    }

    public ImageScriptElement getSelectedScriptElement() {
        return selectedScriptElement;
    }

    public ImageScriptVariant getScriptVariant() {
        return scriptVariant;
    }

    private static TreeItem<?> findTreeItem(ImageScriptElement scriptElement, TreeItem<?> currentItem) {
        if (scriptElement == null)
            return null;

        if (currentItem instanceof ImageScriptElementTreeItem) {
            if (scriptElement.getId().equals(((ImageScriptElementTreeItem) currentItem).getScriptElement().getId()))
                return currentItem;
        }

        if (!currentItem.getChildren().isEmpty()) {
            for (TreeItem<?> childItem : currentItem.getChildren()) {
                final TreeItem treeItem = findTreeItem(scriptElement, childItem);
                if (treeItem != null)
                    return treeItem;
            }
        }

        return null;
    }

    private void updatePreviewImage(ImageScriptElement scriptInfo, ImageScriptVariant variantInfo) {
        final WritableImage writableImage = JavaScriptImageWriterTaskRunner.runJavaScriptTask(origImage, null, 1, scriptInfo, variantInfo, null);
        imgPreview.setImage(writableImage);

        if (secondOrigImage != null) {
            final WritableImage secondWritableImage = JavaScriptImageWriterTaskRunner.runJavaScriptTask(secondOrigImage, null, 1, scriptInfo, variantInfo, null);
            imgSecondPreview.setImage(secondWritableImage);
        }
    }

    @FXML
    private void onActionAddVariant(ActionEvent actionEvent) {
        final String input = Dialogs.create().owner(null)
                .title("Create new Variant")
                .message("Enter a new name for the variant")
                .showTextInput("My Variant");

        if (input != null && tvScript.getSelectionModel().getSelectedItem() instanceof ImageScriptElementTreeItem) {
            final ImageScriptElementTreeItem treeItem = (ImageScriptElementTreeItem) tvScript.getSelectionModel().getSelectedItem();
            switch (treeItem.getScriptElement().getScriptType()) {
                case Effect:
                    XmlEffectScriptManager.getInstance().storeVariant(
                            treeItem.getScriptElement().getGroupInfo().getId(),
                            treeItem.getScriptElement().getId(),
                            input, treeItem.getScriptVariant()
                    );
                    break;
                case Renderer:
                    XmlRendererScriptManager.getInstance().storeVariant(
                            treeItem.getScriptElement().getGroupInfo().getId(),
                            treeItem.getScriptElement().getId(),
                            input, treeItem.getScriptVariant()
                    );
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    @FXML
    private void onActionRemoveVariant(ActionEvent actionEvent) {

    }
}
