package org.pcsoft.tools.image_fx.scripting.types.image;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.pcsoft.tools.image_fx.common.IdUtils;
import org.pcsoft.tools.image_fx.scripting.XmlEffectScriptManager;
import org.pcsoft.tools.image_fx.scripting.XmlRendererScriptManager;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptElementProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptLayerGroupProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptLayerProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptVariantProperty;
import org.pcsoft.tools.image_fx.scripting.xml.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represent a group with scripts
 */
public final class ImageScriptLayer extends AbstractImageScript<ImageScriptLayerGroup, ImageScriptLayerGroupProperty> {

    public static final class ImageScriptHolder {
        private final ImageScriptElementProperty scriptInfo;
        private final ImageScriptVariantProperty variantInfo;
        private final ObjectProperty<byte[]> mask;
        private final DoubleProperty opacity;

        public ImageScriptHolder(ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, byte[] mask, double opacity) {
            this.scriptInfo = new ImageScriptElementProperty(scriptInfo);
            this.variantInfo = new ImageScriptVariantProperty(variantInfo);
            this.mask = new SimpleObjectProperty<>(mask);
            this.opacity = new SimpleDoubleProperty(opacity);
        }

        public ImageScriptElement getScriptInfo() {
            return scriptInfo.get();
        }

        public ImageScriptElementProperty scriptInfoProperty() {
            return scriptInfo;
        }

        public ImageScriptVariant getVariantInfo() {
            return variantInfo.get();
        }

        public ImageScriptVariantProperty variantInfoProperty() {
            return variantInfo;
        }

        public byte[] getMask() {
            return mask.get();
        }

        public ObjectProperty<byte[]> maskProperty() {
            return mask;
        }

        public void setMask(byte[] mask) {
            this.mask.set(mask);
        }

        public double getOpacity() {
            return opacity.get();
        }

        public DoubleProperty opacityProperty() {
            return opacity;
        }

        public void setOpacity(double opacity) {
            this.opacity.set(opacity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(scriptInfo.get(), variantInfo.get(), mask.get(), opacity.get());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final ImageScriptHolder other = (ImageScriptHolder) obj;
            return Objects.equals(this.scriptInfo.get(), other.scriptInfo.get()) && Objects.equals(this.variantInfo.get(), other.variantInfo.get())&& Objects.deepEquals(this.mask.get(), other.mask.get()) && Objects.equals(this.opacity.get(), other.opacity.get());
        }

        @Override
        public String toString() {
            return "ImageScriptHolder{" +
                    "scriptInfo=" + scriptInfo +
                    ", variantInfo=" + variantInfo +
                    ", mask=" + mask +
                    ", opacity=" + opacity +
                    '}';
        }
    }

    private final ObservableList<ImageScriptLayerProperty.ImageScriptHolderProperty> scriptHolderList;
    private final BooleanProperty readOnly;

    public ImageScriptLayer(String id, String name, ImageScriptLayerGroup groupInfo, List<ImageScriptLayerProperty.ImageScriptHolderProperty> scriptHolderList) {
        super(id, name, new ImageScriptLayerGroupProperty(groupInfo));
        this.scriptHolderList = new ObservableListWrapper<>(scriptHolderList);
        this.readOnly = new ReadOnlyBooleanWrapper(false);
    }

    public ImageScriptLayer(XLayer xLayer, XLayerGroup xLayerGroup, boolean readOnly) {
        super(xLayer.getId(), xLayer.getName(), new ImageScriptLayerGroupProperty(xLayerGroup));

        this.readOnly = new ReadOnlyBooleanWrapper(readOnly);
        this.scriptHolderList = new ObservableListWrapper<>(new ArrayList<>());
        for (XScriptElement xScriptElement : xLayer.getScriptElement()) {
            final ImageScriptElement scriptInfo;
            switch (xScriptElement.getType()) {
                case EFFECT:
                    scriptInfo = XmlEffectScriptManager.getInstance().getElementBy(
                            xScriptElement.getScriptRef(), xScriptElement.getGroupRef());
                    break;
                case RENDERER:
                    scriptInfo = XmlRendererScriptManager.getInstance().getElementBy(
                            xScriptElement.getScriptRef(), xScriptElement.getGroupRef());
                    break;
                default:
                    throw new RuntimeException();
            }
            if (scriptInfo == null)
                throw new RuntimeException("Internal error: Cannot find referenced script: " + xScriptElement.getScriptRef() +
                        " in group " + xScriptElement.getGroupRef() + ", type is " + xScriptElement.getType().name());

            final ImageScriptVariant variantInfo = scriptInfo.getDefaultVariant() != null ? scriptInfo.getDefaultVariant() :
                    scriptInfo.getVariantMap().values().stream().findFirst().orElse(null);

            this.scriptHolderList.add(new ImageScriptLayerProperty.ImageScriptHolderProperty(
                    scriptInfo, variantInfo != null ? new ImageScriptVariant(IdUtils.generateId(), "TEMP", variantInfo) : null,
                    xScriptElement.getMask(), xScriptElement.getOpacity()));
        }
    }

    public ObservableList<ImageScriptLayerProperty.ImageScriptHolderProperty> getScriptHolderPropertyList() {
        return scriptHolderList;
    }

    public List<ImageScriptHolder> getScriptHolderList() {
        return scriptHolderList.stream().map(imageScriptHolderProperty -> imageScriptHolderProperty.get()).collect(Collectors.toList());
    }

    public boolean isReadOnly() {
        return readOnly.get();
    }

    public BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    @Override
    public String toString() {
        return "ImageScriptLayer{" +
                "scriptHolderList=" + scriptHolderList +
                ", readOnly=" + readOnly +
                "} " + super.toString();
    }
}
