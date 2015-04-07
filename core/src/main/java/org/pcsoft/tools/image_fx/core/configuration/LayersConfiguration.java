package org.pcsoft.tools.image_fx.core.configuration;

import org.apache.commons.lang.SystemUtils;
import org.ini4j.Profile;
import org.pcsoft.tools.image_fx.common.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class LayersConfiguration {
    private static final String SUB_KEY_DIRECTORY = "directory";
    private static final String SUB_KEY_FILE_USERDATA = "file.user";

    private final Path directory;
    private final Path userDataFile;

    LayersConfiguration(Path directory, Path userDataFile) {
        this.directory = directory;
        this.userDataFile = userDataFile;
    }

    LayersConfiguration(Profile.Section section) {
        final String layersDirectory = ConfigurationManager.BASIC_PATH + SystemUtils.FILE_SEPARATOR +
                section.get(SUB_KEY_DIRECTORY);
        FileUtils.createFoldersIfNeeded(layersDirectory, false);
        this.directory = Paths.get(layersDirectory);

        final String layersUserDataFile = ConfigurationManager.BASIC_PATH + SystemUtils.FILE_SEPARATOR +
                section.get(SUB_KEY_FILE_USERDATA);
        FileUtils.createFoldersIfNeeded(layersUserDataFile, true);
        this.userDataFile = Paths.get(layersUserDataFile);
    }

    public Path getDirectory() {
        return directory;
    }

    public Path getUserDataFile() {
        return userDataFile;
    }
}