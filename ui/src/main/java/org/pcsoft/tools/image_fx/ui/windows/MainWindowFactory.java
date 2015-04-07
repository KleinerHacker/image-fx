package org.pcsoft.tools.image_fx.ui.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Christoph on 17.06.2014.
 */
public final class MainWindowFactory {

    public static final String TITLE = "Image FX";
    public static final String TITLE_FORMAT = TITLE + " - [%s]";

    public static void show() {
        final Stage stage = new Stage();
        stage.setTitle(TITLE);

        final FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(aClass -> {
            try {
                return aClass.getConstructor(Stage.class).newInstance(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("fxml/window.main.fxml"));
        try {
            final Pane pane = loader.load();
            final Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.getIcons().add(new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/icon.png")));

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MainWindowFactory() {
    }
}
