package org.pcsoft.tools.image_fx.ui.dialogs.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.action.Action;
import org.pcsoft.tools.image_fx.stamp.XmlStampManager;
import org.pcsoft.tools.image_fx.stamp.types.Stamp;
import org.pcsoft.tools.image_fx.stamp.types.StampGroup;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Christoph on 16.08.2014.
 */
public class MaskStampDialogController implements Initializable {

    @FXML
    private Accordion accMask;
    @FXML
    private ImageView imgSelectedMask;
    @FXML
    private Label lblSelectedMask;

    private final Action applyAction;
    private final ObjectProperty<Stamp> selectedMask = new SimpleObjectProperty<>(null);

    public MaskStampDialogController(Action applyAction) {
        this.applyAction = applyAction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        applyAction.disabledProperty().bind(selectedMask.isNull());

        final Map<StampGroup, List<Stamp>> map = XmlStampManager.getInstance().extractMaskGroupMap();
        for (final StampGroup stampGroup : map.keySet()) {
            final List<Stamp> stampList = map.get(stampGroup);

            final FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL, 5d, 5d);
            for (final Stamp stamp : stampList) {
                final ImageView imageView = new ImageView(stamp.getImage());
                imageView.setFitWidth(75);
                imageView.setFitHeight(56.25);
                imageView.setCursor(Cursor.CLOSED_HAND);
                imageView.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                        selectedMask.set(stamp);
                        imgSelectedMask.setImage(stamp.getImage());
                        imgSelectedMask.setFitWidth(200);
                        imgSelectedMask.setFitHeight(150);
                        lblSelectedMask.setText(stamp.getName());
                    }
                });

                final Label label = new Label(stamp.getName());
                label.setMaxWidth(Double.MAX_VALUE);
                label.setTextAlignment(TextAlignment.CENTER);
                label.setAlignment(Pos.CENTER);

                flowPane.getChildren().add(new VBox(
                        5d,
                        imageView,
                        label
                ));
            }

            accMask.getPanes().add(new TitledPane(stampGroup.getName(), flowPane));
        }
    }

    public Stamp getSelectedMask() {
        return selectedMask.get();
    }
}
