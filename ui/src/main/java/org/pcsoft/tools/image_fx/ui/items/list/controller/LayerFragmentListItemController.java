package org.pcsoft.tools.image_fx.ui.items.list.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pcsoft.tools.image_fx.core.threading.ImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.scripting.threading.JavaScriptImageWriterTask;
import org.pcsoft.tools.image_fx.scripting.threading.JavaScriptImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Christoph on 05.07.2014.
 */
public class LayerFragmentListItemController implements Initializable {

    @FXML
    private ImageView imgPreview;
    @FXML
    private Label lblName;
    @FXML
    private Label lblType;

    private final Image image;
    private final ImageScriptElement scriptElement;
    private final ImageScriptVariant scriptVariant;

    public LayerFragmentListItemController(Image image, ImageScriptElement scriptElement, ImageScriptVariant scriptVariant) {
        this.image = image;
        this.scriptElement = scriptElement;
        this.scriptVariant = scriptVariant;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblName.setText(scriptElement.getName());
        lblType.setText(scriptElement.getScriptType().name());
        imgPreview.setImage(
                JavaScriptImageWriterTaskRunner.runJavaScriptTask(image, null, 1, scriptElement, scriptVariant, null)
        );
    }
}
