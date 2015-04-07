package org.pcsoft.tools.image_fx.plugins.tooling.plugins.cutter.ui.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.image_fx.plugins.tooling.plugins.cutter.ui.dialogs.controllers.MainDialogController;

import java.io.IOException;

/**
 * Created by pfeifchr on 12.08.2014.
 */
public final class MainDialogFactory {

    public static void show() {
        try {
            final MainDialogController controller = new MainDialogController();

            final FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(aClass -> controller);
            loader.setClassLoader(MainDialogFactory.class.getClassLoader());
            final Pane pane = loader.load(MainDialogFactory.class.getResourceAsStream("/fxml/dialog.main.fxml"));

            final Dialog dialog = new Dialog(null, "Cutter");
            dialog.setMasthead("Here you can cut an image in many parts.");
            dialog.setResizable(false);
            dialog.getActions().add(Dialog.Actions.CLOSE);
            dialog.setContent(pane);

            dialog.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MainDialogFactory() {
    }
}
