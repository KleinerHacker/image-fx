x -= center.x;
y -= center.y;

var r = 0, g = 0, b = 0;
for (var i=0; i<repeatCount; i++) {
    var scale = 1.0 + blurAmount * (i / (repeatCount - repeatCount * repeatMinus));

    var calcX = x * scale + center.x;
    var calcY = y * scale + center.y;

    var color = pixelReader.getColor(calcX, calcY);

    r += color.getRed();
    g += color.getGreen();
    b += color.getBlue();
 }

 return new javafx.scene.paint.Color(r / repeatCount, g / repeatCount, b / repeatCount, 1)