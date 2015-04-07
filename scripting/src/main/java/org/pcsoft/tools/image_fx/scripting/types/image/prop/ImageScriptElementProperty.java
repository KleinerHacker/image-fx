package org.pcsoft.tools.image_fx.scripting.types.image.prop;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptException;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElementGroup;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptParameter;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.scripting.xml.*;

import java.util.Map;

/**
 * Created by pfeifchr on 23.07.2014.
 */
public final class ImageScriptElementProperty extends AbstractImageScriptProperty<ImageScriptElementGroup, ImageScriptElementGroupProperty, ImageScriptElement> {

    public ImageScriptElementProperty(XScript scriptElement, XScriptGroup scriptGroup, ImageScriptElement.ScriptType scriptType, String path, boolean inJar) throws ImageFXScriptException {
        super(new ImageScriptElement(scriptElement, scriptGroup, scriptType, path, inJar));
        init(get());
    }
    
    public ImageScriptElementProperty(ImageScriptElement imageScriptElement) {
        super(imageScriptElement);
        init(imageScriptElement);
    }

    public XScript getScriptElement() {
        return get().getScriptElement();
    }

    public ImageScriptVariant getDefaultVariant() {
        return get().getDefaultVariant();
    }

    public ImageScriptVariantProperty defaultVariantProperty() {
        return get().defaultVariantProperty();
    }

    public byte[] getGraphic() {
        return get().getGraphic();
    }

    public StringProperty scriptFragmentProperty() {
        return get().scriptFragmentProperty();
    }

    public ObservableMap<String, ImageScriptVariantProperty> getVariantPropertyMap() {
        return get().getVariantPropertyMap();
    }

    public Map<String, ImageScriptVariant> getVariantMap() {
        return get().getVariantMap();
    }

    public Map<String, ImageScriptParameter> getParameterMap() {
        return get().getParameterMap();
    }

    public ObservableMap<String, ImageScriptParameterProperty> getParameterPropertyMap() {
        return get().getParameterPropertyMap();
    }

    public boolean hasGraphic() {
        return get().hasGraphic();
    }

    public ObjectProperty<byte[]> graphicProperty() {
        return get().graphicProperty();
    }

    public ImageScriptElement.ScriptType getScriptType() {
        return get().getScriptType();
    }

    public ObjectProperty<ImageScriptElement.ScriptType> scriptTypeProperty() {
        return get().scriptTypeProperty();
    }

    public String getScriptFragment() {
        return get().getScriptFragment();
    }
    
    private void init(ImageScriptElement imageScriptElement) {
        if (imageScriptElement == null)
            return;

        imageScriptElement.defaultVariantProperty().addListener((observableValue, imageScriptVariant, imageScriptVariant2) -> {
            fireValueChangedEvent();
        });
        imageScriptElement.defaultVariantProperty().addListener(observable -> {
            fireValueChangedEvent();
        });
        imageScriptElement.graphicProperty().addListener((observableValue, bytes, bytes2) -> {
            fireValueChangedEvent();
        });
        imageScriptElement.scriptFragmentProperty().addListener((observableValue, s, s2) -> {
            fireValueChangedEvent();
        });
        imageScriptElement.scriptTypeProperty().addListener((observableValue, scriptType, scriptType2) -> {
            fireValueChangedEvent();
        });
        imageScriptElement.getParameterPropertyMap().addListener((MapChangeListener<String, ImageScriptParameterProperty>) change -> {
            fireValueChangedEvent();
        });
        imageScriptElement.getParameterPropertyMap().addListener((Observable observable) -> {
            fireValueChangedEvent();
        });
        imageScriptElement.getVariantPropertyMap().addListener((MapChangeListener<String, ImageScriptVariantProperty>) change -> {
            fireValueChangedEvent();
        });
        imageScriptElement.getVariantPropertyMap().addListener((Observable observable) -> {
            fireValueChangedEvent();
        });
    }

    @Override
    public String toString() {
        return get() == null ? "null" : get().toString();
    }
}
