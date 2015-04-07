package org.pcsoft.tools.image_fx.scripting.types.image;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.pcsoft.tools.image_fx.scripting.xml.XScriptGroup;

/**
 * Created by Christoph on 21.06.2014.
 */
public final class ImageScriptElementGroup extends AbstractImageScriptGroup {

    private final XScriptGroup scriptGroup;
    private final ObjectProperty<byte[]> graphic;

    public ImageScriptElementGroup(XScriptGroup scriptGroup) {
        super(scriptGroup.getId(), scriptGroup.getName());
        this.scriptGroup = scriptGroup;
        this.graphic = new ReadOnlyObjectWrapper<>(scriptGroup.getGraphic());
    }

    public XScriptGroup getScriptGroup() {
        return scriptGroup;
    }

    public boolean hasGraphic() {
        return graphic.get() != null;
    }

    public byte[] getGraphic() {
        return graphic.get();
    }

    public ObjectProperty<byte[]> graphicProperty() {
        return graphic;
    }
}
