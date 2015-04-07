package org.pcsoft.tools.image_fx.ui.building;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.apache.commons.lang.StringUtils;
import org.pcsoft.tools.image_fx.scripting.XmlScriptManager;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptParsingException;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElementGroup;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptParameter;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptParameterGroup;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.scripting.xml.XBooleanParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XColorParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XColorParameterPresentationType;
import org.pcsoft.tools.image_fx.scripting.xml.XDoubleParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XDoubleParameterPresentationType;
import org.pcsoft.tools.image_fx.scripting.xml.XImagePositionParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XImagePositionParameterPresentationType;
import org.pcsoft.tools.image_fx.scripting.xml.XIntegerParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XIntegerParameterPresentationType;
import org.pcsoft.tools.image_fx.scripting.xml.XStringParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XStringParameterPresentationType;
import org.pcsoft.tools.image_fx.scripting.xml.XStringParameterValue;
import org.pcsoft.tools.image_fx.ui.components.addons.tree.ImageScriptElementGroupTreeItem;
import org.pcsoft.tools.image_fx.ui.components.addons.tree.ImageScriptElementTreeItem;
import org.pcsoft.tools.image_fx.ui.components.addons.tree.ImageScriptTypeTreeItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by pfeifchr on 24.06.2014.
 */
public final class ScriptUIBuilder {

    public static interface OnScriptSettingsChangedListener {
        void onChanged(ImageScriptParameter parameterInfo, Object value);
    }

    public static void buildTreeFromScripts(List<ImageScriptElementGroup> groupInfoList, List<ImageScriptElement> scriptInfoList, ImageScriptTypeTreeItem root) {
        final Map<String, ImageScriptElementGroupTreeItem> groupTreeMap = buildGroupTreeFrom(groupInfoList, root);
        for (ImageScriptElement scriptInfo : scriptInfoList) {
            final ImageScriptElementGroupTreeItem groupItem = groupTreeMap.get(scriptInfo.getGroupInfo().getId());
            final ImageScriptElementTreeItem scriptItem = new ImageScriptElementTreeItem(scriptInfo);
            groupItem.getChildren().add(scriptItem);
        }
    }

    private static Map<String, ImageScriptElementGroupTreeItem> buildGroupTreeFrom(List<ImageScriptElementGroup> groupInfoList, ImageScriptTypeTreeItem root) {
        final Map<String, ImageScriptElementGroupTreeItem> groupMap = new HashMap<>();
        for (ImageScriptElementGroup groupInfo : groupInfoList) {
            final ImageScriptElementGroupTreeItem groupItem = new ImageScriptElementGroupTreeItem(groupInfo);
            root.getChildren().add(groupItem);

            groupMap.put(groupInfo.getId(), groupItem);
        }

        return groupMap;
    }

    public static void buildScriptSettingsPage(ImageScriptElement scriptInfo, Accordion accordion, ImageView imgPreview, ListView<ImageScriptVariant> lstVariant, OnScriptSettingsChangedListener listener) throws ImageFXScriptParsingException {
        final Map<ImageScriptParameterGroup, List<ImageScriptParameter>> parameterGroupInfoMap = XmlScriptManager.extractParameterInfoGroupList(scriptInfo);
        final Map<ImageScriptParameterGroup, GridPane> gridPaneMap = buildTitledPanes(scriptInfo, accordion);
        for (final ImageScriptParameterGroup groupInfo : gridPaneMap.keySet()) {
            final List<ImageScriptParameter> parameterInfoList = parameterGroupInfoMap.get(groupInfo);
            final GridPane gridPane = gridPaneMap.get(groupInfo);

            if (parameterInfoList == null)
                continue;

            for (int i = 0; i < parameterInfoList.size(); i++) {
                final ImageScriptParameter parameterInfo = parameterInfoList.get(i);
                gridPane.add(new Label(parameterInfo.getName() + ": "), 0, i);
                buildScriptSettingsInput(scriptInfo, parameterInfo, gridPane, i, imgPreview, lstVariant, listener);
            }
        }
    }

