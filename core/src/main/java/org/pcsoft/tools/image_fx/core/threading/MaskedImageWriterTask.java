package org.pcsoft.tools.image_fx.core.threading;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.pcsoft.tools.image_fx.core.exceptions.ImageFXColorCalculatorException;

/**
 * Created by pfeifchr on 25.08.2014.
 */
public abstract class MaskedImageWriterTask extends ImageWriterTask {

    protected final Image maskImage;
    protected final double opacity;

    protected MaskedImageWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter, Image maskImage, double opacity) {
        super(width, height, pixelReader, pixelWriter);
        this.maskImage = maskImage;
        this.opacity = opacity;
    }

    protected MaskedImageWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted, Image maskImage, double opacity) {
        super(startX, endX, startY, endY, imageInformation, pixelReader, pixelWriter, splitted);
        this.maskImage = maskImage;
        this.opacity = opacity;
    }

    @Override
    protected final Color calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException {
        //Check 1: No mask and opacity is set to 1, script color is shown full
        if (maskImage == null && opacity >= 1d)
            return _calculateColor(x, y, pixelReader);

        //Check 2: Is opacity 0 only old image color is usable
        if (opacity <= 0d)
            return pixelReader.getColor(x, y);

        final Color maskColor = maskImage == null ? new Color(1, 1, 1, 1) : maskImage.getPixelReader().getColor(x, y);
        //Check 3: Is opacity of mask is 0 only old image color is usable
        if (maskColor.getOpacity() <= 0d) //Optimization
            return pixelReader.getColor(x, y);

        final Color newPicColor = _calculateColor(x, y, pixelReader);
        final Color oldPicColor = pixelReader.getColor(x, y);

        //Calculate opacity
        final double alpha = maskColor.getOpacity() * opacity;

        return new Color(
                newPicColor.getRed() * alpha + oldPicColor.getRed() * (1d - alpha),
                newPicColor.getGreen() * alpha + oldPicColor.getGreen() * (1d - alpha),
                newPicColor.getBlue() * alpha + oldPicColor.getBlue() * (1d - alpha),
                newPicColor.getOpacity() * alpha + oldPicColor.getOpacity() * (1d - alpha)
        );
    }

    protected abstract Color _calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException;
}
