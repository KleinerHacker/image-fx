package org.pcsoft.tools.image_fx.scripting;

import org.apache.commons.lang.SystemUtils;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptException;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptExecutionException;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptParsingException;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.apache.commons.io.IOUtils;
import org.pcsoft.tools.image_fx.scripting.utils.ShaderPixelReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christoph on 17.06.2014.
 */
public final class JavaScriptManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaScriptManager.class);

    private static final String FUNC_EFFECT_SCRIPT = "function run(x, y, pixelReader %s) { %s }";
    private static final String FUNC_RENDERER_SCRIPT = "function run(x, y %s) { %s }";
    private static final String BASIC_SCRIPT;

    private static final Map<ImageScriptElement, ScriptEngine> engineEffectMap = new HashMap<>();
    private static final Map<ImageScriptElement, ScriptEngine> engineRendererMap = new HashMap<>();

    static {
        LOGGER.info("Initialize java script manager...");

        try {
            BASIC_SCRIPT = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("scripts/js/basic.js"), "UTF-8");
            LOGGER.info(">>> OK");
        } catch (IOException e) {
            LOGGER.error("Cannot initialize java script manager!", e);
            throw new RuntimeException(e);
        }
    }

    public static void addEffectScript(ImageScriptElement scriptInfo) throws ImageFXScriptException {
        if (scriptInfo.getScriptType() != ImageScriptElement.ScriptType.Effect)
            throw new IllegalArgumentException("Wrong type of script: " + scriptInfo.getScriptType().name());

        LOGGER.debug("Add effect script: " + scriptInfo.getName() + " (" + scriptInfo.getId() + ")");
        final ScriptEngine scriptEngine = createScript(scriptInfo, FUNC_EFFECT_SCRIPT);
        engineEffectMap.put(scriptInfo, scriptEngine);
    }

    public static Color runEffectScript(ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, int width, int height, int x, int y, PixelReader pixelReader) throws ImageFXScriptException {
        if (!engineEffectMap.containsKey(scriptInfo))
            throw new IllegalArgumentException("Unknown effect key: " + scriptInfo);

        //LOGGER.debug("Run effect script: " + scriptInfo.getName() + " (" + scriptInfo.getId() + ")"); Too many output
        final ScriptEngine scriptEngine = engineEffectMap.get(scriptInfo);
        return runScript(scriptEngine, scriptInfo, variantInfo, width, height, x, y, pixelReader);
    }

    public static void addRendererScript(ImageScriptElement scriptInfo) throws ImageFXScriptException {
        if (scriptInfo.getScriptType() != ImageScriptElement.ScriptType.Renderer)
            throw new IllegalArgumentException("Wrong type of script: " + scriptInfo.getScriptType().name());

        LOGGER.debug("Add renderer script: " + scriptInfo.getName() + " (" + scriptInfo.getId() + ")");
        final ScriptEngine scriptEngine = createScript(scriptInfo, FUNC_RENDERER_SCRIPT);
        engineRendererMap.put(scriptInfo, scriptEngine);
    }

    public static Color runRendererScript(ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, int width, int height, int x, int y) throws ImageFXScriptException {
        if (!engineRendererMap.containsKey(scriptInfo))
            throw new IllegalArgumentException("Unknown renderer key: " + scriptInfo);

        //LOGGER.debug("Run renderer script: " + scriptInfo.getName() + " (" + scriptInfo.getId() + ")"); Too many output
        final ScriptEngine scriptEngine = engineRendererMap.get(scriptInfo);
        return runScript(scriptEngine, scriptInfo, variantInfo, width, height, x, y, null);
    }

    private static ScriptEngine createScript(ImageScriptElement scriptInfo, String func) throws ImageFXScriptException {
        LOGGER.debug("Build and pre-compile script...");
        try {
            LOGGER.trace("Create nashorn engine...");
            final ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

            LOGGER.trace("Prepare script...");
            final String functionScript = String.format(BASIC_SCRIPT, func);
            final String script = String.format(functionScript, buildParameterNameList(scriptInfo), scriptInfo.getScriptFragment());
            LOGGER.trace(">>> Script: " + SystemUtils.LINE_SEPARATOR + script);

            LOGGER.trace("Pre-Compile script...");
            scriptEngine.eval(script);

            return scriptEngine;
        } catch (ScriptException e) {
            LOGGER.error("Error in script: " + scriptInfo.getScriptFragment(), e);
            throw new ImageFXScriptParsingException("Error in script: " + scriptInfo.getScriptFragment(), e);
        }
    }

    /**
     *
     * @param scriptEngine
     * @param variantInfo
     * @param width
     * @param height
     * @param x
     * @param y
     * @param pixelReader <b>optional</b>
     * @return
     * @throws ImageFXScriptException
     */
    private static Color runScript(ScriptEngine scriptEngine, ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, int width, int height, int x, int y, PixelReader pixelReader) throws ImageFXScriptException {
//        LOGGER.debug("Run script..."); Too many output

        try {
            return (Color) ((Invocable)scriptEngine).invokeFunction(
                    "run", buildParameterValueList(scriptInfo, variantInfo, width, height, x, y, pixelReader));
        } catch (ScriptException | NoSuchMethodException e) {
            LOGGER.error("Error while running script: " + scriptInfo.getId() +
                    " in " + scriptInfo.getGroupInfo().getId(), e);
            throw new ImageFXScriptParsingException("Error while running script: " + scriptInfo.getId() +
                    " in " + scriptInfo.getGroupInfo().getId(), e);
        } catch (Exception e) {
            LOGGER.error("Error while running script: " + scriptInfo.getId() +
                    " in " + scriptInfo.getGroupInfo().getId(), e);
            throw new ImageFXScriptExecutionException("Error while running script: " + scriptInfo.getId() +
                    " in " + scriptInfo.getGroupInfo().getId(), e);
        }
    }

    private static String buildParameterNameList(ImageScriptElement scriptInfo) {
        final List<String> params = new ArrayList<>(scriptInfo.getParameterMap().keySet());
        if (params.isEmpty())
            return "";

        final StringBuilder sb = new StringBuilder();
        sb.append(',');
        for (final String param : params) {
            sb.append(param);
            sb.append(',');
        }
        sb.delete(sb.length()-1, sb.length());

        return sb.toString();
    }

    /**
     *
     * @param scriptInfo
     * @param variantInfo
     * @param width
     * @param height
     * @param x
     * @param y
     * @param pixelReader <b>optional</b>
     * @return
     */
    private static Object[] buildParameterValueList(ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, int width, int height, int x, int y, PixelReader pixelReader) throws ImageFXScriptParsingException {
        final List<Object> values = new ArrayList<>();
        values.add((double)x / (double)width);
        values.add((double)y / (double)height);
        if (pixelReader != null) {
            values.add(new ShaderPixelReader(pixelReader, width, height));
        }
        values.addAll(XmlScriptManager.buildScriptParameterValueMap(
                scriptInfo.getParameterMap(),
                variantInfo
        ).values());

        return values.toArray(new Object[values.size()]);
    }

    private JavaScriptManager() {
    }
}
