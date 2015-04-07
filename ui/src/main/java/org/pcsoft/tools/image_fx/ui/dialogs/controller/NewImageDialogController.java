package org.pcsoft.tools.image_fx.ui.dialogs.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.controlsfx.control.action.Action;
import org.pcsoft.tools.image_fx.ui.utils.EventHandlerUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 20.06.2014.
 */
public class NewImageDialogController implements Initializable {

    @FXML
    private TextField txtImageWidth;
    @FXML
    private TextField txtImageHeight;
    @FXML
    private ColorPicker cmbBackgroundColor;
    @FXML
    private CheckBox ckbProportional;

    private final Action createAction;

    public NewImageDialogController(Action createAction) {
        this.createAction = createAction;
    }

    public int getImageWidth() {
        return Integer.parseInt(txtImageWidth.getText());
    }

    public int getImageHeight() {
        return Integer.parseInt(txtImageHeight.getText());
    }

    public Color getBackgroundColor() {
        return cmbBackgroundColor.getValue();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final EventHandler<KeyEvent> inputNumberRestrictionHandler =
                EventHandlerUtils.TextFieldHandlers.createNumericIntegerInputRestrictionHandler(5);
        final EventHandler<InputEvent> validationEmptyHandler = inputEvent -> {
            if (ckbProportional.isSelected()) {
                if (inputEvent.getSource() == txtImageWidth) {
                    //TODO
                } else if (inputEvent.getSource() == txtImageHeight) {
                    //TODO
                }
            }
        };

        txtImageWidth.addEventFilter(KeyEvent.KEY_TYPED, inputNumberRestrictionHandler);
        txtImageWidth.addEventHandler(InputEvent.ANY, validationEmptyHandler);
        txtImageHeight.addEventFilter(KeyEvent.KEY_TYPED, inputNumberRestrictionHandler);
        txtImageHeight.addEventHandler(InputEvent.ANY, validationEmptyHandler);

        createAction.disabledProperty().bind(txtImageWidth.textProperty().isEmpty().or(txtImageHeight.textProperty().isEmpty()));
    }
}
