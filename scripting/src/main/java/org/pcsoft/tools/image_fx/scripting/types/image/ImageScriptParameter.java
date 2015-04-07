package org.pcsoft.tools.image_fx.scripting.types.image;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptParameterGroupProperty;
import org.pcsoft.tools.image_fx.scripting.utils.TypeIdentifierUtils;
import org.pcsoft.tools.image_fx.scripting.xml.XParameterDefinition;

/**
 * Created by Christoph on 21.06.2014.
 */
public final class ImageScriptParameter {

    private final StringProperty id;
    private final StringProperty name;
    private final ObjectProperty<Class> type;
    private final ImageScriptParameterGroupProperty groupInfo;
    private final XParameterDefinition definition;

    public ImageScriptParameter(XParameterDefinition definition, ImageScriptParameterGroup groupInfo) {
        this.definition = definition;
        this.id = new ReadOnlyStringWrapper(definition.getId());
        this.name = new ReadOnlyStringWrapper(definition.getName());
        this.type = new ReadOnlyObjectWrapper<>(TypeIdentifierUtils.identifyTypeBy(definition));
        this.groupInfo = new ImageScriptParameterGroupProperty(groupInfo);
    }

    public XParameterDefinition getDefinition() {
        return definition;
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

    public Class getType() {
        return type.get();
    }

    public ObjectProperty<Class> typeProperty() {
        return type;
    }

    public ImageScriptParameterGroup getGroupInfo() {
        return groupInfo.get();
    }

    public ImageScriptParameterGroupProperty groupInfoProperty() {
        return groupInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageScriptParameter that = (ImageScriptParameter) o;

        if (id.get() != null ? !id.get().equals(that.id.get()) : that.id.get() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.get() != null ? id.get().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ParameterInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", groupInfo=" + groupInfo +
                '}';
    }
}
