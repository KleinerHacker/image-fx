package org.pcsoft.tools.image_fx.core.threading;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.pcsoft.tools.image_fx.core.exceptions.ImageFXColorCalculatorException;

/**
 * Created by Christoph on 13.08.2014.
 */
public final class ImageMergingWriterTask extends ImageWriterTask {

    private final PixelReader targetPixelReader;
    private final PixelReader maskPixelReader;
    private final double opacity;

    public ImageMergingWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter, PixelReader targetPixelReader, PixelReader maskPixelReader, double opacity) {
        super(width, height, pixelReader, pixelWriter);
        this.targetPixelReader = targetPixelReader;
        this.maskPixelReader = maskPixelReader;
        this.opacity = opacity;
    }

    private ImageMergingWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted, PixelReader targetPixelReader, PixelReader maskPixelReader, double opacity) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted);
        this.targetPixelReader = targetPixelReader;
        this.maskPixelReader = maskPixelReader;
        this.opacity = opacity;
    }

    @Override
    protected Color calculateColor(int x, int y, PixelReader sourcePixelReader) throws ImageFXColorCalculatorException {
        //Check 1: No mask and opacity is set to 1, script color is shown full
        if (maskPixelReader == null && opacity >= 1d)
            return targetPixelReader.getColor(x, y);

        //Check 2: Is opacity 0 only old image color is usable
        if (opacity <= 0d)
            return sourcePixelReader.getColor(x, y);

        final Color maskColor = maskPixelReader == null ? new Color(1, 1, 1, 1) : maskPixelReader.getColor(x, y);
        //Check 3: Is opacity of mask is 0 only old image color is usable
        if (maskColor.getOpacity() <= 0d) //Optimization
            return sourcePixelReader.getColor(x, y);

        final Color newPicColor = targetPixelReader.getColor(x, y);
        final Color oldPicColor = sourcePixelReader.getColor(x, y);

        //Calculate opacity
        final double alpha = maskColor.getOpacity() * opacity;

        return new Color(
                newPicColor.getRed() * alpha + oldPicColor.getRed() * (1d - alpha),
                newPicColor.getGreen() * alpha + oldPicColor.getGreen() * (1d - alpha),
                newPicColor.getBlue() * alpha + oldPicColor.getBlue() * (1d - alpha),
                newPicColor.getOpacity() * alpha + oldPicColor.getOpacity() * (1d - alpha)
        );
    }

    @Override
    protected ImageWriterTask createTask(int startX, int endX, int startY, int endY, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        return new ImageMergingWriterTask(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted, targetPixelReader, maskPixelReader, opacity);
    }
}
