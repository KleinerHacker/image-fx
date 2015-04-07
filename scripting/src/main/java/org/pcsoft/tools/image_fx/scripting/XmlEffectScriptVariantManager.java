package org.pcsoft.tools.image_fx.scripting;

import org.pcsoft.tools.image_fx.core.configuration.ConfigurationManager;

/**
 * Created by Christoph on 12.07.2014.
 */
final class XmlEffectScriptVariantManager extends XmlScriptVariantManager {

    private static XmlEffectScriptVariantManager instance = null;

    public static XmlEffectScriptVariantManager getInstance() {
        if (instance == null) {
            instance = new XmlEffectScriptVariantManager();
        }

        return instance;
    }

    private XmlEffectScriptVariantManager() {
        super(
                ConfigurationManager.EFFECTS.getVariantsDirectory(),
                ConfigurationManager.EFFECTS.getUserDataVariantsFile()
        );
    }

    @Override
    protected String getBasicFileName() {
        return "scripts/effects.variants.xml";
    }
}
