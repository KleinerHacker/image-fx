var minX = x-size, maxX = x+size;
var minY = y-size, maxY = y+size;

var color = pixelReader.getColor(x, y);
var color1 = pixelReader.getColor(minX, minY);
var color2 = pixelReader.getColor(maxX, maxY);

var r = color.getRed() - color1.getRed() * interpolator + color2.getRed() * interpolator;
var g = color.getGreen() - color1.getGreen() * interpolator + color2.getGreen() * interpolator;
var b = color.getBlue() - color1.getBlue() * interpolator + color2.getBlue() * interpolator;

var resColor = new javafx.scene.paint.Color(colorRange(r), colorRange(g), colorRange(b), 1);

if (gray) {
    return resColor.grayscale();
} else {
    return resColor;
}