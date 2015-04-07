package org.pcsoft.tools.image_fx.scripting.types.image;

import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptParameterGroupProperty;
import org.pcsoft.tools.image_fx.scripting.xml.XParameterGroup;

/**
 * Created by Christoph on 21.06.2014.
 */
public final class ImageScriptParameterGroup extends AbstractImageScriptGroup {

    private final ImageScriptParameterGroupProperty parent;
    private final XParameterGroup parameterGroup;

    public ImageScriptParameterGroup(XParameterGroup parameterGroup, ImageScriptParameterGroup parent) {
        super(parameterGroup.getId(), parameterGroup.getName());
        this.parameterGroup = parameterGroup;
        this.parent = new ImageScriptParameterGroupProperty(parent);
    }

    public ImageScriptParameterGroup getParent() {
        return parent.get();
    }

    public ImageScriptParameterGroupProperty parentProperty() {
        return parent;
    }

    public XParameterGroup getParameterGroup() {
        return parameterGroup;
    }

    @Override
    public String toString() {
        return "ParameterGroupInfo{" +
                "parent=" + parent +
                "} " + super.toString();
    }
}
