var color = pixelReader.getColor(x, y);
var r = color.getRed() * (1-opacity) + addColor.getRed() * opacity;
var g = color.getGreen() * (1-opacity) + addColor.getGreen() * opacity;
var b = color.getBlue() * (1-opacity) + addColor.getBlue() * opacity;

return new javafx.scene.paint.Color(r, g, b, 1);