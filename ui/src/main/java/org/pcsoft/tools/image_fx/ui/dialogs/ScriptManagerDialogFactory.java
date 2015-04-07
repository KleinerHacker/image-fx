package org.pcsoft.tools.image_fx.ui.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.AbstractDialogAction;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.ui.dialogs.controller.ScriptManagerDialogController;

import java.io.IOException;

/**
 * Created by Christoph on 22.06.2014.
 */
public final class ScriptManagerDialogFactory {

    public static enum ScriptView {
        All,
        Effects,
        Renderer
    }

    public static final class Result {
        private final ImageScriptElement scriptElement;
        private final ImageScriptVariant scriptVariant;

        private Result(ImageScriptElement scriptElement, ImageScriptVariant scriptVariant) {
            this.scriptElement = scriptElement;
            this.scriptVariant = scriptVariant;
        }

        public ImageScriptElement getScriptElement() {
            return scriptElement;
        }

        public ImageScriptVariant getScriptVariant() {
            return scriptVariant;
        }
    }

    public static Result show(Image image, ImageScriptElement scriptElement, boolean applicable) {
        return show(image, null, scriptElement, applicable, ScriptView.All);
    }

    public static Result show(Image image, ImageScriptElement scriptElement,
                                                 boolean applicable, ScriptView scriptView) {
        return show(image, null, scriptElement, applicable, scriptView);
    }

    public static Result show(Image image, Image secondImage, ImageScriptElement scriptElement,
                                                 boolean applicable) {
        return show(image, secondImage, scriptElement, applicable, ScriptView.All);
    }

    public static Result show(Image image, Image secondImage, ImageScriptElement scriptElement,
                                                 boolean applicable, ScriptView scriptView) {
        final Dialog dialog = new Dialog(null, "Script Manager");
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

        final ScriptManagerDialogController controller = new ScriptManagerDialogController(
                image, secondImage, scriptElement, applicable, scriptView, applyAction);
        final FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(aClass -> controller);
        try {
            final Pane pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/dialog.script.manager.fxml"));

            dialog.setResizable(false);
            dialog.setMasthead("Here you can see all scripts and its settings. You can create new variants and apply it on an open image.");
            dialog.getActions().addAll(applyAction, cancelAction);
            dialog.setContent(pane);
            final Action resultAction = dialog.show();

            return resultAction == applyAction ? new Result(controller.getSelectedScriptElement(), controller.getScriptVariant()) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ScriptManagerDialogFactory() {
    }
}
