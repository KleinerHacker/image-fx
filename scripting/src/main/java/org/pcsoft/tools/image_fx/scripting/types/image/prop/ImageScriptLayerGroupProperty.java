package org.pcsoft.tools.image_fx.scripting.types.image.prop;

import javafx.beans.property.ObjectProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayerGroup;
import org.pcsoft.tools.image_fx.scripting.xml.XLayerGroup;

/**
 * Created by pfeifchr on 23.07.2014.
 */
public final class ImageScriptLayerGroupProperty extends AbstractImageScriptGroupProperty<ImageScriptLayerGroup> {

    public ImageScriptLayerGroupProperty(XLayerGroup xLayerGroup) {
        super(new ImageScriptLayerGroup(xLayerGroup));
    }

    public ImageScriptLayerGroupProperty(ImageScriptLayerGroup imageScriptLayerGroup) {
        super(imageScriptLayerGroup);
    }

    public byte[] getGraphic() {
        return get().getGraphic();
    }

    public boolean hasGraphic() {
        return get().hasGraphic();
    }

    public ObjectProperty<byte[]> graphicProperty() {
        return get().graphicProperty();
    }

    @Override
    public String toString() {
        return get() == null ? "null" : get().toString();
    }
}
