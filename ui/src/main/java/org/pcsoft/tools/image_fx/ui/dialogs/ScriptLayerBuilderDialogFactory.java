package org.pcsoft.tools.image_fx.ui.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.AbstractDialogAction;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptLayerProperty;
import org.pcsoft.tools.image_fx.ui.dialogs.controller.ScriptLayerBuilderDialogController;

import java.io.IOException;
import java.util.List;

/**
 * Created by Christoph on 22.06.2014.
 */
public final class ScriptLayerBuilderDialogFactory {

    public static final class Result {
        private final List<ImageScriptLayerProperty.ImageScriptHolderProperty> groupScriptElementList;
        private final String name, groupId, groupName;

        public Result(String name, String groupId, String groupName,
                      List<ImageScriptLayerProperty.ImageScriptHolderProperty> groupScriptElementList) {
            this.groupScriptElementList = groupScriptElementList;
            this.name = name;
            this.groupId = groupId;
            this.groupName = groupName;
        }

        public List<ImageScriptLayerProperty.ImageScriptHolderProperty> getGroupScriptElementList() {
            return groupScriptElementList;
        }

        public String getName() {
            return name;
        }

        public String getGroupId() {
            return groupId;
        }

        public String getGroupName() {
            return groupName;
        }
    }

    public static Result show(Image image) {
        final Dialog dialog = new Dialog(null, "Script Layer Builder");
        final AbstractDialogAction okAction = new AbstractDialogAction("_OK", Dialog.ActionTrait.DEFAULT) {
            @Override
            public void execute(ActionEvent actionEvent) {
                dialog.hide();
            }
        };
        final AbstractDialogAction cancelAction = new AbstractDialogAction("_Cancel", Dialog.ActionTrait.CANCEL) {
            @Override
            public void execute(ActionEvent actionEvent) {
                dialog.hide();
            }
        };

        final ScriptLayerBuilderDialogController controller = new ScriptLayerBuilderDialogController(image, okAction);
        final FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(aClass -> controller);
        try {
            final Pane pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/dialog.script.layer.builder.fxml"));

            dialog.setMasthead("Here you can create new script groups with help of other scripts and its variants or custom settings.");
            dialog.setResizable(false);
            dialog.setContent(pane);
            dialog.getActions().addAll(okAction, cancelAction);
            final Action resultAction = dialog.show();

            if (resultAction != okAction)
                return null;

            final ScriptLayerNameDialogFactory.Result dialogResult = ScriptLayerNameDialogFactory.show();
            if (dialogResult == null)
                return null;

            return new Result(dialogResult.getName(), dialogResult.getGroupId(), dialogResult.getGroupName(), controller.getLayerScriptElementList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ScriptLayerBuilderDialogFactory() {
    }
}
