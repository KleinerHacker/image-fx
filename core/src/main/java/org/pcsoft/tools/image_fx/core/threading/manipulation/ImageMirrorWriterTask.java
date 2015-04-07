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
public final class ImageMirrorWriterTask extends MaskedImageWriterTask {

    public enum Orientation {
        Horizontal,
        Vertical
    }

    private final Orientation orientation;

    public ImageMirrorWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter, Image maskImage, double opacity, Orientation orientation) {
        super(width, height, pixelReader, pixelWriter, maskImage, opacity);
        this.orientation = orientation;
    }

    private ImageMirrorWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted, Image maskImage, double opacity, Orientation orientation) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, maskImage, opacity);
        this.orientation = orientation;
    }

    @Override
    protected Color _calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException {
        switch (orientation) {
            case Horizontal:
                final int invertX = (imageInformation.getWidth() - 1) - x;
                return pixelReader.getColor(invertX, y);
            case Vertical:
                final int invertY = (imageInformation.getHeight() - 1) - y;
                return pixelReader.getColor(x, invertY);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected ImageWriterTask createTask(int startX, int endX, int startY, int endY, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        return new ImageMirrorWriterTask(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, maskImage, opacity, orientation);
    }
}
