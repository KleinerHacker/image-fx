package org.pcsoft.tools.image_fx.scripting;

import org.pcsoft.tools.image_fx.core.configuration.ConfigurationManager;

/**
 * Created by Christoph on 12.07.2014.
 */
final class XmlRendererScriptVariantManager extends XmlScriptVariantManager {

    private static XmlRendererScriptVariantManager instance = null;

    public static XmlRendererScriptVariantManager getInstance() {
        if (instance == null) {
            instance = new XmlRendererScriptVariantManager();
        }

        return instance;
    }

    private XmlRendererScriptVariantManager() {
        super(
                ConfigurationManager.RENDERER.getVariantsDirectory(),
                ConfigurationManager.RENDERER.getUserDataVariantsFile()
        );
    }

    @Override
    protected String getBasicFileName() {
        return "scripts/renderers.variants.xml";
    }
}
