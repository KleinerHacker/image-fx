package org.pcsoft.tools.image_fx.core.configuration;

import org.apache.commons.lang.SystemUtils;
import org.ini4j.Profile;
import org.pcsoft.tools.image_fx.common.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PluginsConfiguration {

    private static final String SUB_KEY_DIRECTORY = "directory";

    private final Path directory;

    PluginsConfiguration(Path directory) {
        this.directory = directory;
    }

    PluginsConfiguration(Profile.Section section) {
        final String pluginsDirectory = ConfigurationManager.BASIC_PATH + SystemUtils.FILE_SEPARATOR +
                section.get(SUB_KEY_DIRECTORY);
        FileUtils.createFoldersIfNeeded(pluginsDirectory, false);
        this.directory = Paths.get(pluginsDirectory);
    }

    public Path getDirectory() {
        return directory;
    }
}