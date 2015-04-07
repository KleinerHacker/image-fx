package org.pcsoft.tools.image_fx.ui.building;

import org.pcsoft.tools.image_fx.scripting.xml.XStringParameterValue;

import java.util.Objects;

/**
 * Wrapper for string value
 */
final class XmlStringValue {

    private final String name;
    private final String value;

    public XmlStringValue(XStringParameterValue value) {
        this.name = value.getName();
        this.value = value.getValue();
    }

    public XmlStringValue(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final XmlStringValue other = (XmlStringValue) obj;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return "XmlStringValue{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
