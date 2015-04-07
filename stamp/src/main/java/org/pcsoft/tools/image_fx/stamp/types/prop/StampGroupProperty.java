package org.pcsoft.tools.image_fx.stamp.types.prop;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.StringProperty;
import org.pcsoft.tools.image_fx.stamp.types.StampGroup;
import org.pcsoft.tools.image_fx.stamp.xml.XStampGroup;

/**
 * Created by Christoph on 16.08.2014.
 */
public final class StampGroupProperty extends ReadOnlyObjectWrapper<StampGroup> {

    public StampGroupProperty(XStampGroup maskGroup) {
        super(new StampGroup(maskGroup));
        init(get());
    }
    
    public StampGroupProperty(StampGroup stampGroup) {
        super(stampGroup);
        init(stampGroup);
    }

    public String getId() {
        return get().getId();
    }

    public StringProperty idProperty() {
        return get().idProperty();
    }

    public String getName() {
        return get().getName();
    }

    public StringProperty nameProperty() {
        return get().nameProperty();
    }
    
    private void init(StampGroup stampGroup) {
        stampGroup.idProperty().addListener((observableValue, s, s2) -> fireValueChangedEvent());
        stampGroup.nameProperty().addListener((observableValue, s, s2) -> fireValueChangedEvent());
    }

    @Override
    public String toString() {
        return get() == null ? "null" : get().toString();
    }
}
