package org.pcsoft.tools.image_fx.ui.dialogs.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.dialog.AbstractDialogAction;
import org.pcsoft.tools.image_fx.scripting.XmlScriptLayerManager;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayerGroup;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Christoph on 27.07.2014.
 */
public class ScriptLayerNameDialogController implements Initializable {

    private final AbstractDialogAction okAction;

    @FXML
    private TextField txtName;
    @FXML
    private ComboBox<ImageScriptLayerGroup> cmbGroup;

    public ScriptLayerNameDialogController(AbstractDialogAction okAction) {
        this.okAction = okAction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        okAction.disabledProperty().bind(txtName.textProperty().isEmpty().or(cmbGroup.getEditor().textProperty().isEmpty()));
        cmbGroup.getItems().addAll(XmlScriptLayerManager.getInstance().extractGroups());
        cmbGroup.setCellFactory(new Callback<ListView<ImageScriptLayerGroup>, ListCell<ImageScriptLayerGroup>>() {
            @Override
            public ListCell<ImageScriptLayerGroup> call(ListView<ImageScriptLayerGroup> imageScriptLayerGroupListView) {
                return new ListCell<ImageScriptLayerGroup>() {
                    @Override
                    protected void updateItem(ImageScriptLayerGroup imageScriptLayerGroup, boolean b) {
                        super.updateItem(imageScriptLayerGroup, b);

                        if (imageScriptLayerGroup == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(imageScriptLayerGroup.getName());
                            if (imageScriptLayerGroup.hasGraphic()) {
                                setGraphic(new ImageView(new Image(new ByteArrayInputStream(imageScriptLayerGroup.getGraphic()))));
                            }
                        }
                    }
                };
            }
        });
        cmbGroup.setConverter(new StringConverter<ImageScriptLayerGroup>() {
            @Override
            public String toString(ImageScriptLayerGroup imageScriptLayerGroup) {
                return imageScriptLayerGroup == null ? null : imageScriptLayerGroup.getName();
            }

            @Override
            public ImageScriptLayerGroup fromString(String s) {
                if (s == null || s.isEmpty())
                    return null;

                return XmlScriptLayerManager.getInstance().extractGroups().stream().filter(
                        imageScriptLayerGroup -> imageScriptLayerGroup.getName().equals(s)).findFirst().orElse(null);
            }
        });
    }

    public String getName() {
        return txtName.getText();
    }

    public String getGroupName() {
        return cmbGroup.getSelectionModel().getSelectedItem() == null ?
                cmbGroup.getEditor().getText() : cmbGroup.getSelectionModel().getSelectedItem().getName();
    }

    public String getGroupId() {
        return cmbGroup.getSelectionModel().getSelectedItem() == null ?
                null : cmbGroup.getSelectionModel().getSelectedItem().getId();
    }
}
