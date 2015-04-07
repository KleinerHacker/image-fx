package org.pcsoft.tools.image_fx.common;

import java.io.File;

/**
 * Created by pfeifchr on 22.08.2014.
 */
public final class FileUtils {

    public static void createFoldersIfNeeded(String dir, boolean isFile) {
        File file = new File(dir);
        if (isFile) {
            file = file.getParentFile();
        }

        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private FileUtils() {
    }
}
