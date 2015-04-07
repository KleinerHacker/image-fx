package org.pcsoft.tools.image_fx.plugins.managing;

import javafx.scene.image.Image;
import org.pcsoft.tools.image_fx.plugins.common.exceptions.ImageFXPluginException;
import org.pcsoft.tools.image_fx.plugins.common.exceptions.ImageFXPluginExecutionException;
import org.pcsoft.tools.image_fx.plugins.tooling.interfaces.ImageFXTooling;
import org.pcsoft.tools.image_fx.plugins.tooling.interfaces.ImageFXToolingContext;
import org.pcsoft.tools.image_fx.plugins.tooling.interfaces.annotations.ImageFXToolingItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christoph on 09.08.2014.
 */
public class ToolingPluginManager extends PluginManager<ImageFXTooling> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToolingPluginManager.class);

    private static final class PluginHolder extends Plugin {
        private final String group;

        private PluginHolder(String title, String group, String iconPath, boolean needLoadedImage, String keyCombination, ImageFXTooling instance) {
            super(title, iconPath, needLoadedImage, keyCombination, instance);
            this.group = group;
        }

        public String getGroup() {
            return group;
        }

        public boolean hasGroup() {
            return group != null && !group.trim().isEmpty();
        }
    }

    public static class Plugin {
        private final String name, keyCombination, iconPath;
        private final boolean needLoadedImage;
        private final ImageFXTooling tooling;

        private Plugin(String name, String iconPath, boolean needLoadedImage, String keyCombination, ImageFXTooling tooling) {
            this.name = name;
            this.iconPath = iconPath;
            this.needLoadedImage = needLoadedImage;
            this.keyCombination = keyCombination;
            this.tooling = tooling;
        }

        public String getName() {
            return name;
        }

        public String getIconPath() {
            return iconPath;
        }

        public boolean hasIcon() {
            return iconPath != null && !iconPath.trim().isEmpty();
        }

        public boolean isNeedLoadedImage() {
            return needLoadedImage;
        }

        public String getKeyCombination() {
            return keyCombination;
        }

        public boolean hasKeyCombination() {
            return keyCombination != null && !keyCombination.trim().isEmpty();
        }

        public void execute(File imageFile, Image image, Image mask, double opacity) throws ImageFXPluginExecutionException {
            try {
                tooling.onExecute(new ImageFXToolingContext(imageFile, image, mask, opacity));
            } catch (ImageFXPluginExecutionException e) {
                throw e;
            } catch (Exception e) {
                throw new ImageFXPluginExecutionException("Unknown / Unexpected plugin execution error!", e);
            }
        }
    }

    private final List<PluginHolder> pluginHolderList = new ArrayList<>();

    public ToolingPluginManager(File pluginPath) {
        LOGGER.info("Initialize plugin manager for Tooling...");

        try {
            LOGGER.debug("> Load JARs from path: " + pluginPath.getAbsolutePath());
            final List<ImageFXTooling> instancesFromJAR = super.getInstancesFromJAR(
                    Arrays.asList(pluginPath.listFiles(pathname -> pathname.getAbsolutePath().toLowerCase().endsWith(".jar"))),
                    ImageFXTooling.class, "plugin.tooling"
            );
            LOGGER.debug("> Prepare founded plugin implementations");
            for (ImageFXTooling tooling : instancesFromJAR) {
                if (tooling.getClass().getAnnotation(ImageFXToolingItem.class) == null)
                    throw new RuntimeException("Cannot find annotation " + ImageFXToolingItem.class.getName() +
                            " on class: " + tooling.getClass().getName());

                final ImageFXToolingItem item = tooling.getClass().getAnnotation(ImageFXToolingItem.class);
                pluginHolderList.add(new PluginHolder(item.title(), item.group().isEmpty() ? null : item.group(),
                        item.icon(), item.needLoadedImage(), item.keyCombination(), tooling));
            }

            Collections.sort(pluginHolderList, (o1, o2) -> {
                final int compareGroup = o1.hasGroup() && o2.hasGroup() ?
                        o1.getGroup().compareTo(o2.getGroup()) :
                        !o1.hasGroup() && !o2.hasGroup() ? 0 : o1.hasGroup() ? -1 : 1;
                if (compareGroup != 0)
                    return compareGroup;

                return o1.getName().compareTo(o2.getName());
            });

            LOGGER.info(">>> OK");
        } catch (ImageFXPluginException e) {
            LOGGER.error("Cannot load tooling plugins!", e);
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<Plugin>> getGroupedPluginMap() {
        final Map<String, List<Plugin>> map = new LinkedHashMap<>();

        for (final PluginHolder holder : pluginHolderList) {
            if (!map.containsKey(holder.getGroup())) {
                map.put(holder.getGroup(), new ArrayList<>());
            }
            map.get(holder.getGroup()).add(holder);
        }

        return map;
    }

}