    private static void buildScriptSettingsInput(ImageScriptElement scriptInfo, final ImageScriptParameter parameterInfo, GridPane gridPane,
                                                 int i, ImageView imgPreview, ListView<ImageScriptVariant> lstVariant,
                                                 OnScriptSettingsChangedListener listener) throws ImageFXScriptParsingException {
        final Map<String, Object> objectMap = XmlScriptManager.buildScriptParameterValueMap(
                scriptInfo.getParameterMap(), scriptInfo.getDefaultVariant());
        if (!objectMap.containsKey(parameterInfo.getId()))
            throw new RuntimeException("Parameter value not found: " + parameterInfo.getId());
        final Object parameterValue = objectMap.get(parameterInfo.getId());

        final Node node;
        if (parameterInfo.getDefinition() instanceof XBooleanParameterDefinition) {
            final XBooleanParameterDefinition definition = (XBooleanParameterDefinition) parameterInfo.getDefinition();

            if (definition.getPresentationRadioButton() != null) {
                final RadioButton rbOn = new RadioButton(definition.getPresentationRadioButton().getOnText());
                final RadioButton rbOff = new RadioButton(definition.getPresentationRadioButton().getOffText());
                if ((Boolean) parameterValue) {
                    rbOn.setSelected(true);
                } else {
                    rbOff.setSelected(false);
                }

                final ToggleGroup toggleGroup = new ToggleGroup();
                toggleGroup.getToggles().addAll(rbOn, rbOff);
                toggleGroup.selectedToggleProperty().addListener((observableValue, oldValue, newValue) -> {
                    listener.onChanged(parameterInfo, newValue == rbOn);
                });
                node = new HBox(rbOn, rbOff);

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;

                        final boolean on = Boolean.parseBoolean(newValue.getParameterReferenceFor(parameterInfo).getPlainValue());
                        if (on) {
                            rbOn.setSelected(true);
                        } else {
                            rbOff.setSelected(false);
                        }
                    });
                }
            } else {
                final CheckBox checkBox = new CheckBox();
                checkBox.setSelected((Boolean) parameterValue);
                checkBox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
                    listener.onChanged(parameterInfo, newValue);
                });
                node = checkBox;

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;

                        final boolean on = Boolean.parseBoolean(newValue.getParameterReferenceFor(parameterInfo).getPlainValue());
                        checkBox.setSelected(on);
                    });
                }
            }
        } else if (parameterInfo.getDefinition() instanceof XIntegerParameterDefinition) {
            final XIntegerParameterDefinition definition = (XIntegerParameterDefinition) parameterInfo.getDefinition();

            if (definition.getPresentation() == XIntegerParameterPresentationType.SLIDER) {
                final Slider slider = new Slider(definition.getMin(), definition.getMax(), definition.getMin());
                final Label label = new Label(definition.getMin() + "");

                slider.setValue((Integer) parameterValue);
                label.setText(parameterValue.toString());

                label.textProperty().bind(slider.valueProperty().asString("%.0f"));
                slider.valueProperty().addListener((observableValue, oldNumber, newNumber) -> {
                    listener.onChanged(parameterInfo, newNumber.intValue());
                });

                node = new HBox(slider, label);

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;

                        final int value = Integer.parseInt(newValue.getParameterReferenceFor(parameterInfo).getPlainValue());
                        slider.setValue(value);
                    });
                }
            } else if (definition.getPresentation() == XIntegerParameterPresentationType.TEXT_FIELD) {
                final TextField textField = new TextField(definition.getMin() + "");
                textField.setText(parameterValue.toString());
                textField.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        if (!StringUtils.containsOnly(keyEvent.getCharacter(), "0123456789")) {
                            keyEvent.consume();
                        }
                    }
                });
                textField.setOnAction(actionEvent -> {
                    listener.onChanged(parameterInfo, Integer.parseInt(textField.getText()));
                });
                node = textField;

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;

                        textField.setText(newValue.getParameterReferenceFor(parameterInfo).getPlainValue());
                    });
                }
            } else
                throw new RuntimeException();
        } else if (parameterInfo.getDefinition() instanceof XDoubleParameterDefinition) {
            final XDoubleParameterDefinition definition = (XDoubleParameterDefinition) parameterInfo.getDefinition();

            if (definition.getPresentation() == XDoubleParameterPresentationType.SLIDER) {
                final Slider slider = new Slider(definition.getMin(), definition.getMax(), definition.getMin());
                final Label label = new Label(definition.getMin() + "");

                slider.setValue((Double) parameterValue);
                label.setText(String.format("%.2f", parameterValue));

                label.textProperty().bind(slider.valueProperty().asString("%.2f"));
                slider.valueProperty().addListener((observableValue, oldNumber, newNumber) -> {
                    listener.onChanged(parameterInfo, newNumber.doubleValue());
                });

                node = new HBox(slider, label);

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;

                        final double value = Double.parseDouble(newValue.getParameterReferenceFor(parameterInfo).getPlainValue());
                        slider.setValue(value);
                    });
                }
            } else if (definition.getPresentation() == XDoubleParameterPresentationType.TEXT_FIELD) {
                final TextField textField = new TextField(definition.getMin() + "");
                textField.setText(String.format("%.2f", parameterValue));
                textField.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        if (!StringUtils.containsOnly(keyEvent.getCharacter(), "0123456789")) {
                            keyEvent.consume();
                        }
                    }
                });
                textField.setOnAction(actionEvent -> {
                    listener.onChanged(parameterInfo, Integer.parseInt(textField.getText()));
                });
                node = textField;

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;

                        textField.setText(newValue.getParameterReferenceFor(parameterInfo).getPlainValue());
                    });
                }
            } else
                throw new RuntimeException();
        } else if (parameterInfo.getDefinition() instanceof XStringParameterDefinition) {
            final XStringParameterDefinition definition = (XStringParameterDefinition) parameterInfo.getDefinition();
            final XStringParameterValue pValue = definition.getValue().stream().filter(
                    xStringParameterValue -> xStringParameterValue.getValue().equals(parameterValue)
            ).findFirst().get();

            if (definition.getPresentation() == XStringParameterPresentationType.COMBO_BOX) {
                final ComboBox<XmlStringValue> comboBox = new ComboBox<>(new ObservableListWrapper<>(
                        definition.getValue().stream().map(XmlStringValue::new).collect(Collectors.toList())
                ));
                for (final XmlStringValue value : comboBox.getItems()) {
                    if (value.equals(new XmlStringValue(pValue))) {
                        comboBox.getSelectionModel().select(value);
                        break;
                    }
                }
                comboBox.setConverter(new StringConverter<XmlStringValue>() {

                    @Override
                    public String toString(XmlStringValue xStringParameterValue) {
                        return xStringParameterValue.getValue();
                    }

                    @Override
                    public XmlStringValue fromString(String s) {
                        for (final XStringParameterValue parameterValue : definition.getValue()) {
                            if (parameterValue.getValue().equals(s))
                                return new XmlStringValue(parameterValue);
                        }

                        return null;
                    }
                });
                comboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                    listener.onChanged(parameterInfo, newValue.getValue());
                });

                node = comboBox;

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;

                        for (final XmlStringValue value : comboBox.getItems()) {
                            if (value.getValue().equals(newValue.getParameterReferenceFor(parameterInfo).getPlainValue())) {
                                comboBox.getSelectionModel().select(value);
                                break;
                            }
                        }
                    });
                }
            } else if (definition.getPresentation() == XStringParameterPresentationType.LIST_BOX) {
                final ListView<XmlStringValue> listView = new ListView<>(new ObservableListWrapper<>(
                        definition.getValue().stream().map(XmlStringValue::new).collect(Collectors.toList())
                ));
                for (final XmlStringValue value : listView.getItems()) {
                    if (value.equals(new XmlStringValue(pValue))) {
                        listView.getSelectionModel().select(value);
                        break;
                    }
                }
                listView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                    listener.onChanged(parameterInfo, newValue.getValue());
                });

                node = listView;

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;

                        for (final XmlStringValue value : listView.getItems()) {
                            if (value.getValue().equals(newValue.getParameterReferenceFor(parameterInfo).getPlainValue())) {
                                listView.getSelectionModel().select(value);
                                break;
                            }
                        }
                    });
                }
            } else
                throw new RuntimeException();
        } else if (parameterInfo.getDefinition() instanceof XColorParameterDefinition) {
            final XColorParameterDefinition definition = (XColorParameterDefinition) parameterInfo.getDefinition();

            if (definition.getPresentation() == XColorParameterPresentationType.COMBO_BOX) {
                final ColorPicker colorPicker = new ColorPicker((javafx.scene.paint.Color) parameterValue);
                colorPicker.valueProperty().addListener((observableValue, oldColor, newColor) -> {
                    listener.onChanged(parameterInfo, newColor);
                });

                node = colorPicker;

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;

                        final Color color = Color.web(newValue.getParameterReferenceFor(parameterInfo).getPlainValue());
                        colorPicker.setValue(color);
                    });
                }
            } else if (definition.getPresentation() == XColorParameterPresentationType.BUTTON) {
                final Button button = new Button("Choose color...");
                final Label label = new Label("Color");

                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        //TODO
                    }
                });

                node = new HBox(button, label);

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;
                        //TODO
                    });
                }
            } else if (definition.getPresentation() == XColorParameterPresentationType.COLOR_PICKER) {
                //TODO
                node = new Label("TODO");

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;
                        //TODO
                    });
                }
            } else
                throw new RuntimeException();
        } else if (parameterInfo.getDefinition() instanceof XImagePositionParameterDefinition) {
            final XImagePositionParameterDefinition definition = (XImagePositionParameterDefinition) parameterInfo.getDefinition();

            if (definition.getPresentation() == XImagePositionParameterPresentationType.SLIDER) {
                final Slider sliderX = new Slider(0, 1, 0.5);
                final Label labelX = new Label(("0.5"));

                labelX.textProperty().bind(sliderX.valueProperty().asString("%.2f"));
                sliderX.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                    listener.onChanged(parameterInfo, newValue.doubleValue());
                });

                final Slider sliderY = new Slider(0, 1, 0.5);
                final Label labelY = new Label("0.5");

                labelY.textProperty().bind(sliderY.valueProperty().asString("%.2f"));
                sliderY.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                    listener.onChanged(parameterInfo, newValue);
                });

                HBox.setMargin(sliderX, new Insets(5));
                HBox.setMargin(sliderY, new Insets(5));
                node = new VBox(new HBox(sliderX, labelX), new HBox(sliderY, labelY));

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;

                        final String[] xy = newValue.getParameterReferenceFor(parameterInfo).getPlainValue().split(",");
                        if (xy.length != 2)
                            throw new RuntimeException();

                        sliderX.setValue(Double.parseDouble(xy[0]));
                        sliderY.setValue(Double.parseDouble(xy[1]));
                    });
                }
            } else if (definition.getPresentation() == XImagePositionParameterPresentationType.IMAGE_CLICK) {
                final ImageView imageView = new ImageView();
                imageView.imageProperty().bind(imgPreview.imageProperty());
                imageView.setFitWidth(200);
                imageView.setFitHeight(150);
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        listener.onChanged(parameterInfo, new Point2D(
                                mouseEvent.getX() / imageView.getImage().getWidth(),
                                mouseEvent.getY() / imageView.getImage().getHeight()));
                    }
                });

                node = imageView;

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;

                        final String[] xy = newValue.getParameterReferenceFor(parameterInfo).getPlainValue().split(",");
                        if (xy.length != 2)
                            throw new RuntimeException();

                        listener.onChanged(parameterInfo, new Point2D(
                                Double.parseDouble(xy[0]),
                                Double.parseDouble(xy[1])
                        ));
                    });
                }
            } else if (definition.getPresentation() == XImagePositionParameterPresentationType.TEXT_FIELD) {
                final TextField textFieldX = new TextField("0.5");
                final TextField textFieldY = new TextField("0.5");

                textFieldX.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        if (!StringUtils.containsOnly(keyEvent.getCharacter(), "0123456789.")) {
                            keyEvent.consume();
                        }
                    }
                });
                textFieldY.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        if (!StringUtils.containsOnly(keyEvent.getCharacter(), "0123456789.")) {
                            keyEvent.consume();
                        }
                    }
                });

                node = new HBox(textFieldX, textFieldY);

                if (lstVariant != null) {
                    lstVariant.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue == null) return;

                        final String[] xy = newValue.getParameterReferenceFor(parameterInfo).getPlainValue().split(",");
                        if (xy.length != 2)
                            throw new RuntimeException();

                        textFieldX.setText(xy[0]);
                        textFieldY.setText(xy[1]);
                    });
                }
            } else
                throw new RuntimeException();
        } else
            throw new RuntimeException();

        GridPane.setMargin(node, new Insets(5));
        gridPane.add(node, 1, i);
    }

    private static Map<ImageScriptParameterGroup, GridPane> buildTitledPanes(ImageScriptElement scriptInfo, Accordion accordion) {
        final Map<String, ImageScriptParameterGroup> groupInfoMap =
                XmlScriptManager.extractScriptGroupInfoMap(scriptInfo.getParameterMap().values());

        accordion.getPanes().clear();
        final Map<ImageScriptParameterGroup, GridPane> gridPaneMap = new HashMap<>();
        for (String id : groupInfoMap.keySet()) {
            final ImageScriptParameterGroup groupInfo = groupInfoMap.get(id);

            final GridPane gridPane = new GridPane();
            final TitledPane titledPane = new TitledPane(groupInfo.getName(), gridPane);
            accordion.getPanes().add(titledPane);
            titledPane.setExpanded(true);

            gridPaneMap.put(groupInfo, gridPane);
        }

        final GridPane gridPane = new GridPane();
        final TitledPane titledPane = new TitledPane("Default", gridPane);
        accordion.getPanes().add(titledPane);

        gridPaneMap.put(null, gridPane);

        return gridPaneMap;
    }

    private ScriptUIBuilder() {
    }
}
