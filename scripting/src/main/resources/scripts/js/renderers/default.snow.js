var random = new java.util.Random();
var r = random.nextDouble() % 1;
var g = random.nextDouble() % 1;
var b = random.nextDouble() % 1;

var color = new javafx.scene.paint.Color(r, g, b, 1);

if (colored) {
    return color;
} else {
    return color.grayscale();
}