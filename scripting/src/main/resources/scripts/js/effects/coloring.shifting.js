var color = pixelReader.getColor(x, y);
var r = color.getRed(), g = color.getGreen(), b = color.getBlue();

var shiftX = x + shiftFactor, shiftY = y + shiftFactor;

var shiftColor = pixelReader.getColor(shiftX, shiftY);

r += shiftColor.getRed() * channelColor.getRed();
g += shiftColor.getGreen() * channelColor.getGreen();
b += shiftColor.getBlue() * channelColor.getBlue();

return new javafx.scene.paint.Color(colorRange(r), colorRange(g), colorRange(b), 1);