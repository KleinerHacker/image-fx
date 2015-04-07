package org.pcsoft.tools.image_fx.scripting.threading;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.pcsoft.tools.image_fx.core.exceptions.ImageFXColorCalculatorException;
import org.pcsoft.tools.image_fx.core.threading.ImageInformation;
import org.pcsoft.tools.image_fx.core.threading.ImageWriterTask;
import org.pcsoft.tools.image_fx.scripting.JavaScriptManager;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptException;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;

/**
 * Created by pfeifchr on 19.06.2014.
 */
public final class JavaScriptRendererImageWriterTask extends JavaScriptImageWriterTask {

    public JavaScriptRendererImageWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter, ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, Image maskImage, double opacity) {
        super(width, height, pixelReader, pixelWriter, scriptInfo, variantInfo, maskImage, opacity);
    }

    public JavaScriptRendererImageWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted, ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, Image maskImage, double opacity) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, scriptInfo, variantInfo, maskImage, opacity);
    }

    @Override
    protected Color _calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException {
        try {
            return JavaScriptManager.runRendererScript(scriptInfo, variantInfo,
                    imageInformation.getWidth(), imageInformation.getHeight(), x, y);
        } catch (ImageFXScriptException e) {
            throw new ImageFXColorCalculatorException(e);
        }
    }

    @Override
    protected ImageWriterTask createTask(int startX, int endX, int startY, int endY, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        return new JavaScriptRendererImageWriterTask(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, scriptInfo, variantInfo, maskImage, opacity);
    }

}
