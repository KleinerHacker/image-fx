var color = pixelReader.getColor(x, y);
color = color.grayscale();

if (color.getRed() < factor) {
    return colorDark;
} else {
    return colorBright;
}