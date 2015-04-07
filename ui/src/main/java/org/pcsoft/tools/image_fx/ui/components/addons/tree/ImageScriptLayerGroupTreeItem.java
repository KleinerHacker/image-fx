package org.pcsoft.tools.image_fx.ui.components.addons.tree;

import javafx.scene.control.TreeItem;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayerGroup;

/**
 * Created by pfeifchr on 25.07.2014.
 */
public class ImageScriptLayerGroupTreeItem extends TreeItem<Object> {

    public ImageScriptLayerGroupTreeItem(ImageScriptLayerGroup group) {
        super(group);
    }

    public ImageScriptLayerGroup getGroup() {
        return (ImageScriptLayerGroup) getValue();
    }
}
