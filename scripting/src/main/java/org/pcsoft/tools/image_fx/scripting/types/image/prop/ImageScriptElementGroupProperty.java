package org.pcsoft.tools.image_fx.scripting.types.image.prop;

import javafx.beans.property.ObjectProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElementGroup;
import org.pcsoft.tools.image_fx.scripting.xml.XScriptGroup;

/**
 * Created by pfeifchr on 23.07.2014.
 */
public final class ImageScriptElementGroupProperty extends AbstractImageScriptGroupProperty<ImageScriptElementGroup> {

    public ImageScriptElementGroupProperty(XScriptGroup xScriptGroup) {
        super(new ImageScriptElementGroup(xScriptGroup));
        init(get());
    }

    public ImageScriptElementGroupProperty(ImageScriptElementGroup imageScriptElementGroup) {
        super(imageScriptElementGroup);
        init(imageScriptElementGroup);
    }

    public XScriptGroup getScriptGroup() {
        return get().getScriptGroup();
    }

    public ObjectProperty<byte[]> graphicProperty() {
        return get().graphicProperty();
    }

    public byte[] getGraphic() {
        return get().getGraphic();
    }

    public boolean hasGraphic() {
        return get().hasGraphic();
    }

    private void init(ImageScriptElementGroup imageScriptElementGroup) {
        if (imageScriptElementGroup == null)
            return;

        imageScriptElementGroup.graphicProperty().addListener((observableValue, bytes, bytes2) -> {
            fireValueChangedEvent();
        });
    }

    @Override
    public String toString() {
        return get() == null ? "null" : get().toString();
    }
}
