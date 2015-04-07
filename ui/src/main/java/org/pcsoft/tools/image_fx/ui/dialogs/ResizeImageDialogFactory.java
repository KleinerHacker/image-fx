package org.pcsoft.tools.image_fx.ui.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.image_fx.ui.dialogs.controller.ResizeImageDialogController;
import org.pcsoft.tools.image_fx.ui.utils.DialogActionUtils;

import java.io.IOException;

/**
 * Created by pfeifchr on 21.08.2014.
 */
public final class ResizeImageDialogFactory {

    public static final class Result {
        private final int width, height;
        private final boolean smooth;

        private Result(int width, int height, boolean smooth) {
            this.width = width;
            this.height = height;
            this.smooth = smooth;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public boolean isSmooth() {
            return smooth;
        }
    }

    public static Result show(int width, int height) {
        final Dialog dialog = new Dialog(null, "Resize Image");
        final Action resizeAction = DialogActionUtils.createDialogAction("_Resize", dialog, Dialog.ActionTrait.DEFAULT);
        final Action cancelAction = DialogActionUtils.createDialogAction("_Cancel", dialog, Dialog.ActionTrait.CANCEL);
        final ResizeImageDialogController controller = new ResizeImageDialogController(resizeAction, width, height);

        dialog.setResizable(false);
        dialog.setMasthead("Here you can resize your image");
        dialog.getActions().addAll(resizeAction, cancelAction);

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(aClass -> controller);
            final Pane pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/dialog.image.resize.fxml"));

            dialog.setContent(pane);

            if (dialog.show() == resizeAction) {
                return new Result(controller.getWidth(), controller.getHeight(), controller.isSmooth());
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ResizeImageDialogFactory() {
    }
}
