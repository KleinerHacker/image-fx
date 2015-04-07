package org.pcsoft.tools.image_fx.core.threading.mask;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.pcsoft.tools.image_fx.core.exceptions.ImageFXColorCalculatorException;
import org.pcsoft.tools.image_fx.core.threading.ImageInformation;
import org.pcsoft.tools.image_fx.core.threading.ImageWriterTask;

/**
 * Created by pfeifchr on 11.08.2014.
 */
public final class MaskInverterWriterTask extends ImageWriterTask {

    private final Color color;

    public MaskInverterWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter, Color color) {
        super(width, height, pixelReader, pixelWriter);
        this.color = color;
    }

    private MaskInverterWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted, Color color) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted);
        this.color = color;
    }

    @Override
    protected Color calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException {
        final Color color = pixelReader.getColor(x, y);
        return new Color(
                this.color.getRed(),
                this.color.getGreen(),
                this.color.getBlue(),
                1d - color.getOpacity()
        );
    }

    @Override
    protected ImageWriterTask createTask(int startX, int endX, int startY, int endY, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        return new MaskInverterWriterTask(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, color);
    }
}
