package org.pcsoft.tools.image_fx.scripting.threading;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import org.pcsoft.tools.image_fx.core.threading.ImageInformation;
import org.pcsoft.tools.image_fx.core.threading.MaskedImageWriterTask;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;

/**
 * Created by pfeifchr on 16.06.2014.
 */
public abstract class JavaScriptImageWriterTask extends MaskedImageWriterTask {

    protected final ImageScriptElement scriptInfo;
    protected final ImageScriptVariant variantInfo;

    protected JavaScriptImageWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter, ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, Image maskImage, double opacity) {
        super(width, height, pixelReader, pixelWriter, maskImage, opacity);
        this.scriptInfo = scriptInfo;
        this.variantInfo = variantInfo;
    }

    protected JavaScriptImageWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted, ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, Image maskImage, double opacity) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, maskImage, opacity);
        this.scriptInfo = scriptInfo;
        this.variantInfo = variantInfo;
    }
}
