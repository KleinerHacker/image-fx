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
 * Created by pfeifchr on 21.08.2014.
 */
public final class ImageRotateWriterTask extends MaskedImageWriterTask {

    public enum Rotation {
        Left,
        Right
    }

    private final Rotation rotation;

    public ImageRotateWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter, Image maskImage, double opacity, Rotation rotation) {
        super(height, width, pixelReader, pixelWriter, maskImage, opacity);//Rotated
        this.rotation = rotation;
    }

    private ImageRotateWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted, Image maskImage, double opacity, Rotation rotation) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, maskImage, opacity);
        this.rotation = rotation;
    }

    @Override
    protected Color _calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException {
        switch (rotation) {
            case Left:
                return pixelReader.getColor((imageInformation.getHeight() - 1) - y, x);
            case Right:
                return pixelReader.getColor(y, (imageInformation.getWidth() - 1) - x);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected ImageWriterTask createTask(int startX, int endX, int startY, int endY, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        return new ImageRotateWriterTask(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, maskImage, opacity, rotation);
    }
}
