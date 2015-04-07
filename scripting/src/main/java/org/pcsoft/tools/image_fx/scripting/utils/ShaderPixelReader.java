package org.pcsoft.tools.image_fx.scripting.utils;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * Created by Christoph on 25.06.2014.
 */
public final class ShaderPixelReader {

    private final PixelReader pixelReader;
    private final int width, height;

    public ShaderPixelReader(PixelReader pixelReader, int width, int height) {
        this.pixelReader = pixelReader;
        this.width = width;
        this.height = height;
    }

    public Color getColor(double x, double y) {
        return pixelReader.getColor(
                (int)(Math.max(0, Math.min(1, x)) * (width - 1)),
                (int)(Math.max(0, Math.min(1, y)) * (height - 1))
        );
    }

    public int getArgb(double x, double y) {
        return pixelReader.getArgb(
                (int)(Math.max(0, Math.min(1, x)) * (width - 1)),
                (int)(Math.max(0, Math.min(1, y)) * (height - 1))
        );
    }
}
