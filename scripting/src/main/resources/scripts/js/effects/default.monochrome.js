var color = pixelReader.getColor(x, y);

var luminance = color.getRed() * 0.30 +
               color.getGreen() * 0.59 +
               color.getBlue() * 0.11;

return new javafx.scene.paint.Color(
    luminance * filterColor.getRed(),
    luminance * filterColor.getGreen(),
    luminance * filterColor.getBlue(),
    1
)