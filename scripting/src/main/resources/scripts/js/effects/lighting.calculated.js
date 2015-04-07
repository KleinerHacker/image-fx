var color = pixelReader.getColor(x, y);

var brightColor = new javafx.scene.paint.Color(
    colorRange(color.getRed() * brightFactor),
    colorRange(color.getGreen() * brightFactor),
    colorRange(color.getBlue() * brightFactor),
    1);
var darkColor = new javafx.scene.paint.Color(
    colorRange(color.getRed() * darkFactor),
    colorRange(color.getGreen() * darkFactor),
    colorRange(color.getBlue() * darkFactor),
    1);

if (calculation.equalsIgnoreCase("mul")) {
    return new javafx.scene.paint.Color(
        colorRange(brightColor.getRed() * darkColor.getRed()),
        colorRange(brightColor.getGreen() * darkColor.getGreen()),
        colorRange(brightColor.getBlue() * darkColor.getBlue()),
        1);
} else if (calculation.equalsIgnoreCase("div")) {
    return new javafx.scene.paint.Color(
        colorRange(brightColor.getRed() / darkColor.getRed()),
        colorRange(brightColor.getGreen() / darkColor.getGreen()),
        colorRange(brightColor.getBlue() / darkColor.getBlue()),
        1);
} else if (calculation.equalsIgnoreCase("add")) {
    return new javafx.scene.paint.Color(
        colorRange(brightColor.getRed() + darkColor.getRed()),
        colorRange(brightColor.getGreen() + darkColor.getGreen()),
        colorRange(brightColor.getBlue() + darkColor.getBlue()),
        1);
} else if (calculation.equalsIgnoreCase("sub")) {
    return new javafx.scene.paint.Color(
        colorRange(brightColor.getRed() - darkColor.getRed()),
        colorRange(brightColor.getGreen() - darkColor.getGreen()),
        colorRange(brightColor.getBlue() - darkColor.getBlue()),
        1);
} else {
   return color;
}