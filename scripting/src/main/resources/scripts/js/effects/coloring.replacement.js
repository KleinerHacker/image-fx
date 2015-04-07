var color = pixelReader.getColor(x, y);
var origR = color.getRed();
var origG = color.getGreen();
var origB = color.getBlue();

var sourceR = sourceColor.getRed();
var sourceG = sourceColor.getGreen();
var sourceB = sourceColor.getBlue();

if (origR >= sourceR - factor && origR <= sourceR + factor &&
    origG >= sourceG - factor && origG <= sourceG + factor &&
    origB >= sourceB - factor && origB <= sourceB + factor) {
    if (smoothly) {
        var smoothValue = (abs(origR - sourceR) + abs(origG - sourceG) + abs(origB - sourceB)) / 3.0;
        return new javafx.scene.paint.Color(
            color.getRed() * (1.0-smoothValue) + targetColor.getRed() * smoothValue,
            color.getGreen() * (1.0-smoothValue) + targetColor.getGreen() * smoothValue,
            color.getBlue() * (1.0-smoothValue) + targetColor.getBlue() * smoothValue,
            1
        );
    } else {
        return targetColor;
    }
} else {
    return color;
}