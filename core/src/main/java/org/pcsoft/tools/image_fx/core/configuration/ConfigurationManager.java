package org.pcsoft.tools.image_fx.core.configuration;

import org.apache.commons.lang.SystemUtils;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Christoph on 18.06.2014.
 */
public final class ConfigurationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class);
    static final String BASIC_PATH = SystemUtils.USER_HOME + SystemUtils.FILE_SEPARATOR + ".imageFX";

    private static final String KEY_LAYERS = "LAYERS";
    private static final String KEY_RENDERER = "RENDERER";
    private static final String KEY_EFFECTS = "EFFECTS";
    private static final String KEY_PLUGINS = "PLUGINS";

    public static final ScriptConfiguration EFFECTS;
    public static final ScriptConfiguration RENDERER;
    public static final LayersConfiguration LAYERS;
    public static final PluginsConfiguration PLUGINS;
    public static final AppConfiguration APP = new AppConfiguration();

    static {
        LOGGER.info("Read system configuration from 'config.ini' (embedded)...");

        try {
            final Ini ini = new Ini(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini"));

            EFFECTS = buildScriptConfiguration(ini, KEY_EFFECTS);
            RENDERER = buildScriptConfiguration(ini, KEY_RENDERER);
            LAYERS = buildLayersConfiguration(ini);
            PLUGINS = buildPluginsConfiguration(ini);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ScriptConfiguration buildScriptConfiguration(Ini ini, String key) {
        LOGGER.debug(">>> Read Effects configuration...");

        final Profile.Section section = ini.get(key);
        return new ScriptConfiguration(section);
    }

    private static LayersConfiguration buildLayersConfiguration(Ini ini) {
        LOGGER.debug(">>> Read Layers configuration...");

        final Profile.Section layersSection = ini.get(KEY_LAYERS);
        return new LayersConfiguration(layersSection);
    }

    private static PluginsConfiguration buildPluginsConfiguration(Ini ini) {
        LOGGER.debug(">>> Read Plugins configuration...");

        final Profile.Section pluginsSection = ini.get(KEY_PLUGINS);
        return new PluginsConfiguration(pluginsSection);
    }

}
