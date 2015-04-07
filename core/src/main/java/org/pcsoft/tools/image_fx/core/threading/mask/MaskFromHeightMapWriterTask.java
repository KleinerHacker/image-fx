package org.pcsoft.tools.image_fx.core.threading.mask;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.pcsoft.tools.image_fx.core.exceptions.ImageFXColorCalculatorException;
import org.pcsoft.tools.image_fx.core.threading.ImageInformation;
import org.pcsoft.tools.image_fx.core.threading.ImageWriterTask;

/**
 * Created by Christoph on 25.07.2014.
 */
public final class MaskFromHeightMapWriterTask extends ImageWriterTask {

    private final Color color;

    public MaskFromHeightMapWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter, Color color) {
        super(width, height, pixelReader, pixelWriter);
        this.color = color;
    }

    private MaskFromHeightMapWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted, Color color) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted);
        this.color = color;
    }

    @Override
    protected Color calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException {
        return new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                pixelReader.getColor(x, y).grayscale().getRed()
        );
    }

    @Override
    protected ImageWriterTask createTask(int startX, int endX, int startY, int endY, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        return new MaskFromHeightMapWriterTask(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, color);
    }
}
