package org.pcsoft.tools.image_fx.core.threading.mask;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.pcsoft.tools.image_fx.core.exceptions.ImageFXColorCalculatorException;
import org.pcsoft.tools.image_fx.core.threading.ImageInformation;
import org.pcsoft.tools.image_fx.core.threading.ImageWriterTask;

/**
 * Created by pfeifchr on 22.08.2014.
 */
public final class MaskBlurWriterTask extends ImageWriterTask {

    private static final int SIZE = 2;

    private final Color color;

    public MaskBlurWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter, Color color) {
        super(width, height, pixelReader, pixelWriter);
        this.color = color;
    }

    private MaskBlurWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted, Color color) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted);
        this.color = color;
    }

    @Override
    protected Color calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException {
        final int minX = x - SIZE < 0 ? 0 : x - SIZE;
        final int maxX = x + SIZE >= imageInformation.getWidth() ? imageInformation.getWidth() : x + SIZE;
        final int minY = y - SIZE < 0 ? 0 : y - SIZE;
        final int maxY = y + SIZE >= imageInformation.getHeight() ? imageInformation.getHeight() : y + SIZE;

        double opacityCounter = 0d;
        int counter=0;
        for (int xx = minX; xx <= maxX; xx++) {
            for (int yy = minY; yy <= maxY; yy++) {
                final Color blurColor = pixelReader.getColor(xx, yy);

                opacityCounter += blurColor.getOpacity();
                counter++;
            }
        }

        return new Color(
                this.color.getRed(),
                this.color.getGreen(),
                this.color.getBlue(),
                opacityCounter / (double)counter
        );
    }

    @Override
    protected ImageWriterTask createTask(int startX, int endX, int startY, int endY, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        return new MaskBlurWriterTask(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, color);
    }
}
