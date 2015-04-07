package org.pcsoft.tools.image_fx.scripting.types.image;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.pcsoft.tools.image_fx.scripting.xml.XLayerGroup;

/**
 * Created by Christoph on 21.07.2014.
 */
public final class ImageScriptLayerGroup extends AbstractImageScriptGroup {

    private final ObjectProperty<byte[]> graphic;

    public ImageScriptLayerGroup(String id, String name, byte[] graphic) {
        super(id, name);
        this.graphic = new ReadOnlyObjectWrapper<>(graphic);
    }

    public ImageScriptLayerGroup(XLayerGroup layerGroup) {
        super(layerGroup.getId(), layerGroup.getName());
        graphic = new ReadOnlyObjectWrapper<>(layerGroup.getGraphic());
    }

    public byte[] getGraphic() {
        return graphic.get();
    }

    public ObjectProperty<byte[]> graphicProperty() {
        return graphic;
    }

    public boolean hasGraphic() {
        return getGraphic() != null;
    }
}
