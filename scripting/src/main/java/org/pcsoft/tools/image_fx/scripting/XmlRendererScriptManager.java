package org.pcsoft.tools.image_fx.scripting;

import org.pcsoft.tools.image_fx.core.configuration.ConfigurationManager;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptException;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptParsingException;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.scripting.xml.Renderers;
import org.pcsoft.tools.image_fx.scripting.xml.XScriptGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by pfeifchr on 19.06.2014.
 */
public final class XmlRendererScriptManager extends XmlScriptManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlRendererScriptManager.class);

    private static final XmlRendererScriptManager instance = new XmlRendererScriptManager();

    public static XmlRendererScriptManager getInstance() {
        return instance;
    }

    private XmlRendererScriptManager() {
        super(ImageScriptElement.ScriptType.Renderer, ConfigurationManager.RENDERER.getScriptsDirectory());
    }

    @Override
    public Map<String, ImageScriptVariant> getVariantMap(String group, String script) {
        return XmlRendererScriptVariantManager.getInstance().getVariantMap(group, script);
    }

    @Override
    protected ImageScriptVariant _storeVariant(String group, String script, String name, ImageScriptVariant variantInfo) {
        return XmlRendererScriptVariantManager.getInstance().storeVariant(group, script, name, variantInfo);
    }

    @Override
    protected List<XScriptGroup> loadFromBasicFile() throws ImageFXScriptException {
        LOGGER.trace("Load from renderer basic file...");
        try {
            return JAXB.unmarshal(Thread.currentThread().getContextClassLoader().getResourceAsStream("scripts/renderers.xml"), Renderers.class).getScriptGroup();
        } catch (Exception e) {
            LOGGER.error("Cannot parse renderer basic file!", e);
            throw new ImageFXScriptParsingException("Cannot parse renderer basic file!", e);
        }
    }

    @Override
    protected List<XScriptGroup> loadFromAddOnFile(File file) throws ImageFXScriptException {
        LOGGER.trace("Load from renderer addon file '" + file.getAbsolutePath() + "'...");
        try {
            return JAXB.unmarshal(file, Renderers.class).getScriptGroup();
        } catch (Exception e) {
            LOGGER.error("Cannot parse renderer add-on file: " + file.getAbsolutePath(), e);
            throw new ImageFXScriptParsingException("Cannot parse renderer add-on file: " + file.getAbsolutePath(), e);
        }
    }

    @Override
    protected void addToScriptManager(ImageScriptElement scriptInfo) throws ImageFXScriptException {
        JavaScriptManager.addRendererScript(scriptInfo);
    }

    @Override
    protected void loadVariants() throws ImageFXScriptException {
        XmlRendererScriptVariantManager.getInstance().loadElements();
    }
}
