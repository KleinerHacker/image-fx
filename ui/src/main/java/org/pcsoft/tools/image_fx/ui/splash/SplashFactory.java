package org.pcsoft.tools.image_fx.ui.splash;

import org.pcsoft.tools.image_fx.common.threads.DaemonThreadFactory;
import org.pcsoft.tools.image_fx.ui.splash.controllers.SplashController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by Christoph on 17.06.2014.
 */
public final class SplashFactory {

    public static abstract class SplashRunner implements Runnable {
        private SplashController controller;

        protected final SplashController getController() {
            return controller;
        }
    }

    public static void show(final SplashRunner task) {
        final Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setTitle("Loading");
        stage.setResizable(false);

        final FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> aClass) {
                final SplashController splashController = new SplashController();
                task.controller = splashController;
                return splashController;
            }
        });
        try {
            final Pane pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/splash.fxml"));
            final Scene scene = new Scene(pane);
            stage.setScene(scene);

            Executors.newCachedThreadPool(new DaemonThreadFactory("startup")).submit(new FutureTask<Void>(task, null) {
                @Override
                protected void done() {
                    super.done();
                    Platform.runLater(stage::close);
                }
            });
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SplashFactory() {
    }
}
