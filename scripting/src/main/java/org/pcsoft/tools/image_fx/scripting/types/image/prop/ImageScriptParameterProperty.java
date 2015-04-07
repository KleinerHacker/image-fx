package org.pcsoft.tools.image_fx.scripting.types.image.prop;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.StringProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptParameter;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptParameterGroup;
import org.pcsoft.tools.image_fx.scripting.xml.XParameterDefinition;

/**
 * Created by pfeifchr on 23.07.2014.
 */
public final class ImageScriptParameterProperty extends ReadOnlyObjectWrapper<ImageScriptParameter> {

    public ImageScriptParameterProperty(XParameterDefinition definition, ImageScriptParameterGroup groupInfo) {
        super(new ImageScriptParameter(definition, groupInfo));
        init(get());
    }

    public ImageScriptParameterProperty(ImageScriptParameter imageScriptParameter) {
        super(imageScriptParameter);
        init(imageScriptParameter);
    }

    public XParameterDefinition getDefinition() {
        return get().getDefinition();
    }

    public StringProperty idProperty() {
        return get().idProperty();
    }

    public String getId() {
        return get().getId();
    }

    public ObjectProperty<ImageScriptParameterGroup> groupInfoProperty() {
        return get().groupInfoProperty();
    }

    public Class getType() {
        return get().getType();
    }

    public ImageScriptParameterGroup getGroupInfo() {
        return get().getGroupInfo();
    }

    public StringProperty nameProperty() {
        return get().nameProperty();
    }

    public ObjectProperty<Class> typeProperty() {
        return get().typeProperty();
    }

    public String getName() {
        return get().getName();
    }

    private void init(ImageScriptParameter imageScriptParameter) {
        if (imageScriptParameter == null)
            return;

        imageScriptParameter.idProperty().addListener((observableValue, s, s2) -> {
            fireValueChangedEvent();
        });
        imageScriptParameter.nameProperty().addListener((observableValue, s, s2) -> {
            fireValueChangedEvent();
        });
        imageScriptParameter.groupInfoProperty().addListener((observableValue, imageScriptParameterGroup, imageScriptParameterGroup2) -> {
            fireValueChangedEvent();
        });
        imageScriptParameter.groupInfoProperty().addListener(observable -> {
            fireValueChangedEvent();
        });
        imageScriptParameter.typeProperty().addListener((observableValue, aClass, aClass2) -> {
            fireValueChangedEvent();
        });
    }

    @Override
    public String toString() {
        return get() == null ? "null" : get().toString();
    }
}
