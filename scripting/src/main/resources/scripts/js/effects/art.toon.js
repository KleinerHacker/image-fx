var color = pixelReader.getColor(x, y);

var r = color.getRed();
var g = color.getGreen();
var b = color.getBlue();

r *= factor;
g *= factor;
b *= factor;

r = java.lang.Math.floor(r);
g = java.lang.Math.floor(g);
b = java.lang.Math.floor(b);

r /= factor;
g /= factor;
b /= factor;

return new javafx.scene.paint.Color(r, g, b, 1);