package org.pcsoft.tools.image_fx.scripting;

import org.pcsoft.tools.image_fx.core.configuration.ConfigurationManager;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptException;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptParsingException;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.scripting.xml.Effects;
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
public final class XmlEffectScriptManager extends XmlScriptManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlEffectScriptManager.class);

    private static final XmlEffectScriptManager instance = new XmlEffectScriptManager();

    public static XmlEffectScriptManager getInstance() {
        return instance;
    }

    private XmlEffectScriptManager() {
        super(ImageScriptElement.ScriptType.Effect, ConfigurationManager.EFFECTS.getScriptsDirectory());
    }

    @Override
    public Map<String, ImageScriptVariant> getVariantMap(String group, String script) {
        return XmlEffectScriptVariantManager.getInstance().getVariantMap(group, script);
    }

    @Override
    protected ImageScriptVariant _storeVariant(String group, String script, String name, ImageScriptVariant variantInfo) {
        return XmlEffectScriptVariantManager.getInstance().storeVariant(group, script, name, variantInfo);
    }

    @Override
    protected List<XScriptGroup> loadFromBasicFile() throws ImageFXScriptException {
        LOGGER.trace("Load from effect basic file...");
        try {
            return JAXB.unmarshal(Thread.currentThread().getContextClassLoader().getResourceAsStream("scripts/effects.xml"), Effects.class).getScriptGroup();
        } catch (Exception e) {
            LOGGER.error("Cannot parse effect basic file!", e);
            throw new ImageFXScriptParsingException("Cannot parse effect basic file!", e);
        }
    }

    @Override
    protected List<XScriptGroup> loadFromAddOnFile(File file) throws ImageFXScriptException {
        LOGGER.trace("Load from effect addon file '" + file.getAbsolutePath() + "'...");
        try {
            return JAXB.unmarshal(file, Effects.class).getScriptGroup();
        } catch (Exception e) {
            LOGGER.error("Cannot parse effect add-on file: " + file.getAbsolutePath(), e);
            throw new ImageFXScriptParsingException("Cannot parse effect add-on file: " + file.getAbsolutePath(), e);
        }
    }

    @Override
    protected void addToScriptManager(ImageScriptElement scriptInfo) throws ImageFXScriptException {
        JavaScriptManager.addEffectScript(scriptInfo);
    }

    @Override
    protected void loadVariants() throws ImageFXScriptException {
        XmlEffectScriptVariantManager.getInstance().loadElements();
    }
}
