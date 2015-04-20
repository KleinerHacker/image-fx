package org.pcsoft.tools.image_fx.ui.managing;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import org.pcsoft.tools.image_fx.common.listeners.ProgressListener;
import org.pcsoft.tools.image_fx.common.threads.DaemonThreadFactory;
import org.pcsoft.tools.image_fx.scripting.types.image.AbstractImageScript;
import org.pcsoft.tools.image_fx.ui.Constants;
import org.pcsoft.tools.image_fx.ui.components.ImagePreviewFactory;
import org.pcsoft.tools.image_fx.ui.components.controller.ImagePreviewController;
import org.pcsoft.tools.image_fx.ui.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by pfeifchr on 26.06.2014.
 */
public final class XmlScriptPreviewManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlScriptPreviewManager.class);

    private static final Map<AbstractImageScript, ImagePreviewFactory.ImagePreview> imagePreviewMap = new HashMap<>();

    private static final Lock PREVIEW_UPDATE_TASK_LOCK = new ReentrantLock();
    private static Future previewUpdateTask = null;

    public static void createPreview(AbstractImageScript scripting, Pane pane, boolean needRebuild, ImagePreviewController.OnImageApplyListener effectListener) {
        LOGGER.trace("> Create preview for: " + scripting.getName() + " (" + scripting.getId() + ")");

        final ImagePreviewFactory.ImagePreview imagePreview = ImagePreviewFactory.create(scripting, pane, needRebuild);
        if (!needRebuild) {
            imagePreview.setImage(new WritableImage(Constants.THUMB_WIDTH, Constants.THUMB_HEIGHT));
        }

        imagePreview.setOnEffectListener(effectListener);
        imagePreviewMap.put(scripting, imagePreview);
    }

    private static void updatePreviewWithThumbnail(AbstractImageScript scripting, Image thumbnail) {
        if (!imagePreviewMap.containsKey(scripting))
            throw new IllegalArgumentException("Unknown effect key: " + scripting);

        final ImagePreviewFactory.ImagePreview imagePreview = imagePreviewMap.get(scripting);
        if (!imagePreview.isNeedRebuild())
            return;

        imagePreview.setImage(thumbnail);
        imagePreview.hideWaiter();
    }

    private static void showWaiterForPreview(AbstractImageScript scripting) {
        if (!imagePreviewMap.containsKey(scripting))
            throw new IllegalArgumentException("Unknown effect key: " + scripting);

        final ImagePreviewFactory.ImagePreview imagePreview = imagePreviewMap.get(scripting);
        if (!imagePreview.isNeedRebuild())
            return;

        imagePreview.showWaiter();
    }

    public static void updateAllPreviewsWithThumbnail(Image thumbnail, ProgressListener listener) {
        LOGGER.debug("Update all previews...");

        PREVIEW_UPDATE_TASK_LOCK.lock();
        try {
            if (previewUpdateTask != null) {
                LOGGER.warn("Try cancel current running preview update task...");
                previewUpdateTask.cancel(true);
                listener.onProgressUpdate(ProgressBar.INDETERMINATE_PROGRESS);
            }
        } finally {
            PREVIEW_UPDATE_TASK_LOCK.unlock();
        }

        if (listener != null) {
            listener.onStartProgress();
        }

        PREVIEW_UPDATE_TASK_LOCK.lock();
        imagePreviewMap.keySet().forEach(XmlScriptPreviewManager::showWaiterForPreview);

        try {
            previewUpdateTask = Executors.newCachedThreadPool(new DaemonThreadFactory("Preview updater")).submit(() -> {
                final Future ownTask = previewUpdateTask;

                int counter = 0;
                for (AbstractImageScript scripting : imagePreviewMap.keySet()) {
                    LOGGER.trace("> Update preview for: " + scripting.getName() + " (" + scripting.getId() + ")");

                    updatePreviewWithThumbnail(scripting, thumbnail);
                    counter++;

                    if (ownTask.isCancelled()) {
                        LOGGER.warn("Cancel preview update task!");
                        return;
                    }

                    if (listener != null) {
                        listener.onProgressUpdate((double)counter / (double)imagePreviewMap.size());
                    }
                }

                if (listener != null) {
                    listener.onFinishProgress();
                }

                PREVIEW_UPDATE_TASK_LOCK.lock();
                try {
                    previewUpdateTask = null;
                } finally {
                    PREVIEW_UPDATE_TASK_LOCK.unlock();
                }
            });
        } finally {
            PREVIEW_UPDATE_TASK_LOCK.unlock();
        }
    }

    /**
     * Creates based on the given image new preview images (thumbnail)
     * @param imageIn Image to create preview from. If image is <code>null</code> example image is used
     * @throws java.io.IOException
     */
    public static void updateAllPreviewsFromImage(Image imageIn, ProgressListener listener) throws IOException {
        if (imageIn == null) {
            updateAllPreviewsWithThumbnail(ImageUtils.createExamplePreviewThumbnail(), listener);
            return;
        }

        try (final ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
            final BufferedImage bufferedImage = new BufferedImage(
                    (int) imageIn.getWidth(),
                    (int) imageIn.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            SwingFXUtils.fromFXImage(imageIn, bufferedImage);
            ImageIO.write(bufferedImage, "png", bout);

            try (final ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray())) {
                final Image image = ImageUtils.createPreviewThumbnail(bin);
                updateAllPreviewsWithThumbnail(image, listener);
            }
        }
    }

    private XmlScriptPreviewManager() {
    }
}
