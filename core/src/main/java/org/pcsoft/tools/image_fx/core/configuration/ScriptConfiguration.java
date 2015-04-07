package org.pcsoft.tools.image_fx.core.configuration;

import org.apache.commons.lang.SystemUtils;
import org.ini4j.Profile;
import org.pcsoft.tools.image_fx.common.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class ScriptConfiguration {
    private static final String SUB_KEY_DIRECTORY_SCRIPTS = "directory.scripts";
    private static final String SUB_KEY_DIRECTORY_VARIANTS = "directory.variants";
    private static final String SUB_KEY_FILE_USERDATA_VARIANTS = "file.varinats.user";

    private final Path scriptsDirectory;
    private final Path variantsDirectory;
    private final Path userDataVariantsFile;

    ScriptConfiguration(Path scriptsDirectory, Path variantsDirectory, Path userDataVariantsFile) {
        this.scriptsDirectory = scriptsDirectory;
        this.variantsDirectory = variantsDirectory;
        this.userDataVariantsFile = userDataVariantsFile;
    }

    ScriptConfiguration(Profile.Section section) {
        final String scriptsDirectory = ConfigurationManager.BASIC_PATH + SystemUtils.FILE_SEPARATOR +
                section.get(SUB_KEY_DIRECTORY_SCRIPTS);
        FileUtils.createFoldersIfNeeded(scriptsDirectory, false);
        this.scriptsDirectory = Paths.get(scriptsDirectory);

        final String variantsDirectory = ConfigurationManager.BASIC_PATH + SystemUtils.FILE_SEPARATOR +
                section.get(SUB_KEY_DIRECTORY_VARIANTS);
        FileUtils.createFoldersIfNeeded(variantsDirectory, false);
        this.variantsDirectory = Paths.get(variantsDirectory);

        final String userDataVariantsFile = ConfigurationManager.BASIC_PATH + SystemUtils.FILE_SEPARATOR +
                section.get(SUB_KEY_FILE_USERDATA_VARIANTS);
        FileUtils.createFoldersIfNeeded(userDataVariantsFile, true);
        this.userDataVariantsFile = Paths.get(userDataVariantsFile);
    }

    public Path getScriptsDirectory() {
        return scriptsDirectory;
    }

    public Path getVariantsDirectory() {
        return variantsDirectory;
    }

    public Path getUserDataVariantsFile() {
        return userDataVariantsFile;
    }
}