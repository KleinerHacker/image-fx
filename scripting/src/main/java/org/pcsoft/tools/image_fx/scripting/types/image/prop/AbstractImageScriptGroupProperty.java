package org.pcsoft.tools.image_fx.scripting.types.image.prop;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.StringProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.AbstractImageScriptGroup;

/**
 * Created by pfeifchr on 23.07.2014.
 */
public abstract class AbstractImageScriptGroupProperty<T extends AbstractImageScriptGroup> extends ReadOnlyObjectWrapper<T> {

    protected AbstractImageScriptGroupProperty(T t) {
        super(t);
        init(t);
    }

    public final String getId() {
        return get().getId();
    }

    public final StringProperty idProperty() {
        return get().idProperty();
    }

    public final StringProperty nameProperty() {
        return get().nameProperty();
    }

    public final String getName() {
        return get().getName();
    }

    private void init(T t) {
        if (t == null)
            return;

        t.idProperty().addListener((observableValue, s, s2) -> {
            fireValueChangedEvent();
        });
        t.nameProperty().addListener((observableValue, s, s2) -> {
            fireValueChangedEvent();
        });
    }

    @Override
    public String toString() {
        return get() == null ? "null" : get().toString();
    }
}
