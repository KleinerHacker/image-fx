package org.pcsoft.tools.image_fx.ui.splash.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * Created by Christoph on 17.06.2014.
 */
public class SplashController {
    @FXML
    private ProgressBar pbCompleteProgress;
    @FXML
    private ProgressBar pbPartProgress;
    @FXML
    private Label lblAction;

    private String action;
    private double completeProgress, partProgress;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
        if (lblAction != null) {
            lblAction.setText(action);
        }
    }

    public double getCompleteProgress() {
        return completeProgress;
    }

    public void setCompleteProgress(double completeProgress) {
        this.completeProgress = completeProgress;
        if (pbCompleteProgress != null) {
            pbCompleteProgress.setProgress(completeProgress);
        }
    }

    public double getPartProgress() {
        return partProgress;
    }

    public void setPartProgress(double partProgress) {
        this.partProgress = partProgress;
        if (pbPartProgress != null) {
            pbPartProgress.setProgress(partProgress);
        }
    }
}
