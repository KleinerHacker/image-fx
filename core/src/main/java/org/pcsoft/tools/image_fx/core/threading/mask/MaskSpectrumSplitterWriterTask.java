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
public final class MaskSpectrumSplitterWriterTask extends ImageWriterTask {

    public MaskSpectrumSplitterWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter) {
        super(width, height, pixelReader, pixelWriter);
    }

    private MaskSpectrumSplitterWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted);
    }

    @Override
    protected Color calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException {
        final Color color = pixelReader.getColor(x, y);
        if (color.getOpacity() > 0.5d)
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), 1d);
        else
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), 0d);
    }

    @Override
    protected ImageWriterTask createTask(int startX, int endX, int startY, int endY, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        return new MaskSpectrumSplitterWriterTask(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted);
    }
}
