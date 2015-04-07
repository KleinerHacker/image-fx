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
public final class MaskResetWriterTask extends ImageWriterTask {

    private final Color color;

    public MaskResetWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter, Color color) {
        super(width, height, pixelReader, pixelWriter);
        this.color = color;
    }

    private MaskResetWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted, Color color) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted);
        this.color = color;
    }

    @Override
    protected Color calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException {
        return new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                pixelReader.getColor(x, y).getOpacity()
        );
    }

    @Override
    protected ImageWriterTask createTask(int startX, int endX, int startY, int endY, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        return new MaskResetWriterTask(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, color);
    }
}
