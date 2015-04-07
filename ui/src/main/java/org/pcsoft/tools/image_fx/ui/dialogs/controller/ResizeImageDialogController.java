package org.pcsoft.tools.image_fx.ui.dialogs.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.action.Action;
import org.pcsoft.tools.image_fx.ui.utils.EventHandlerUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 21.08.2014.
 */
public class ResizeImageDialogController implements Initializable {

    @FXML
    private TextField txtWidth;
    @FXML
    private TextField txtHeight;
    @FXML
    private CheckBox ckbProportional;
    @FXML
    private CheckBox ckbSmooth;

    private final Action resizeAction;
    private final int origWidth, origHeight;

    public ResizeImageDialogController(Action resizeAction, int origWidth, int origHeight) {
        this.resizeAction = resizeAction;
        this.origWidth = origWidth;
        this.origHeight = origHeight;
    }

    public int getWidth() {
        return Integer.parseInt(txtWidth.getText());
    }

    public int getHeight() {
        return Integer.parseInt(txtHeight.getText());
    }

    public boolean isSmooth() {
        return ckbSmooth.isSelected();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtWidth.setText(String.valueOf(origWidth));
        txtHeight.setText(String.valueOf(origHeight));

        final EventHandler<KeyEvent> inputNumberRestrictionHandler =
                EventHandlerUtils.TextFieldHandlers.createNumericIntegerInputRestrictionHandler(5);
        final EventHandler<InputEvent> validationEmptyHandler = inputEvent -> {
            if (ckbProportional.isSelected()) {
                if (inputEvent.getSource() == txtWidth) {
                    //TODO
                } else if (inputEvent.getSource() == txtHeight) {
                    //TODO
                }
            }
        };

        txtWidth.addEventFilter(KeyEvent.KEY_TYPED, inputNumberRestrictionHandler);
        txtWidth.addEventHandler(InputEvent.ANY, validationEmptyHandler);
        txtHeight.addEventFilter(KeyEvent.KEY_TYPED, inputNumberRestrictionHandler);
        txtHeight.addEventHandler(InputEvent.ANY, validationEmptyHandler);

        resizeAction.disabledProperty().bind(txtWidth.textProperty().isEmpty().or(txtHeight.textProperty().isEmpty()));
    }
}
