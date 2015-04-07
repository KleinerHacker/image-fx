package org.pcsoft.tools.image_fx.scripting.types.image.prop;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayer;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayerGroup;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.scripting.xml.XLayer;
import org.pcsoft.tools.image_fx.scripting.xml.XLayerGroup;

import java.util.List;

/**
 * Created by pfeifchr on 23.07.2014.
 */
public final class ImageScriptLayerProperty extends AbstractImageScriptProperty<ImageScriptLayerGroup, ImageScriptLayerGroupProperty, ImageScriptLayer> {

    public static final class ImageScriptHolderProperty extends ReadOnlyObjectWrapper<ImageScriptLayer.ImageScriptHolder> {
        public ImageScriptHolderProperty(ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, byte[] mask, double opacity) {
            super(new ImageScriptLayer.ImageScriptHolder(scriptInfo, variantInfo, mask, opacity));
            init(get());
        }

        public ImageScriptHolderProperty(ImageScriptLayer.ImageScriptHolder imageScriptHolder) {
            super(imageScriptHolder);
            init(imageScriptHolder);
        }

        public ImageScriptElement getScriptInfo() {
            return get().getScriptInfo();
        }

        public ImageScriptVariantProperty variantInfoProperty() {
            return get().variantInfoProperty();
        }

        public ImageScriptElementProperty scriptInfoProperty() {
            return get().scriptInfoProperty();
        }

        public ImageScriptVariant getVariantInfo() {
            return get().getVariantInfo();
        }

        public ObjectProperty<byte[]> maskProperty() {
            return get().maskProperty();
        }

        public byte[] getMask() {
            return get().getMask();
        }

        public void setMask(byte[] mask) {
            get().setMask(mask);
        }

        public void setOpacity(double opacity) {
            get().setOpacity(opacity);
        }

        public DoubleProperty opacityProperty() {
            return get().opacityProperty();
        }

        public double getOpacity() {
            return get().getOpacity();
        }

        private void init(ImageScriptLayer.ImageScriptHolder imageScriptHolder) {
            imageScriptHolder.scriptInfoProperty().addListener(observable -> fireValueChangedEvent());
            imageScriptHolder.scriptInfoProperty().addListener((observableValue, imageScriptElement, imageScriptElement2) -> fireValueChangedEvent());
            imageScriptHolder.variantInfoProperty().addListener((observableValue, imageScriptVariant, imageScriptVariant2) -> fireValueChangedEvent());
            imageScriptHolder.variantInfoProperty().addListener(observable -> fireValueChangedEvent());
            imageScriptHolder.maskProperty().addListener((observableValue, bytes, bytes2) -> fireValueChangedEvent());
            imageScriptHolder.opacityProperty().addListener((observableValue, number, number2) -> fireValueChangedEvent());
        }
    }

    public ImageScriptLayerProperty(XLayer xLayer, XLayerGroup xLayerGroup, boolean readOnly) {
        super(new ImageScriptLayer(xLayer, xLayerGroup, readOnly));
        init(get());
    }

    public ImageScriptLayerProperty(ImageScriptLayer imageScriptLayer) {
        super(imageScriptLayer);
        init(imageScriptLayer);
    }

    public ObservableList<ImageScriptHolderProperty> getScriptHolderPropertyList() {
        return get().getScriptHolderPropertyList();
    }

    public List<ImageScriptLayer.ImageScriptHolder> getScriptHolderList() {
        return get().getScriptHolderList();
    }

    public boolean isReadOnly() {
        return get().isReadOnly();
    }

    public BooleanProperty readOnlyProperty() {
        return get().readOnlyProperty();
    }

    private void init(ImageScriptLayer imageScriptLayer) {
        if (imageScriptLayer == null)
            return;

        imageScriptLayer.getScriptHolderPropertyList().addListener((ListChangeListener<ImageScriptHolderProperty>) change -> {
            fireValueChangedEvent();
        });
        imageScriptLayer.getScriptHolderPropertyList().addListener((Observable observable) -> {
            fireValueChangedEvent();
        });
    }

    @Override
    public String toString() {
        return get() == null ? "null" : get().toString();
    }
}
