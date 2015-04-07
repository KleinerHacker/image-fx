package org.pcsoft.tools.image_fx.core.configuration;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.apache.commons.lang.SystemUtils;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class AppConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfiguration.class);

    private static final String FILE_NAME = "app.ini";
    private static final File FILE = new File(ConfigurationManager.BASIC_PATH + SystemUtils.FILE_SEPARATOR + FILE_NAME);

    public static final class MaskConfiguration {
        private static final String KEY = "MASK";
        private static final String SUB_KEY_COLOR = "color";

        private Color color = Color.RED;

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        private void loadSection(Ini ini) {
            final Profile.Section section = ini.get(KEY);
            if (section == null)
                return;

            color = Color.web(section.get(SUB_KEY_COLOR));
        }

        private void saveSection(Ini ini) {
            final Profile.Section section = ini.add(KEY);
            section.put(SUB_KEY_COLOR, color.toString());
        }
    }

    public static final class ReopenConfiguration {
        private static final int MAX_SIZE = 10;

        private static final String KEY = "REOPEN";
        private static final String SUB_KEY_IMAGE = "image";
        private static final String SUB_KEY_HEIGHT_MAP = "height_map";

        private final ObservableList<File> imageFileList = new ObservableListWrapper<>(new ArrayList<>());
        private final ObservableList<File> heightMapFileList = new ObservableListWrapper<>(new ArrayList<>());

        public void addImageFile(File file) {
            //For reopen
            if (imageFileList.contains(file)) {
                imageFileList.remove(file);
            }

            imageFileList.add(0, file);
            while (imageFileList.size() > MAX_SIZE) {
                imageFileList.remove(imageFileList.size() - 1);
            }
        }

        public void removeImageFile(File file) {
            imageFileList.remove(file);
        }

        public ObservableList<File> getImageFileList() {
            return imageFileList;
        }

        public void addHeightMapFile(File file) {
            heightMapFileList.add(0, file);
            while (heightMapFileList.size() > MAX_SIZE) {
                heightMapFileList.remove(heightMapFileList.size() - 1);
            }
        }

        public void removeHeightMapFile(File file) {
            heightMapFileList.remove(file);
        }

        public ObservableList<File> getHeightMapFileList() {
            return heightMapFileList;
        }

        private void loadSection(Ini ini) {
            final Profile.Section section = ini.get(KEY);
            if (section == null)
                return;

            for (final String key : section.keySet()) {
                if (key.startsWith(SUB_KEY_IMAGE)) {
                    imageFileList.add(new File(section.get(key)));
                } else if (key.startsWith(SUB_KEY_HEIGHT_MAP)) {
                    heightMapFileList.add(new File(section.get(key)));
                } else {
                    LOGGER.warn("Unknown key found: " + key);
                }
            }
        }

        private void saveSection(Ini ini) {
            final Profile.Section section = ini.add(KEY);

            int counter=0;
            for (final File file : imageFileList) {
                section.put(SUB_KEY_IMAGE + "." + counter, file.getAbsolutePath());
                counter++;
            }

            counter=0;
            for (final File file : heightMapFileList) {
                section.put(SUB_KEY_HEIGHT_MAP + "." + counter, file.getAbsolutePath());
                counter++;
            }
        }
    }

    private final MaskConfiguration maskConfiguration = new MaskConfiguration();
    private final ReopenConfiguration reopenConfiguration = new ReopenConfiguration();

    public AppConfiguration() {
        if (FILE.exists()) {
            try {
                final Ini ini = new Ini(FILE);
                maskConfiguration.loadSection(ini);
                reopenConfiguration.loadSection(ini);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public MaskConfiguration getMaskConfiguration() {
        return maskConfiguration;
    }

    public ReopenConfiguration getReopenConfiguration() {
        return reopenConfiguration;
    }

    public void save() {
        final Ini ini = new Ini();
        maskConfiguration.saveSection(ini);
        reopenConfiguration.saveSection(ini);

        try {
            ini.store(FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}