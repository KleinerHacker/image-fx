package org.pcsoft.tools.image_fx.ui.windows.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 13.08.2014.
 */
public class MainWindowWorkerController extends AbstractController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindowWorkerController.class);

    @FXML
    private BorderPane pnlWait;
    @FXML
    private ProgressIndicator pbWait;

    public MainWindowWorkerController(Stage parent) {
        super(parent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LOGGER.info("Initialize UI: Main Window Controller (WORKER)");
    }

    void showWorker() {
        pbWait.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

        final FadeTransition fadeTransitionIn = new FadeTransition(new Duration(200), pnlWait);
        fadeTransitionIn.setFromValue(0);
        fadeTransitionIn.setToValue(1);
        fadeTransitionIn.play();
    }

    void hideWorker() {
        final FadeTransition fadeTransitionOut = new FadeTransition(new Duration(200), pnlWait);
        fadeTransitionOut.setFromValue(1);
        fadeTransitionOut.setToValue(0);
        fadeTransitionOut.play();
    }

    void updateWorkerProgress(double progress) {
        pbWait.setProgress(progress);
    }
}
