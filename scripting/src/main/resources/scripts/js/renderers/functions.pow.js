var base = 0;

if (type.equalsIgnoreCase("x")) base = x;
else if (type.equalsIgnoreCase("y")) base = y;
else if (type.equalsIgnoreCase("x*y")) base = x*y;
else if (type.equalsIgnoreCase("x+y")) base = x+y;
else if (type.equalsIgnoreCase("x-y")) base = x-y;
else if (type.equalsIgnoreCase("x/y")) base = x/y;

var value = java.lang.Math.pow(base, exp) % interpolator;

var minValue = factor - tolerance;
var maxValue = factor + tolerance;

if (value >= minValue && value <= maxValue) {
    return lightColor;
} else {
    return darkColor;
}