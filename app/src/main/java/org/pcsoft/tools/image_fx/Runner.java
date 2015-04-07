package org.pcsoft.tools.image_fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.apache.commons.lang.SystemUtils;
import org.pcsoft.tools.image_fx.common.listeners.ProgressListener;
import org.pcsoft.tools.image_fx.scripting.XmlEffectScriptManager;
import org.pcsoft.tools.image_fx.scripting.XmlRendererScriptManager;
import org.pcsoft.tools.image_fx.scripting.XmlScriptLayerManager;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptException;
import org.pcsoft.tools.image_fx.stamp.XmlStampManager;
import org.pcsoft.tools.image_fx.ui.splash.SplashFactory;
import org.pcsoft.tools.image_fx.ui.windows.MainWindowFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Christoph on 17.06.2014.
 */
public class Runner extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        System.out.println("Image FX\t2014");
        System.out.println(">>> Running with Java " + SystemUtils.JAVA_VERSION);
        System.out.println();

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.info("Startup application...");

        SplashFactory.show(new SplashFactory.SplashRunner() {
            @Override
            public void run() {
                LOGGER.info("> Initialize application");

                try {
                    LOGGER.debug(">>> Loading effects...");
                    Platform.runLater(() -> {
                        getController().setAction("Loading effects...");
                        getController().setPartProgress(ProgressBar.INDETERMINATE_PROGRESS);
                    });
                    XmlEffectScriptManager.getInstance().loadElements(new ProgressListener() {
                        @Override
                        public void onStartProgress() {

                        }

                        @Override
                        public void onProgressUpdate(double progress) {
                            Platform.runLater(() -> getController().setPartProgress(progress));
                        }

                        @Override
                        public void onFinishProgress() {

                        }
                    });

                    LOGGER.debug(">>> Loading renderer...");
                    Platform.runLater(() -> {
                        getController().setCompleteProgress(0.25);
                        getController().setPartProgress(ProgressBar.INDETERMINATE_PROGRESS);
                        getController().setAction("Loading renderer...");
                    });
                    XmlRendererScriptManager.getInstance().loadElements(new ProgressListener() {
                        @Override
                        public void onStartProgress() {

                        }

                        @Override
                        public void onProgressUpdate(double progress) {
                            Platform.runLater(() -> getController().setPartProgress(progress));
                        }

                        @Override
                        public void onFinishProgress() {

                        }
                    });

                    LOGGER.debug(">>> Loading layers...");
                    Platform.runLater(() -> {
                        getController().setCompleteProgress(0.50);
                        getController().setPartProgress(ProgressBar.INDETERMINATE_PROGRESS);
                        getController().setAction("Loading layers...");
                    });
                    XmlScriptLayerManager.getInstance().loadElements();

                    LOGGER.debug(">>> Loading stamps...");
                    Platform.runLater(() -> {
                        getController().setCompleteProgress(0.75);
                        getController().setPartProgress(ProgressBar.INDETERMINATE_PROGRESS);
                        getController().setAction("Loading stamps...");
                    });
                    XmlStampManager.getInstance().loadElements(new ProgressListener() {
                        @Override
                        public void onStartProgress() {

                        }

                        @Override
                        public void onProgressUpdate(double progress) {
                            Platform.runLater(() -> getController().setPartProgress(progress));
                        }

                        @Override
                        public void onFinishProgress() {

                        }
                    });

                    Platform.runLater(MainWindowFactory::show);

                    LOGGER.info("> Application was initialized successfully!");
                } catch (ImageFXScriptException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
