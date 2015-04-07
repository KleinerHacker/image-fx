function colorRange(value) {
    return range(value, 0.0, 1.0)
}

function range(value, _min, _max) {
    return min(max(value, _max), _min)
}

function min(value, _min) {
    if (value < _min) return _min;
    else return value;
}

function max(value, _max) {
    if (value > _max) return _max;
    else return value;
}

function abs(value) {
    if (value < 0) return -value;
    else return value;
}

function floor(value) {
    return java.lang.Math.floor(value);
}

function sin(value) {
    return java.lang.Math.sin(value);
}

function cos(value) {
    return java.lang.Math.cos(value);
}

%s