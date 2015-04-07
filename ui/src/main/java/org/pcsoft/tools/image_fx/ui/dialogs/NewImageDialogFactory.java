package org.pcsoft.tools.image_fx.ui.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.image_fx.ui.dialogs.controller.NewImageDialogController;
import org.pcsoft.tools.image_fx.ui.utils.DialogActionUtils;

import java.io.IOException;

/**
 * Created by pfeifchr on 20.06.2014.
 */
public final class NewImageDialogFactory {

    public static final class Result {
        private final int width, height;
        private final Color backgroundColor;

        private Result(int width, int height, Color backgroundColor) {
            this.width = width;
            this.height = height;
            this.backgroundColor = backgroundColor;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public Color getBackgroundColor() {
            return backgroundColor;
        }
    }

    public static Result show() {
        final Dialog dialog = new Dialog(null, "Create new image");
        final Action createAction = DialogActionUtils.createDialogAction("C_reate", dialog, Dialog.ActionTrait.DEFAULT);
        final Action cancelAction = DialogActionUtils.createDialogAction("_Cancel", dialog, Dialog.ActionTrait.CANCEL);

        final NewImageDialogController controller = new NewImageDialogController(createAction);
        final FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(aClass -> controller);
        try {
            final Pane pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/dialog.image.new.fxml"));

            dialog.setResizable(false);
            dialog.setMasthead("Creates a new image with the given width and height and the given background color.");
            dialog.setContent(pane);
            dialog.getActions().addAll(createAction, cancelAction);
            final Action resultAction = dialog.show();

            if (resultAction == createAction) {
                return new Result(controller.getImageWidth(), controller.getImageHeight(), controller.getBackgroundColor());
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private NewImageDialogFactory() {
    }
}
