package org.pcsoft.tools.image_fx.plugins.tooling.interfaces;

import javafx.scene.image.Image;
import org.pcsoft.tools.image_fx.plugins.common.ImageFXContext;

import java.io.File;

/**
 * Created by Christoph on 09.08.2014.
 */
public final class ImageFXToolingContext extends ImageFXContext {

    public ImageFXToolingContext(File imageFile, Image image, Image mask, double opacity) {
        super(imageFile, image, mask, opacity);
    }
}
