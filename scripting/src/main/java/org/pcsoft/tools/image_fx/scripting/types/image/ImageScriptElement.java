package org.pcsoft.tools.image_fx.scripting.types.image;

import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.pcsoft.tools.image_fx.scripting.XmlEffectScriptManager;
import org.pcsoft.tools.image_fx.scripting.XmlRendererScriptManager;
import org.pcsoft.tools.image_fx.scripting.XmlScriptManager;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptException;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptLoadingException;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptElementGroupProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptParameterProperty;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptVariantProperty;
import org.pcsoft.tools.image_fx.scripting.xml.XScript;
import org.pcsoft.tools.image_fx.scripting.xml.XScriptGroup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Christoph on 21.06.2014.
 */
public final class ImageScriptElement extends AbstractImageScript<ImageScriptElementGroup, ImageScriptElementGroupProperty> {

    public enum ScriptType {
        Effect,
        Renderer
    }

    private static String getScriptFragment(XScript.Script script, String path, boolean inJar) throws IOException {
        if (script.getJavaScript() != null) {
            return script.getJavaScript();
        } else if (script.getJavaScriptFile() != null) {
            if (inJar) {
                final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        path + "/" + script.getJavaScriptFile().getValue());
                if (stream == null)
                    throw new IOException("Cannot find file in JAR: " + path + "/" + script.getJavaScriptFile().getValue());

                return IOUtils.toString(stream, script.getJavaScriptFile().getEncoding());
            } else {
                final File file = new File(path + SystemUtils.FILE_SEPARATOR + script.getJavaScriptFile().getValue());
                if (!file.exists())
                    throw new IOException("Cannot find file: " + path + SystemUtils.FILE_SEPARATOR + script.getJavaScriptFile().getValue());

                return FileUtils.readFileToString(file, script.getJavaScriptFile().getEncoding());
            }
        } else
            throw new RuntimeException();
    }

    private static Map<String, ImageScriptVariantProperty> getVariantMap(String group, String script, ScriptType scriptType) {
        final Map<String, ImageScriptVariant> map;
        switch (scriptType) {
            case Effect:
                map = XmlEffectScriptManager.getInstance().getVariantMap(group, script);
                break;
            case Renderer:
                map = XmlRendererScriptManager.getInstance().getVariantMap(group, script);
                break;
            default:
                throw new RuntimeException();
        }

        final Map<String, ImageScriptVariantProperty> resultMap = new LinkedHashMap<>();
        for (final ImageScriptVariant variant : map.values()) {
            resultMap.put(variant.getId(), new ImageScriptVariantProperty(variant));
        }

        return resultMap;
    }

    private static ImageScriptVariantProperty findDefaultVariantInfo(Collection<ImageScriptVariantProperty> variantInfoList) {
        for (final ImageScriptVariantProperty variantInfoProperty : variantInfoList) {
            if (variantInfoProperty.isDefault()) {
                return variantInfoProperty;
            }
        }

        return variantInfoList.isEmpty() ? new ImageScriptVariantProperty(null) : variantInfoList.iterator().next();
    }

    private static Map<String, ImageScriptParameterProperty> getScriptParameterMap(XScript scriptElement) throws ImageFXScriptException {
        final Map<String, ImageScriptParameter> scriptParameterMap = XmlScriptManager.getScriptParameterMap(scriptElement);
        final Map<String, ImageScriptParameterProperty> resultMap = new HashMap<>();

        for (final String key : scriptParameterMap.keySet()) {
            resultMap.put(key, new ImageScriptParameterProperty(scriptParameterMap.get(key)));
        }

        return resultMap;
    }

    private final StringProperty scriptFragment;
    private final XScript scriptElement;
    private final ObjectProperty<ScriptType> scriptType;
    private final ObjectProperty<byte[]> graphic;

    private final ObservableMap<String, ImageScriptParameterProperty> parameterMap;
    private final ObservableMap<String, ImageScriptVariantProperty> variantMap;
    private final ImageScriptVariantProperty defaultVariant;

    public ImageScriptElement(XScript scriptElement, XScriptGroup scriptGroup, ScriptType scriptType, String path, boolean inJar) throws ImageFXScriptException {
        super(scriptElement.getId(), scriptElement.getName(), new ImageScriptElementGroupProperty(scriptGroup));
        try {
            this.scriptElement = scriptElement;
            this.scriptFragment = new ReadOnlyStringWrapper(getScriptFragment(scriptElement.getScript(), path, inJar));
            this.scriptType = new ReadOnlyObjectWrapper<>(scriptType);
            this.graphic = new ReadOnlyObjectWrapper<>(scriptElement.getGraphic());

            parameterMap = new ObservableMapWrapper<>(getScriptParameterMap(scriptElement));
            variantMap = new ObservableMapWrapper<>(getVariantMap(scriptGroup.getId(), scriptElement.getId(), scriptType));
            defaultVariant = findDefaultVariantInfo(variantMap.values());
        } catch (IOException e) {
            throw new ImageFXScriptLoadingException("Cannot load script: " + scriptElement.getId() + " in " + scriptGroup.getId(), e);
        }
    }

    public XScript getScriptElement() {
        return scriptElement;
    }

    public ObservableMap<String, ImageScriptParameterProperty> getParameterPropertyMap() {
        return parameterMap;
    }

    public Map<String, ImageScriptParameter> getParameterMap() {
        final Map<String, ImageScriptParameter> map = new HashMap<>();
        for (String key : parameterMap.keySet()) {
            map.put(key, parameterMap.get(key).get());
        }

        return map;
    }

    public ObservableMap<String, ImageScriptVariantProperty> getVariantPropertyMap() {
        return variantMap;
    }

    public Map<String, ImageScriptVariant> getVariantMap() {
        final Map<String, ImageScriptVariant> map = new LinkedHashMap<>();
        for (String key : variantMap.keySet()) {
            map.put(key, variantMap.get(key).get());
        }

        return map;
    }

    public boolean hasGraphic() {
        return graphic.get() != null;
    }

    public String getScriptFragment() {
        return scriptFragment.get();
    }

    public StringProperty scriptFragmentProperty() {
        return scriptFragment;
    }

    public ScriptType getScriptType() {
        return scriptType.get();
    }

    public ObjectProperty<ScriptType> scriptTypeProperty() {
        return scriptType;
    }

    public byte[] getGraphic() {
        return graphic.get();
    }

    public ObjectProperty<byte[]> graphicProperty() {
        return graphic;
    }

    public ImageScriptVariant getDefaultVariant() {
        return defaultVariant.get();
    }

    public ImageScriptVariantProperty defaultVariantProperty() {
        return defaultVariant;
    }

    @Override
    public String toString() {
        return "ImageScriptElement{" +
                "scriptFragment='" + scriptFragment + '\'' +
                ", scriptElement=" + scriptElement +
                ", scriptType=" + scriptType +
                ", graphic=" + Arrays.toString(graphic.get()) +
                "} " + super.toString();
    }
}
