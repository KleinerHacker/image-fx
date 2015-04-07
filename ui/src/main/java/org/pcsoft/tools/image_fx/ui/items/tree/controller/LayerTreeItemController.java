package org.pcsoft.tools.image_fx.ui.items.tree.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.pcsoft.tools.image_fx.scripting.threading.JavaScriptImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayer;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 25.07.2014.
 */
public class LayerTreeItemController implements Initializable {

    @FXML
    private Label lblTitle;
    @FXML
    private ImageView imgPreview;
    @FXML
    private FlowPane pnlImages;

    private final Image origImage;
    private final ImageScriptLayer scriptLayer;

    public LayerTreeItemController(Image origImage, ImageScriptLayer scriptLayer) {
        this.origImage = origImage;
        this.scriptLayer = scriptLayer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTitle.setText(scriptLayer.getName());
        imgPreview.setImage(JavaScriptImageWriterTaskRunner.runJavaScriptTask(origImage, null, 1, scriptLayer.getScriptHolderList(), null));
        for (ImageScriptLayer.ImageScriptHolder scriptHolder : scriptLayer.getScriptHolderList()) {
            final ImageView imageView = new ImageView(
                    JavaScriptImageWriterTaskRunner.runJavaScriptTask(
                            origImage, null, 1,
                            scriptHolder.getScriptInfo(),
                            scriptHolder.getVariantInfo(),
                            null
                    )
            );
            imageView.setFitWidth(100);
            imageView.setFitHeight(75);
            Tooltip.install(imageView, new Tooltip(scriptHolder.getScriptInfo().getName()));

            final Label label = new Label(scriptHolder.getScriptInfo().getName());
            label.setMaxWidth(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER);

            VBox.setMargin(label, new Insets(5, 0, 5, 0));
            final VBox panel = new VBox(imageView, label);

            FlowPane.setMargin(panel, new Insets(0, 2.5, 0, 2.5));
            pnlImages.getChildren().add(panel);
        }
    }
}
