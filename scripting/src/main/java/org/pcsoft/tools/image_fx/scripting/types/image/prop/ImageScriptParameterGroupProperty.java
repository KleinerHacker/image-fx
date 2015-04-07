package org.pcsoft.tools.image_fx.scripting.types.image.prop;

import javafx.beans.property.ObjectProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptParameterGroup;
import org.pcsoft.tools.image_fx.scripting.xml.XParameterGroup;

/**
 * Created by pfeifchr on 23.07.2014.
 */
public final class ImageScriptParameterGroupProperty extends AbstractImageScriptGroupProperty<ImageScriptParameterGroup> {

    public ImageScriptParameterGroupProperty(XParameterGroup parameterGroup, ImageScriptParameterGroup parent) {
        super(new ImageScriptParameterGroup(parameterGroup, parent));
        init(get());
    }

    public ImageScriptParameterGroupProperty(ImageScriptParameterGroup imageScriptParameterGroup) {
        super(imageScriptParameterGroup);
        init(imageScriptParameterGroup);
    }

    public ImageScriptParameterGroup getParent() {
        return get().getParent();
    }

    public ObjectProperty<ImageScriptParameterGroup> parentProperty() {
        return get().parentProperty();
    }

    public XParameterGroup getParameterGroup() {
        return get().getParameterGroup();
    }

    private void init(ImageScriptParameterGroup imageScriptParameterGroup) {
        if (imageScriptParameterGroup == null)
            return;

        imageScriptParameterGroup.parentProperty().addListener((observableValue, imageScriptParameterGroup1, imageScriptParameterGroup2) -> {
            fireValueChangedEvent();
        });
    }

    @Override
    public String toString() {
        return get() == null ? "null" : get().toString();
    }
}
