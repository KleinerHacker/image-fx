var color = pixelReader.getColor(x, y);
var channel = (color.getRed() * filterColor.getRed() +
              color.getGreen() * filterColor.getGreen() +
              color.getBlue() * filterColor.getBlue()) / 3;
return new javafx.scene.paint.Color(channel, channel, channel, 1);