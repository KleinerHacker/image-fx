package org.pcsoft.tools.image_fx.stamp.types;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import org.pcsoft.tools.image_fx.stamp.xml.XStampGroup;

import java.util.Objects;

/**
 * Created by Christoph on 16.08.2014.
 */
public final class StampGroup {

    private final StringProperty id, name;
    private final XStampGroup maskGroup;

    public StampGroup(XStampGroup maskGroup) {
        this.maskGroup = maskGroup;
        this.id = new ReadOnlyStringWrapper(maskGroup.getId());
        this.name = new ReadOnlyStringWrapper(maskGroup.getName());
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
    public int hashCode() {
        return Objects.hash(id.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final StampGroup other = (StampGroup) obj;
        return Objects.equals(this.id.get(), other.id.get());
    }

    @Override
    public String toString() {
        return "MaskGroup{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
