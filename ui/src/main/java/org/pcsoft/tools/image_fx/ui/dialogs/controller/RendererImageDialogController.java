package org.pcsoft.tools.image_fx.ui.dialogs.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.action.Action;
import org.pcsoft.tools.image_fx.scripting.threading.JavaScriptImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.ui.dialogs.ScriptManagerDialogFactory;
import org.pcsoft.tools.image_fx.ui.utils.EventHandlerUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 14.08.2014.
 */
public class RendererImageDialogController implements Initializable {

    @FXML
    private TextField txtWidth;
    @FXML
    private TextField txtHeight;
    @FXML
    private CheckBox ckbProportional;
    @FXML
    private ImageView imgPreview;
    @FXML
    private Label lblRendererName;

    private final Action createAction;
    private final ObjectProperty<ImageScriptElement> scriptElement = new SimpleObjectProperty<>(null);
    private final ObjectProperty<ImageScriptVariant> scriptVariant = new SimpleObjectProperty<>(null);

    public RendererImageDialogController(Action createAction) {
        this.createAction = createAction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final EventHandler<KeyEvent> inputNumberRestrictionHandler =
                EventHandlerUtils.TextFieldHandlers.createNumericIntegerInputRestrictionHandler(5);
        final EventHandler<InputEvent> validationEmptyHandler = new EventHandler<InputEvent>() {
            @Override
            public void handle(InputEvent inputEvent) {
                if (ckbProportional.isSelected()) {
                    if (inputEvent.getSource() == txtWidth) {
                        //TODO
                    } else if (inputEvent.getSource() == txtHeight) {
                        //TODO
                    }
                }
            }
        };

        txtWidth.addEventFilter(KeyEvent.KEY_TYPED, inputNumberRestrictionHandler);
        txtWidth.addEventHandler(InputEvent.ANY, validationEmptyHandler);
        txtHeight.addEventFilter(KeyEvent.KEY_TYPED, inputNumberRestrictionHandler);
        txtHeight.addEventHandler(InputEvent.ANY, validationEmptyHandler);

        createAction.disabledProperty().bind(txtWidth.textProperty().isEmpty().or(txtHeight.textProperty().isEmpty())
                .or(scriptElement.isNull()).or(scriptVariant.isNull()));
    }

    public int getWidth() {
        return Integer.parseInt(txtWidth.getText());
    }

    public int getHeight() {
        return Integer.parseInt(txtHeight.getText());
    }

    public ImageScriptElement getScriptElement() {
        return scriptElement.get();
    }

    public ImageScriptVariant getScriptVariant() {
        return scriptVariant.get();
    }

    @FXML
    private void onActionChooseRenderer(ActionEvent actionEvent) {
        final WritableImage image = new WritableImage(200, 150);
        final ScriptManagerDialogFactory.Result result = ScriptManagerDialogFactory.show(
                image, null, true, ScriptManagerDialogFactory.ScriptView.Renderer);

        if (result != null) {
            scriptElement.set(result.getScriptElement());
            scriptVariant.set(result.getScriptVariant());

            updatePreview();
        }
    }

    private void updatePreview() {
        final WritableImage image = new WritableImage(200, 150);
        imgPreview.setImage(JavaScriptImageWriterTaskRunner.runJavaScriptTask(
                image, null, 1, scriptElement.get(), scriptVariant.get(), null));
    }
}
