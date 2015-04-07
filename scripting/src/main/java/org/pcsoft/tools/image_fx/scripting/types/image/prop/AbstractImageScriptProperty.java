package org.pcsoft.tools.image_fx.scripting.types.image.prop;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.StringProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.AbstractImageScript;
import org.pcsoft.tools.image_fx.scripting.types.image.AbstractImageScriptGroup;

/**
 * Created by pfeifchr on 23.07.2014.
 */
public abstract class AbstractImageScriptProperty<P extends AbstractImageScriptGroup, G extends AbstractImageScriptGroupProperty<P>, T extends AbstractImageScript<P, G>> extends ReadOnlyObjectWrapper<T> {

    protected AbstractImageScriptProperty(T t) {
        super(t);
        init(t);
    }

    public final String getId() {
        return get().getId();
    }

    public final G groupInfoProperty() {
        return get().groupInfoProperty();
    }

    public final P getGroupInfo() {
        return get().getGroupInfo();
    }

    public final StringProperty nameProperty() {
        return get().nameProperty();
    }

    public final StringProperty idProperty() {
        return get().idProperty();
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
