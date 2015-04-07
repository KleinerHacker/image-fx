package org.pcsoft.tools.image_fx.ui.windows.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 13.08.2014.
 */
public class MainWindowStatusBarController extends AbstractController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindowStatusBarController.class);

    @FXML
    private Label lblWork;
    @FXML
    private ProgressBar pbWork;

    public MainWindowStatusBarController(Stage parent) {
        super(parent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LOGGER.info("Initialize UI: Main Window Controller (STATUS BAR)");
    }

    void showWorker(String text) {
        pbWork.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        pbWork.setVisible(true);
        lblWork.setText(text);
        lblWork.setVisible(true);
    }

    /**
     * Update the worker text. <b>Worker must visible, call {@link MainWindowStatusBarController#showWorker(String)} first</b>
     * @param text
     */
    void updateWorkerText(String text) {
        lblWork.setText(text);
    }

    /**
     * Update the worker progress. <b>Worker must visible, call {@link MainWindowStatusBarController#showWorker(String)} first</b>
     * @param progress
     */
    void updateWorkerProgress(double progress) {
        pbWork.setProgress(progress);
    }

    void hideWorker() {
        pbWork.setVisible(false);
        lblWork.setVisible(false);
    }

    ProgressBar getWorkerProgress() {
        return pbWork;
    }

    Label getWorkerLabel() {
        return lblWork;
    }
}
