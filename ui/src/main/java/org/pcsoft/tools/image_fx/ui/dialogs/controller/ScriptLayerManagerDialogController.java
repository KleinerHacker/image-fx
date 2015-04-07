package org.pcsoft.tools.image_fx.ui.dialogs.controller;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import org.controlsfx.dialog.AbstractDialogAction;
import org.pcsoft.tools.image_fx.scripting.XmlScriptLayerManager;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayer;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayerGroup;
import org.pcsoft.tools.image_fx.ui.components.addons.tree.ImageScriptLayerGroupTreeItem;
import org.pcsoft.tools.image_fx.ui.components.addons.tree.ImageScriptLayerTreeItem;
import org.pcsoft.tools.image_fx.ui.dialogs.ScriptLayerBuilderDialogFactory;
import org.pcsoft.tools.image_fx.ui.items.tree.LayerTreeItemFactory;
import org.pcsoft.tools.image_fx.ui.utils.ImageUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by pfeifchr on 17.07.2014.
 */
public class ScriptLayerManagerDialogController implements Initializable {

    @FXML
    private TreeView<Object> tvScriptGroup;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    private final Image origImage;
    private final boolean applicable;
    private final AbstractDialogAction applyAction;

    public ScriptLayerManagerDialogController(Image origImage, boolean applicable, AbstractDialogAction applyAction) {
        this.origImage = ImageUtils.createPreviewThumbnail(origImage);
        this.applicable = applicable;
        this.applyAction = applyAction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!applicable) {
            applyAction.setDisabled(true);
        } else {
            tvScriptGroup.getSelectionModel().selectedItemProperty().addListener(
                    (observableValue, oldValue, newValue) ->
                            applyAction.setDisabled(newValue == null || !(newValue instanceof ImageScriptLayerTreeItem))
            );
        }

        LayerTreeItemFactory.useForTree(origImage, tvScriptGroup);

        final Map<ImageScriptLayerGroup, ImageScriptLayerGroupTreeItem> treeItemMap = new HashMap<>();
        for (ImageScriptLayerGroup group : XmlScriptLayerManager.getInstance().extractGroups()) {
            final ImageScriptLayerGroupTreeItem treeItem = new ImageScriptLayerGroupTreeItem(group);
            treeItemMap.put(group, treeItem);

            tvScriptGroup.setRoot(new TreeItem<>());
            tvScriptGroup.getRoot().getChildren().add(treeItem);
        }

        for (ImageScriptLayer layer : XmlScriptLayerManager.getInstance().getScriptLayerList()) {
            final ImageScriptLayerTreeItem treeItem = new ImageScriptLayerTreeItem(layer);
            treeItemMap.get(layer.getGroupInfo()).getChildren().add(treeItem);
        }

        tvScriptGroup.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Object>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Object>> observableValue, TreeItem<Object> oldItem, TreeItem<Object> newItem) {
                if (newItem == null || !(newItem instanceof ImageScriptLayerTreeItem)) {
                    btnDelete.setDisable(true);
                    btnEdit.setDisable(true);
                } else {
                    final ImageScriptLayerTreeItem scriptLayerTreeItem = (ImageScriptLayerTreeItem) newItem;

                    btnDelete.setDisable(scriptLayerTreeItem.getScriptLayer().isReadOnly());
                    btnEdit.setDisable(scriptLayerTreeItem.getScriptLayer().isReadOnly());
                }
            }
        });
    }

    public ImageScriptLayer getSelectedScriptLayer() {
        return ((ImageScriptLayerTreeItem)tvScriptGroup.getSelectionModel().getSelectedItem()).getScriptLayer();
    }

    @FXML
    private void onActionAdd(ActionEvent actionEvent) {
        final ScriptLayerBuilderDialogFactory.Result result =
                ScriptLayerBuilderDialogFactory.show(origImage);

        if (result != null) {
            XmlScriptLayerManager.getInstance().storeLayer(result.getName(), result.getGroupId(), result.getGroupName(),
                    result.getGroupScriptElementList().stream().map(ObjectPropertyBase::get).collect(Collectors.toList()));
        }
    }

    @FXML
    private void onActionEdit(ActionEvent actionEvent) {

    }

    @FXML
    private void onActionDelete(ActionEvent actionEvent) {

    }
}
