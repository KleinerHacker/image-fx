var r = 0;
if (redFunction.equalsIgnoreCase("sin")) {
    r = java.lang.Math.sin(y * java.lang.Math.PI * 2 * shiftRed) * 0.5 + 0.5;
} else if (redFunction.equalsIgnoreCase("cos")) {
    r = java.lang.Math.cos(y * java.lang.Math.PI * 2 * shiftRed) * 0.5 + 0.5;
}

var g = 0;
if (greenFunction.equalsIgnoreCase("sin")) {
    g = java.lang.Math.sin(y * java.lang.Math.PI * 2 * shiftGreen) * 0.5 + 0.5;
} else if (greenFunction.equalsIgnoreCase("cos")) {
    g = java.lang.Math.cos(y * java.lang.Math.PI * 2 * shiftGreen) * 0.5 + 0.5;
}

var b = 0;
if (blueFunction.equalsIgnoreCase("sin")) {
    b = java.lang.Math.sin(y * java.lang.Math.PI * 2 * shiftBlue) * 0.5 + 0.5;
} else if (blueFunction.equalsIgnoreCase("cos")) {
    b = java.lang.Math.cos(y * java.lang.Math.PI * 2 * shiftBlue) * 0.5 + 0.5;
}

return new javafx.scene.paint.Color(r, g, b, 1);