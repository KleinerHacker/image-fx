package org.pcsoft.tools.image_fx.scripting.types.image;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;

/**
 * Created by Christoph on 21.06.2014.
 */
public abstract class AbstractImageScriptGroup {

    private final StringProperty id;
    private final StringProperty name;

    protected AbstractImageScriptGroup(String id, String name) {
        this.id = new ReadOnlyStringWrapper(id);
        this.name = new ReadOnlyStringWrapper(name);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractImageScriptGroup that = (AbstractImageScriptGroup) o;

        if (id.get() != null ? !id.get().equals(that.id.get()) : that.id.get() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.get() != null ? id.get().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AbstractImageScriptGroup{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
