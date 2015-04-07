package org.pcsoft.tools.image_fx.plugins.common;

import javafx.scene.image.Image;

import java.io.File;

/**
 * Created by Christoph on 09.08.2014.
 */
public abstract class ImageFXContext {

    private final File imageFile;
    private final Image image, mask;
    private final double opacity;

    protected ImageFXContext(File imageFile, Image image, Image mask, double opacity) {
        this.imageFile = imageFile;
        this.image = image;
        this.mask = mask;
        this.opacity = opacity;
    }

    public File getImageFile() {
        return imageFile;
    }

    public Image getImage() {
        return image;
    }

    public Image getMask() {
        return mask;
    }

    public double getOpacity() {
        return opacity;
    }
}
