package org.pcsoft.tools.image_fx.ui.components.addons.tree;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;

/**
 * Created by Christoph on 22.06.2014.
 */
public class ImageScriptTypeTreeItem extends TreeItem<Object> {

    private static Node buildGraphic(ImageScriptElement.ScriptType scriptType) {
        switch (scriptType) {
            case Effect:
                return new ImageView(new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "ui/icons/effects16.png")));
            case Renderer:
                return new ImageView(new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "ui/icons/renderers16.png")));
            default:
                throw new RuntimeException();
        }
    }

    public ImageScriptTypeTreeItem(ImageScriptElement.ScriptType scriptType) {
        super(scriptType);
    }
}
