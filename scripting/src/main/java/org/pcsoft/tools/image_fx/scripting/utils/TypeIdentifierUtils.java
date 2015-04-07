package org.pcsoft.tools.image_fx.scripting.utils;

import org.pcsoft.tools.image_fx.scripting.xml.XBooleanParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XColorParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XDoubleParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XImagePositionParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XIntegerParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XStringParameterDefinition;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * Created by pfeifchr on 20.06.2014.
 */
public class TypeIdentifierUtils {

    public static Class identifyTypeBy(XParameterDefinition definition) {
        if (definition instanceof XBooleanParameterDefinition) {
            return Boolean.class;
        } else if (definition instanceof XStringParameterDefinition) {
            return String.class;
        } else if (definition instanceof XIntegerParameterDefinition) {
            return Integer.class;
        } else if (definition instanceof XDoubleParameterDefinition) {
            return Double.class;
        } else if (definition instanceof XColorParameterDefinition) {
            return Color.class;
        } else if (definition instanceof XImagePositionParameterDefinition) {
            return Point2D.class;
        } else
            throw new RuntimeException();
    }
    
    private TypeIdentifierUtils() {
    }
}
