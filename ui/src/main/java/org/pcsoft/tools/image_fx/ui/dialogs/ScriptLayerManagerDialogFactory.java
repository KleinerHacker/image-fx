package org.pcsoft.tools.image_fx.ui.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.AbstractDialogAction;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayer;
import org.pcsoft.tools.image_fx.ui.dialogs.controller.ScriptLayerManagerDialogController;

import java.io.IOException;

/**
 * Created by pfeifchr on 17.07.2014.
 */
public final class ScriptLayerManagerDialogFactory {

    public static final class Result {
        private final ImageScriptLayer scriptLayer;

        private Result(ImageScriptLayer scriptLayer) {
            this.scriptLayer = scriptLayer;
        }

        public ImageScriptLayer getScriptLayer() {
            return scriptLayer;
        }
    }

    public static Result show(Image image, boolean applicable) {
        final Dialog dialog = new Dialog(null, "Script Layer Manager");
        final AbstractDialogAction applyAction = new AbstractDialogAction("_Apply", Dialog.ActionTrait.DEFAULT) {
            @Override
            public void execute(ActionEvent actionEvent) {
                dialog.hide();
            }
        };
        applyAction.setDisabled(true);
        final AbstractDialogAction cancelAction = new AbstractDialogAction("_Cancel", Dialog.ActionTrait.CANCEL) {
            @Override
            public void execute(ActionEvent actionEvent) {
                dialog.hide();
            }
        };

        final ScriptLayerManagerDialogController controller = new ScriptLayerManagerDialogController(image, applicable, applyAction);
        final FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(aClass -> controller);
        try {
            final Pane pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/dialog.script.layer.manager.fxml"));

            dialog.setMasthead("Here you can add, edit or remove existing script groups or apply a selected script group.");
            dialog.setResizable(false);
            dialog.setContent(pane);
            dialog.getActions().addAll(applyAction, cancelAction);
            final Action resultAction = dialog.show();

            if (resultAction == applyAction) {
                return new Result(controller.getSelectedScriptLayer());
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ScriptLayerManagerDialogFactory() {
    }
}
