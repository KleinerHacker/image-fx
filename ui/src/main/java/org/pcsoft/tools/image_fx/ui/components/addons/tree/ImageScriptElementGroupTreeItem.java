package org.pcsoft.tools.image_fx.ui.components.addons.tree;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElementGroup;

/**
 * Created by Christoph on 22.06.2014.
 */
public class ImageScriptElementGroupTreeItem extends TreeItem<Object> {

    public ImageScriptElementGroupTreeItem(ImageScriptElementGroup group) {
        super(group);
    }

    public ImageScriptElementGroupTreeItem(Node node, ImageScriptElementGroup group) {
        super(group, node);
    }

    public ImageScriptElementGroup getGroup() {
        return (ImageScriptElementGroup) getValue();
    }
}
