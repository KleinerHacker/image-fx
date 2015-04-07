package org.pcsoft.tools.image_fx.plugins.managing;

import org.pcsoft.tools.image_fx.core.configuration.ConfigurationManager;
import org.pcsoft.tools.image_fx.plugins.common.ImageFXPlugin;
import org.pcsoft.tools.image_fx.plugins.common.exceptions.ImageFXPluginException;
import org.pcsoft.tools.image_fx.plugins.common.exceptions.ImageFXPluginInitializationException;
import org.pcsoft.tools.image_fx.plugins.common.exceptions.ImageFXPluginStructureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Christoph on 09.08.2014.
 */
public abstract class PluginManager<T extends ImageFXPlugin> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);

    public static final ToolingPluginManager TOOLING_PLUGIN_MANAGER = new ToolingPluginManager(
            ConfigurationManager.PLUGINS.getDirectory().toFile()
    );

    protected final List<T> getInstancesFromJAR(List<File> files, Class<T> $interface, String propKey) throws ImageFXPluginException{
        LOGGER.debug("Get instances from JAR...");
        final List<T> resultList = new ArrayList<>();

        for (final File file : files) {
            LOGGER.debug("> Load from file: " + file.getAbsolutePath());
            try {
                final ClassLoader classLoader = new URLClassLoader(new URL[] {file.toURI().toURL()}, getClass().getClassLoader());
                final InputStream stream = classLoader.getResourceAsStream("META-INF/plugin.properties");
                if (stream == null) {
                    System.out.println("WARNING: Found JAR without plugin: " + file.getAbsolutePath());
                    continue;
                }

                LOGGER.trace(">>> Load plugin resource file...");
                try {
                    final Properties properties = new Properties();
                    properties.load(stream);
                    final String classes = properties.getProperty(propKey);
                    if (classes == null || classes.trim().isEmpty())
                        continue;
                    final String[] classList = classes.split(";");
                    LOGGER.trace(">>> Search for plugin classes...");
                    for (final String className : classList) {
                        final Class<?> clazz = classLoader.loadClass(className);
                        if (!$interface.isAssignableFrom(clazz))
                            throw new RuntimeException("Class do not implement " + $interface.getName() + ": " + clazz.getName());
                        LOGGER.trace(">>>>> Found: " + clazz.getName());
                        resultList.add((T)clazz.newInstance());
                    }
                } catch (IOException e) {
                    LOGGER.error("Cannot load plugin properties file!", e);
                    throw new ImageFXPluginStructureException("Cannot load plugin properties file!", e);
                } catch (ClassNotFoundException e) {
                    LOGGER.error("Cannot find class!", e);
                    throw new ImageFXPluginInitializationException("Cannot find class!", e);
                } catch (InstantiationException e) {
                    LOGGER.error("Cannot instantiate class!", e);
                    throw new ImageFXPluginInitializationException("Cannot instantiate class!", e);
                } catch (IllegalAccessException e) {
                    LOGGER.error("Cannot access constructor of class!", e);
                    throw new ImageFXPluginInitializationException("Cannot access constructor of class!", e);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return resultList;
    }

}
