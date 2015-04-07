var color = pixelReader.getColor(x, y);

var r = color.getRed() * factor;
var g = color.getGreen() * factor;
var b = color.getBlue() * factor;

return new javafx.scene.paint.Color(colorRange(r), colorRange(g), colorRange(b), 1);