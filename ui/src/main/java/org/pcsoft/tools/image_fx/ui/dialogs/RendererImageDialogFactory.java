package org.pcsoft.tools.image_fx.ui.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.ui.dialogs.controller.RendererImageDialogController;
import org.pcsoft.tools.image_fx.ui.utils.DialogActionUtils;

import java.io.IOException;

/**
 * Created by pfeifchr on 14.08.2014.
 */
public final class RendererImageDialogFactory {

    public static final class Result {
        private final int width, height;
        private final ImageScriptElement scriptElement;
        private final ImageScriptVariant scriptVariant;

        private Result(int width, int height, ImageScriptElement scriptElement, ImageScriptVariant scriptVariant) {
            this.width = width;
            this.height = height;
            this.scriptElement = scriptElement;
            this.scriptVariant = scriptVariant;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public ImageScriptElement getScriptElement() {
            return scriptElement;
        }

        public ImageScriptVariant getScriptVariant() {
            return scriptVariant;
        }
    }

    public static Result show() {
        final Dialog dialog = new Dialog(null, "New Image From Renderer");
        final Action createAction = DialogActionUtils.createDialogAction("C_reate", dialog, Dialog.ActionTrait.DEFAULT);
        final Action cancelAction = DialogActionUtils.createDialogAction("_Cancel", dialog, Dialog.ActionTrait.CANCEL);

        try {
            final FXMLLoader loader = new FXMLLoader();
            final RendererImageDialogController controller = new RendererImageDialogController(createAction);
            loader.setControllerFactory(aClass -> controller);
            final Pane pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/dialog.image.renderer.fxml"));

            dialog.setMasthead("Creates a new image from the given renderer");
            dialog.setResizable(false);
            dialog.setContent(pane);
            dialog.getActions().addAll(createAction, cancelAction);

            if (dialog.show() == createAction) {
                return new Result(controller.getWidth(), controller.getHeight(), controller.getScriptElement(),
                        controller.getScriptVariant());
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private RendererImageDialogFactory() {
    }
}
