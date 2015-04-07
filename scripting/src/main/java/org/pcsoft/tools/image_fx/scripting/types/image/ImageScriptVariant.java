package org.pcsoft.tools.image_fx.scripting.types.image;

import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableMap;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptParameterReferenceProperty;
import org.pcsoft.tools.image_fx.scripting.xml.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Christoph on 21.06.2014.
 */
public final class ImageScriptVariant {

    private final StringProperty id;
    private final StringProperty name;
    private final XVariant variant;
    private final BooleanProperty $default, readOnly;
    private final ObservableMap<String, ImageScriptParameterReferenceProperty> parameterReferenceMap = new ObservableMapWrapper<>(new HashMap<>());

    /**
     * Copy constructor
     * @param id
     * @param name
     * @param variantInfo
     */
    public ImageScriptVariant(String id, String name, ImageScriptVariant variantInfo) {
        this.id = new ReadOnlyStringWrapper(id);
        this.name = new ReadOnlyStringWrapper(name);
        this.variant = variantInfo.getVariant();//TODO: Wrong content?
        this.$default = new ReadOnlyBooleanWrapper(variantInfo.isDefault());
        this.readOnly = new ReadOnlyBooleanWrapper(false);
        this.parameterReferenceMap.putAll(variantInfo.parameterReferenceMap);
    }

    public ImageScriptVariant(XVariant variant, boolean readOnly) {
        this.variant = variant;
        this.id = new ReadOnlyStringWrapper(variant.getId());
        this.name = new ReadOnlyStringWrapper(variant.getName());
        this.$default = new ReadOnlyBooleanWrapper(variant.isDefault());
        this.readOnly = new ReadOnlyBooleanWrapper(readOnly);

        for (XParameterReference reference : variant.getParameterReference()) {
            parameterReferenceMap.put(reference.getRef(), new ImageScriptParameterReferenceProperty(reference));
        }
    }

    public XVariant getVariant() {
        return variant;
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public boolean isDefault() {
        return $default.get();
    }

    public BooleanProperty defaultProperty() {
        return $default;
    }

    public boolean isReadOnly() {
        return readOnly.get();
    }

    public BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    public ImageScriptParameterReference getParameterReferenceFor(ImageScriptParameter parameterInfo) {
        final ImageScriptParameterReferenceProperty imageScriptParameterReferenceProperty = parameterReferenceMap.get(parameterInfo.getId());
        if (imageScriptParameterReferenceProperty == null)
            return null;

        return imageScriptParameterReferenceProperty.get();
    }

    public ObservableMap<String, ImageScriptParameterReferenceProperty> getParameterReferencePropertyMap() {
        return parameterReferenceMap;
    }

    public Map<String, ImageScriptParameterReference> getParameterReferenceMap() {
        final Map<String, ImageScriptParameterReference> map = new HashMap<>();
        for (String key : parameterReferenceMap.keySet()) {
            map.put(key, parameterReferenceMap.get(key).get());
        }

        return map;
    }

    public boolean containsParameterReferenceFor(ImageScriptParameter parameterInfo) {
        return parameterReferenceMap.containsKey(parameterInfo.getId());
    }

    public void updateParameterReferenceValue(ImageScriptParameter parameterInfo, String value) {
        if (!parameterReferenceMap.containsKey(parameterInfo.getId()))
            throw new IllegalArgumentException("Unknown parameter id: " + parameterInfo.getId());

        parameterReferenceMap.get(parameterInfo.getId()).setPlainValue(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageScriptVariant that = (ImageScriptVariant) o;

        if (id.get() != null ? !id.get().equals(that.id.get()) : that.id.get() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.get() != null ? id.get().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "VariantInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parameterReferenceMap=" + parameterReferenceMap +
                '}';
    }
}
