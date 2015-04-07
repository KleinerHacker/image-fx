package org.pcsoft.tools.image_fx.scripting.threading;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.pcsoft.tools.image_fx.core.threading.ImageWriterTask;
import org.pcsoft.tools.image_fx.core.threading.ImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayer;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pcsoft.tools.image_fx.common.listeners.ProgressListener;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by pfeifchr on 22.08.2014.
 */
public final class JavaScriptImageWriterTaskRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaScriptImageWriterTaskRunner.class);

    public static Image runJavaScriptTask(Image image, Image maskImage, double opacity, List<ImageScriptLayer.ImageScriptHolder> scriptHolderList, ProgressListener progressListener) {
        LOGGER.debug("Run java script task (multi script)");

        Image currentImage = image;
        for (final ImageScriptLayer.ImageScriptHolder scriptHolder : scriptHolderList) {
            final Image partMaskImage = scriptHolder.getMask() == null ? null :
                    new Image(
                            new ByteArrayInputStream(scriptHolder.getMask()), image.getWidth(), image.getHeight(),
                            false, true
                    );
            currentImage = runJavaScriptTask(currentImage, partMaskImage, scriptHolder.getOpacity(), scriptHolder.getScriptInfo(), scriptHolder.getVariantInfo(), progressListener);
        }

        return ImageWriterTaskRunner.runImageMergingTask(image, currentImage, maskImage, opacity);
    }

    public static WritableImage runJavaScriptTask(Image image, Image maskImage, double opacity, ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, ProgressListener progressListener) {
        LOGGER.debug("Run java script task (single script)");

        switch (scriptInfo.getScriptType()) {
            case Effect:
                return runJavaScriptEffectTask(image, maskImage, opacity, scriptInfo, variantInfo, progressListener);
            case Renderer:
                return runJavaScriptRendererTask(image, maskImage, opacity, scriptInfo, variantInfo, progressListener);
            default:
                throw new RuntimeException();
        }
    }

    private static WritableImage runJavaScriptEffectTask(Image image, Image maskImage, double opacity, ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, ProgressListener progressListener) {
        LOGGER.trace(">>> Run java script task for effect: " + scriptInfo.getName() + " (" + scriptInfo.getId() + ")" +
                (variantInfo == null ? "" : " > " + variantInfo.getName() + " (" + variantInfo.getId() + ")"));

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage(pixelReader, (int) image.getWidth(), (int) image.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new JavaScriptEffectImageWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter, scriptInfo, variantInfo, maskImage, opacity);
        task.setProgressListener(progressListener);
        new ForkJoinPool(Runtime.getRuntime().availableProcessors()).invoke(task);

        return writableImage;
    }

    private static WritableImage runJavaScriptRendererTask(Image image, Image maskImage, double opacity, ImageScriptElement scriptInfo, ImageScriptVariant variantInfo, ProgressListener progressListener) {
        LOGGER.trace(">>> Run java script task for renderer: " + scriptInfo.getName() + " (" + scriptInfo.getId() + ")" +
                (variantInfo == null ? "" : " > " + variantInfo.getName() + " (" + variantInfo.getId() + ")"));

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage(pixelReader, (int) image.getWidth(), (int) image.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new JavaScriptRendererImageWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter, scriptInfo, variantInfo, maskImage, opacity);
        task.setProgressListener(progressListener);
        new ForkJoinPool(Runtime.getRuntime().availableProcessors()).invoke(task);

        return writableImage;
    }

    private JavaScriptImageWriterTaskRunner() {
    }
}
