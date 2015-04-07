package org.pcsoft.tools.image_fx.scripting.utils;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptParsingException;

/**
 * Created by pfeifchr on 20.06.2014.
 */
public final class ObjectConverterUtils {

    public static Object convertFromString(String value, Class targetClass) throws ImageFXScriptParsingException {
        if (targetClass == Boolean.class) {
            try {
                return Boolean.parseBoolean(value);
            } catch (Exception e) {
                throw new ImageFXScriptParsingException("Not allowed value for Boolean: " + value);
            }
        } else if (targetClass == String.class) {
            return value;
        } else if (targetClass == Integer.class) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new ImageFXScriptParsingException("Not allowed value for Integer: " + value);
            }
        } else if (targetClass == Double.class) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new ImageFXScriptParsingException("Not allowed value for Double: " + value);
            }
        } else if (targetClass == Color.class) {
            try {
                return Color.web(value);
            } catch (Exception e) {
                throw new ImageFXScriptParsingException("Not allowed value for Color: " + value);
            }
        } else if (targetClass == Point2D.class) {
            final String[] parts = value.split(",");
            if (parts.length != 2)
                throw new ImageFXScriptParsingException("Not allowed value for Image Position: " + value);
            try {
                return new Point2D(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
            } catch (NumberFormatException e) {
                throw new ImageFXScriptParsingException("Not allowed value for Image Position (Double): " + value);
            }
        } else
            throw new RuntimeException();
    }

    private ObjectConverterUtils() {
    }
}
