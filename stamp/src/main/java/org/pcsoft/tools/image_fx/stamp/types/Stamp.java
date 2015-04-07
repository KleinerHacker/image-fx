package org.pcsoft.tools.image_fx.stamp.types;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import org.pcsoft.tools.image_fx.stamp.types.prop.StampGroupProperty;
import org.pcsoft.tools.image_fx.stamp.xml.XStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

/**
 * Created by Christoph on 16.08.2014.
 */
public final class Stamp {

    private static final Logger LOGGER = LoggerFactory.getLogger(Stamp.class);

    private final StringProperty id, name;
    private final StampGroupProperty maskGroup;
    private final ObjectProperty<Image> image;
    private final XStamp stamp;

    public Stamp(XStamp stamp, StampGroup stampGroup) {
        this.stamp = stamp;
        this.maskGroup = new StampGroupProperty(stampGroup);
        this.id = new ReadOnlyStringWrapper(stamp.getId());
        this.name = new ReadOnlyStringWrapper(stamp.getName());

        if (stamp.getStampContent() != null) {
            if (stamp.getStampContent().getB64() != null) {
                this.image = new ReadOnlyObjectWrapper<>(new Image(new ByteArrayInputStream(stamp.getStampContent().getB64())));
            } else if (stamp.getStampContent().getHex() != null) {
                this.image = new ReadOnlyObjectWrapper<>(new Image(new ByteArrayInputStream(stamp.getStampContent().getHex())));
            } else if (stamp.getStampContent().getResource() != null) {
//                try {
                    final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(stamp.getStampContent().getResource());
                    if (stream == null) {
                        LOGGER.error("Cannot find mask resource: " + stamp.getStampContent().getResource());
                        throw new RuntimeException("Cannot find mask resource: " + stamp.getStampContent().getResource());
                    }
                    this.image = new ReadOnlyObjectWrapper<>(new Image(stream));
//                } catch (IOException e) {
//                    LOGGER.error("Internal resource error!", e);
//                    throw new RuntimeException("Internal resource error!", e);
//                }
            } else
                throw new RuntimeException();
        } else
            throw new RuntimeException();
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StampGroup getMaskGroup() {
        return maskGroup.get();
    }

    public StampGroupProperty maskGroupProperty() {
        return maskGroup;
    }

    public Image getImage() {
        return image.get();
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Stamp other = (Stamp) obj;
        return Objects.equals(this.id.get(), other.id.get());
    }

    @Override
    public String toString() {
        return "Mask{" +
                "id=" + id +
                ", name=" + name +
                ", maskGroup=" + maskGroup +
                '}';
    }
}
