package org.pcsoft.tools.image_fx.scripting.types.image;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.AbstractImageScriptGroupProperty;

/**
 * Created by Christoph on 21.07.2014.
 */
public abstract class AbstractImageScript<P extends AbstractImageScriptGroup, T extends AbstractImageScriptGroupProperty<P>> {

    private final StringProperty id;
    private final StringProperty name;
    private final T groupInfo;

    protected AbstractImageScript(String id, String name, T groupInfo) {
        this.id = new ReadOnlyStringWrapper(id);
        this.name = new ReadOnlyStringWrapper(name);
        this.groupInfo = groupInfo;
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

    public P getGroupInfo() {
        return groupInfo.get();
    }

    public T groupInfoProperty() {
        return groupInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractImageScript that = (AbstractImageScript) o;

        if (id.get() != null ? !id.get().equals(that.id.get()) : that.id.get() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.get() != null ? id.get().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AbstractImageScript{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", groupInfo=" + groupInfo +
                '}';
    }
}
