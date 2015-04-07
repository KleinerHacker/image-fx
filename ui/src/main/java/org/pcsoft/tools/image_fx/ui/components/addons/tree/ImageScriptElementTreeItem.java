package org.pcsoft.tools.image_fx.ui.components.addons.tree;

import javafx.scene.control.TreeItem;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;

import java.util.Objects;

/**
 * Created by Christoph on 22.06.2014.
 */
public class ImageScriptElementTreeItem extends TreeItem<Object> {

    public static final class Holder {
        private final ImageScriptElement scriptElement;
        private ImageScriptVariant scriptVariant;

        private Holder(ImageScriptElement scriptElement, ImageScriptVariant scriptVariant) {
            this.scriptElement = scriptElement;
            this.scriptVariant = scriptVariant;
        }

        public ImageScriptElement getScriptElement() {
            return scriptElement;
        }

        public ImageScriptVariant getScriptVariant() {
            return scriptVariant;
        }

        public void setScriptVariant(ImageScriptVariant scriptVariant) {
            this.scriptVariant = scriptVariant;
        }

        @Override
        public int hashCode() {
            return Objects.hash(scriptElement, scriptVariant);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Holder other = (Holder) obj;
            return Objects.equals(this.scriptElement, other.scriptElement) && Objects.equals(this.scriptVariant, other.scriptVariant);
        }

        @Override
        public String toString() {
            return "Holder{" +
                    "scriptElement=" + scriptElement +
                    ", scriptVariant=" + scriptVariant +
                    '}';
        }
    }

    public ImageScriptElementTreeItem(ImageScriptElement scriptInfo) {
        super(new Holder(scriptInfo, scriptInfo.getDefaultVariant() == null ? null :
                new ImageScriptVariant(scriptInfo.getDefaultVariant().getVariant(), true)
        ));
    }

    public ImageScriptElement getScriptElement() {
        return ((Holder)getValue()).getScriptElement();
    }

    public ImageScriptVariant getScriptVariant() {
        return ((Holder)getValue()).getScriptVariant();
    }

    public void setVariantInfo(ImageScriptVariant scriptVariant) {
        ((Holder)getValue()).setScriptVariant(scriptVariant);
    }
}
