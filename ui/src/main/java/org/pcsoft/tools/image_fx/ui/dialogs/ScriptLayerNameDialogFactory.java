package org.pcsoft.tools.image_fx.ui.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.AbstractDialogAction;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.image_fx.ui.dialogs.controller.ScriptLayerNameDialogController;

import java.io.IOException;

/**
 * Created by Christoph on 27.07.2014.
 */
public final class ScriptLayerNameDialogFactory {

    public static final class Result {
        private final String name, groupId, groupName;

        private Result(String name, String groupId, String groupName) {
            this.name = name;
            this.groupId = groupId;
            this.groupName = groupName;
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

    public static Result show() {
        final Dialog dialog = new Dialog(null, "Script Layer Name");
        final AbstractDialogAction okAction = new AbstractDialogAction("_OK", Dialog.ActionTrait.DEFAULT) {
            @Override
            public void execute(ActionEvent actionEvent) {
                dialog.hide();
            }
        };
        okAction.setDisabled(true);
        final AbstractDialogAction cancelAction = new AbstractDialogAction("_Cancel", Dialog.ActionTrait.CANCEL) {
            @Override
            public void execute(ActionEvent actionEvent) {
                dialog.hide();
            }
        };

        final ScriptLayerNameDialogController controller = new ScriptLayerNameDialogController(okAction);
        final FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(aClass -> controller);
        try {
            final Pane pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/dialog.script.layer.name.fxml"));

            dialog.setResizable(false);
            dialog.setMasthead("Please enter a new name for the script layer.");
            dialog.getActions().addAll(okAction, cancelAction);
            dialog.setContent(pane);
            final Action resultAction = dialog.show();

            return resultAction == okAction ? new Result(controller.getName(), controller.getGroupId(), controller.getGroupName()) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ScriptLayerNameDialogFactory() {
    }
}
