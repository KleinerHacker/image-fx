package org.pcsoft.tools.image_fx.ui.items.tree.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pcsoft.tools.image_fx.scripting.threading.JavaScriptImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 25.07.2014.
 */
public class ScriptTreeItemController implements Initializable {

    @FXML
    private ImageView imgPreview;
    @FXML
    private Label lblTitle;

    private final Image origImage;
    private final ImageScriptElement scriptElement;
    private final ImageScriptVariant scriptVariant;

    public ScriptTreeItemController(Image origImage, ImageScriptElement scriptElement, ImageScriptVariant scriptVariant) {
        this.origImage = origImage;
        this.scriptElement = scriptElement;
        this.scriptVariant = scriptVariant;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTitle.setText(scriptElement.getName());
        imgPreview.setImage(
                JavaScriptImageWriterTaskRunner.runJavaScriptTask(origImage, null, 1, scriptElement, scriptVariant, null)
        );
    }
}
