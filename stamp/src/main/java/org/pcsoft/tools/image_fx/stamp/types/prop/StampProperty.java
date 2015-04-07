package org.pcsoft.tools.image_fx.stamp.types.prop;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import org.pcsoft.tools.image_fx.stamp.types.Stamp;
import org.pcsoft.tools.image_fx.stamp.types.StampGroup;
import org.pcsoft.tools.image_fx.stamp.xml.XStamp;

/**
 * Created by Christoph on 16.08.2014.
 */
public final class StampProperty extends ReadOnlyObjectWrapper<Stamp> {

    public StampProperty(XStamp mask, StampGroup stampGroup) {
        super(new Stamp(mask, stampGroup));
        init(get());
    }

    public StampProperty(Stamp stamp) {
        super(stamp);
        init(stamp);
    }

    public String getId() {
        return get().getId();
    }

    public StringProperty idProperty() {
        return get().idProperty();
    }

    public StampGroupProperty maskGroupProperty() {
        return get().maskGroupProperty();
    }

    public StampGroup getMaskGroup() {
        return get().getMaskGroup();
    }

    public Image getImage() {
        return get().getImage();
    }

    public ObjectProperty<Image> imageProperty() {
        return get().imageProperty();
    }

    public StringProperty nameProperty() {
        return get().nameProperty();
    }

    public String getName() {
        return get().getName();
    }

    private void init(Stamp stamp) {
        stamp.idProperty().addListener((observableValue, s, s2) -> fireValueChangedEvent());
        stamp.nameProperty().addListener((observableValue, s, s2) -> fireValueChangedEvent());
        stamp.imageProperty().addListener((observableValue, bytes, bytes2) -> fireValueChangedEvent());
        stamp.maskGroupProperty().addListener((observableValue, maskGroup, maskGroup2) -> fireValueChangedEvent());
        stamp.maskGroupProperty().addListener(observable -> fireValueChangedEvent());
    }

    @Override
    public String toString() {
        return get() == null ? "null" : get().toString();
    }
}
