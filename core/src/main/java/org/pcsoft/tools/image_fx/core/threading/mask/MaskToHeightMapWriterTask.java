package org.pcsoft.tools.image_fx.core.threading.mask;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.pcsoft.tools.image_fx.core.exceptions.ImageFXColorCalculatorException;
import org.pcsoft.tools.image_fx.core.threading.ImageInformation;
import org.pcsoft.tools.image_fx.core.threading.ImageWriterTask;

/**
 * Created by pfeifchr on 21.08.2014.
 */
public final class MaskToHeightMapWriterTask extends ImageWriterTask {

    public MaskToHeightMapWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter) {
        super(width, height, pixelReader, pixelWriter);
    }

    private MaskToHeightMapWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted);
    }

    @Override
    protected Color calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException {
        final Color color = pixelReader.getColor(x, y);
        return new Color(
                color.getOpacity(),
                color.getOpacity(),
                color.getOpacity(),
                1
        );
    }

    @Override
    protected ImageWriterTask createTask(int startX, int endX, int startY, int endY, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        return new MaskToHeightMapWriterTask(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted);
    }
}
