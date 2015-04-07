package org.pcsoft.tools.image_fx.ui.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.image_fx.ui.dialogs.controller.MaskStampDialogController;
import org.pcsoft.tools.image_fx.ui.utils.DialogActionUtils;

import java.io.IOException;

/**
 * Created by Christoph on 16.08.2014.
 */
public final class MaskStampDialogFactory {

    public static final class Result {
        private final Image image;

        private Result(Image image) {
            this.image = image;
        }

        public Image getImage() {
            return image;
        }
    }

    public static Result show() {
        final Dialog dialog = new Dialog(null, "Mask Stamps");
        final Action applyAction = DialogActionUtils.createDialogAction("_Apply", dialog, Dialog.ActionTrait.DEFAULT);
        final Action cancelAction = DialogActionUtils.createDialogAction("_Cancel", dialog, Dialog.ActionTrait.CANCEL);
        final MaskStampDialogController controller = new MaskStampDialogController(applyAction);

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(aClass -> controller);
            final Pane pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/dialog.mask.stamp.fxml"));

            dialog.setMasthead("Here you can choose a stamp to use as mask (stretch to full image size)");
            dialog.setResizable(false);
            dialog.setContent(pane);
            dialog.getActions().addAll(applyAction, cancelAction);

            if (dialog.show() == applyAction)
                return new Result(controller.getSelectedMask().getImage());

            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MaskStampDialogFactory() {
    }
}
