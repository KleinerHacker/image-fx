package org.pcsoft.tools.image_fx.scripting.types.image.prop;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.StringProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptParameter;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptParameterReference;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.scripting.xml.XVariant;

import java.util.Map;

/**
 * Created by pfeifchr on 23.07.2014.
 */
public final class ImageScriptVariantProperty extends ReadOnlyObjectWrapper<ImageScriptVariant> {

    public ImageScriptVariantProperty(String id, String name, ImageScriptVariant variantInfo) {
        super(new ImageScriptVariant(id, name, variantInfo));
        init(get());
    }

    public ImageScriptVariantProperty(XVariant variant, boolean readOnly) {
        super(new ImageScriptVariant(variant, readOnly));
        init(get());
    }

    public ImageScriptVariantProperty(ImageScriptVariant imageScriptVariant) {
        super(imageScriptVariant);
        init(imageScriptVariant);
    }

    public XVariant getVariant() {
        return get().getVariant();
    }

    public boolean isReadOnly() {
        return get().isReadOnly();
    }

    public StringProperty nameProperty() {
        return get().nameProperty();
    }

    public void updateParameterReferenceValue(ImageScriptParameter parameterInfo, String value) {
        get().updateParameterReferenceValue(parameterInfo, value);
    }

    public String getId() {
        return get().getId();
    }

    public StringProperty idProperty() {
        return get().idProperty();
    }

    public ImageScriptParameterReference getParameterReferenceFor(ImageScriptParameter parameterInfo) {
        return get().getParameterReferenceFor(parameterInfo);
    }

    public BooleanProperty defaultProperty() {
        return get().defaultProperty();
    }

    public boolean containsParameterReferenceFor(ImageScriptParameter parameterInfo) {
        return get().containsParameterReferenceFor(parameterInfo);
    }

    public Map<String, ImageScriptParameterReference> getParameterReferenceMap() {
        return get().getParameterReferenceMap();
    }

    public ObservableMap<String, ImageScriptParameterReferenceProperty> getParameterReferencePropertyMap() {
        return get().getParameterReferencePropertyMap();
    }

    public boolean isDefault() {
        return get().isDefault();
    }

    public BooleanProperty readOnlyProperty() {
        return get().readOnlyProperty();
    }

    public String getName() {
        return get().getName();
    }

    private void init(ImageScriptVariant imageScriptVariant) {
        if (imageScriptVariant == null)
            return;

        imageScriptVariant.idProperty().addListener((observableValue, s, s2) -> {
            fireValueChangedEvent();
        });
        imageScriptVariant.nameProperty().addListener((observableValue, s, s2) -> {
            fireValueChangedEvent();
        });
        imageScriptVariant.defaultProperty().addListener((observableValue, aBoolean, aBoolean2) -> {
            fireValueChangedEvent();
        });
        imageScriptVariant.readOnlyProperty().addListener((observableValue, aBoolean, aBoolean2) -> {
            fireValueChangedEvent();
        });
        imageScriptVariant.getParameterReferencePropertyMap().addListener((Observable observable) -> {
            fireValueChangedEvent();
        });
        imageScriptVariant.getParameterReferencePropertyMap().addListener((MapChangeListener<String, ImageScriptParameterReferenceProperty>) change -> {
            fireValueChangedEvent();
        });
    }

    @Override
    public String toString() {
        return get() == null ? "null" : get().toString();
    }
}
