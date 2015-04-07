package org.pcsoft.tools.image_fx.ui.utils;

import javafx.event.ActionEvent;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.AbstractDialogAction;
import org.controlsfx.dialog.Dialog;

/**
 * Created by pfeifchr on 14.08.2014.
 */
public final class DialogActionUtils {

    public static interface DialogExecutor {
        void execute(ActionEvent actionEvent);
    }

    public static Action createDialogAction(String text, Dialog dialog, Dialog.ActionTrait... traits) {
        return createDialogAction(text, (e) -> dialog.hide(), traits);
    }

    public static Action createDialogAction(String text, Dialog.ActionTrait... traits) {
        return createDialogAction(text, (DialogExecutor)null, traits);
    }

    public static Action createDialogAction(String text, DialogExecutor executor, Dialog.ActionTrait... traits) {
        return new AbstractDialogAction(text, traits) {
            @Override
            public void execute(ActionEvent actionEvent) {
                if (executor != null) {
                    executor.execute(actionEvent);
                }
            }
        };
    }

    private DialogActionUtils() {
    }
}
