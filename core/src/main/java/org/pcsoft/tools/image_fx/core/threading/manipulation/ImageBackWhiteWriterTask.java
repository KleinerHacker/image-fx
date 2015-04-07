package org.pcsoft.tools.image_fx.core.threading.manipulation;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.pcsoft.tools.image_fx.core.exceptions.ImageFXColorCalculatorException;
import org.pcsoft.tools.image_fx.core.threading.ImageInformation;
import org.pcsoft.tools.image_fx.core.threading.ImageWriterTask;
import org.pcsoft.tools.image_fx.core.threading.MaskedImageWriterTask;

/**
 * Created by pfeifchr on 22.08.2014.
 */
public final class ImageBackWhiteWriterTask extends MaskedImageWriterTask {

    public ImageBackWhiteWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter, Image maskImage, double opacity) {
        super(width, height, pixelReader, pixelWriter, maskImage, opacity);
    }

    private ImageBackWhiteWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted, Image maskImage, double opacity) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, maskImage, opacity);
    }

    @Override
    protected Color _calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException {
        return pixelReader.getColor(x, y).grayscale().getRed() < 0.5d ? Color.BLACK : Color.WHITE;
    }

    @Override
    protected ImageWriterTask createTask(int startX, int endX, int startY, int endY, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        return new ImageBackWhiteWriterTask(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, maskImage, opacity);
    }
}
