var r=0.0, g=0.0, b=0.0;
var counter = 0;
for (var by = -factor; by <= factor; by += (factor * interpolator)) {
    for (var bx = -factor; bx <= factor; bx += (factor * interpolator)) {
        var color = pixelReader.getColor(x + bx, y + by);
        r += color.getRed();
        g += color.getGreen();
        b += color.getBlue();

        counter++;
    }
}

return new javafx.scene.paint.Color(r / counter, g / counter, b / counter, 1);