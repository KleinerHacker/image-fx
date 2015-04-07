package org.pcsoft.tools.image_fx.ui.components.addons.tree;

import javafx.scene.control.TreeItem;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayer;

/**
 * Created by pfeifchr on 25.07.2014.
 */
public class ImageScriptLayerTreeItem extends TreeItem<Object> {

    public ImageScriptLayerTreeItem(ImageScriptLayer scriptLayer) {
        super(scriptLayer);
    }

    public ImageScriptLayer getScriptLayer() {
        return (ImageScriptLayer) getValue();
    }
}
