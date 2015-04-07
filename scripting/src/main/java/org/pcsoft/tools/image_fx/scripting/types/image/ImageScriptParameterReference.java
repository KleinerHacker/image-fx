package org.pcsoft.tools.image_fx.scripting.types.image;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.pcsoft.tools.image_fx.scripting.xml.XParameterReference;

/**
 * Created by Christoph on 21.06.2014.
 */
public final class ImageScriptParameterReference {

    private final StringProperty reference;
    private final StringProperty originalPlainValue;
    private StringProperty plainValue;
    private final XParameterReference parameterReference;

    public ImageScriptParameterReference(XParameterReference parameterReference) {
        this.parameterReference = parameterReference;
        this.reference = new ReadOnlyStringWrapper(parameterReference.getRef());
        this.plainValue = new SimpleStringProperty(parameterReference.getValue());
        this.originalPlainValue = new ReadOnlyStringWrapper(parameterReference.getValue());
    }

    public String getReference() {
        return reference.get();
    }

    public StringProperty referenceProperty() {
        return reference;
    }

    public String getOriginalPlainValue() {
        return originalPlainValue.get();
    }

    public StringProperty originalPlainValueProperty() {
        return originalPlainValue;
    }

    public String getPlainValue() {
        return plainValue.get();
    }

    public StringProperty plainValueProperty() {
        return plainValue;
    }

    public void setPlainValue(String plainValue) {
        this.plainValue.set(plainValue);
    }

    public XParameterReference getParameterReference() {
        return parameterReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageScriptParameterReference that = (ImageScriptParameterReference) o;

        if (reference.get() != null ? !reference.get().equals(that.reference.get()) : that.reference.get() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return reference.get() != null ? reference.get().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ParameterReference{" +
                "reference='" + reference + '\'' +
                ", plainValue='" + plainValue + '\'' +
                '}';
    }
}
