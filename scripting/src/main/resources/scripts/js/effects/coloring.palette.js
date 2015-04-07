var color = pixelReader.getColor(x, y);
if (palette.equalsIgnoreCase("BGR")) {
return new javafx.scene.paint.Color(color.getBlue(), color.getGreen(), color.getRed(), 1);
} else if (palette.equalsIgnoreCase("RBG")) {
return new javafx.scene.paint.Color(color.getRed(), color.getBlue(), color.getGreen(), 1);
} else if (palette.equalsIgnoreCase("BRG")) {
return new javafx.scene.paint.Color(color.getBlue(), color.getRed(), color.getGreen(), 1);
} else if (palette.equalsIgnoreCase("GBR")) {
return new javafx.scene.paint.Color(color.getGreen(), color.getBlue(), color.getRed(), 1);
} else if (palette.equalsIgnoreCase("GRB")) {
return new javafx.scene.paint.Color(color.getGreen(), color.getRed(), color.getBlue(), 1);
} else {
return color;
}