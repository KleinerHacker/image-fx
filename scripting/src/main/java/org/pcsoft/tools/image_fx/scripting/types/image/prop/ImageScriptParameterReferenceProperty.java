package org.pcsoft.tools.image_fx.scripting.types.image.prop;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.StringProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptParameterReference;
import org.pcsoft.tools.image_fx.scripting.xml.XParameterReference;

/**
 * Created by pfeifchr on 23.07.2014.
 */
public final class ImageScriptParameterReferenceProperty extends ReadOnlyObjectWrapper<ImageScriptParameterReference> {

    public ImageScriptParameterReferenceProperty(XParameterReference parameterReference) {
        super(new ImageScriptParameterReference(parameterReference));
        init(get());
    }

    public ImageScriptParameterReferenceProperty(ImageScriptParameterReference imageScriptParameterReference) {
        super(imageScriptParameterReference);
        init(imageScriptParameterReference);
    }

    public String getReference() {
        return get().getReference();
    }

    public String getOriginalPlainValue() {
        return get().getOriginalPlainValue();
    }

    public XParameterReference getParameterReference() {
        return get().getParameterReference();
    }

    public StringProperty referenceProperty() {
        return get().referenceProperty();
    }

    public String getPlainValue() {
        return get().getPlainValue();
    }

    public StringProperty originalPlainValueProperty() {
        return get().originalPlainValueProperty();
    }

    public void setPlainValue(String plainValue) {
        get().setPlainValue(plainValue);
    }

    public StringProperty plainValueProperty() {
        return get().plainValueProperty();
    }

    private void init(ImageScriptParameterReference imageScriptParameterReference) {
        if (imageScriptParameterReference == null)
            return;
        
        imageScriptParameterReference.originalPlainValueProperty().addListener((observableValue, s, s2) -> {
            fireValueChangedEvent();
        });
        imageScriptParameterReference.plainValueProperty().addListener((observableValue, s, s2) -> {
            fireValueChangedEvent();
        });
        imageScriptParameterReference.referenceProperty().addListener((observableValue, s, s2) -> {
            fireValueChangedEvent();
        });
    }

    @Override
    public String toString() {
        return get() == null ? "null" : get().toString();
    }
}
