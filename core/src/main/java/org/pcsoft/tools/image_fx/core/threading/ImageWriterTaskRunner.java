package org.pcsoft.tools.image_fx.core.threading;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.pcsoft.tools.image_fx.core.configuration.ConfigurationManager;
import org.pcsoft.tools.image_fx.core.threading.manipulation.ImageBackWhiteWriterTask;
import org.pcsoft.tools.image_fx.core.threading.manipulation.ImageGrayscaleWriterTask;
import org.pcsoft.tools.image_fx.core.threading.manipulation.ImageInvertWriterTask;
import org.pcsoft.tools.image_fx.core.threading.manipulation.ImageMirrorWriterTask;
import org.pcsoft.tools.image_fx.core.threading.manipulation.ImageRotateWriterTask;
import org.pcsoft.tools.image_fx.core.threading.mask.MaskBlurWriterTask;
import org.pcsoft.tools.image_fx.core.threading.mask.MaskFromHeightMapWriterTask;
import org.pcsoft.tools.image_fx.core.threading.mask.MaskInverterWriterTask;
import org.pcsoft.tools.image_fx.core.threading.mask.MaskResetWriterTask;
import org.pcsoft.tools.image_fx.core.threading.mask.MaskSpectrumSplitterWriterTask;
import org.pcsoft.tools.image_fx.core.threading.mask.MaskToHeightMapWriterTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ForkJoinPool;

/**
 * Created by pfeifchr on 22.08.2014.
 */
public final class ImageWriterTaskRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageWriterTaskRunner.class);
    public static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    public static WritableImage runImageBlackWhiteWriterTask(Image image, Image maskImage, double opacity) {
        LOGGER.debug("Run image black and white");

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new ImageBackWhiteWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter, maskImage, opacity);
        FORK_JOIN_POOL.execute(task);

        return writableImage;
    }

    public static WritableImage runImageGrayscaleWriterTask(Image image, Image maskImage, double opacity) {
        LOGGER.debug("Run image gray scale");

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new ImageGrayscaleWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter, maskImage, opacity);
        FORK_JOIN_POOL.execute(task);

        return writableImage;
    }

    public static WritableImage runImageInvertWriterTask(Image image, Image maskImage, double opacity) {
        LOGGER.debug("Run image invert");

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new ImageInvertWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter, maskImage, opacity);
        FORK_JOIN_POOL.execute(task);

        return writableImage;
    }

    public static WritableImage runImageRotateWriterTask(Image image, Image maskImage, double opacity, ImageRotateWriterTask.Rotation rotation) {
        LOGGER.debug("Run image rotate with rotation " + rotation.name());

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage((int) image.getHeight(), (int) image.getWidth());//Rotated
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new ImageRotateWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter, maskImage, opacity, rotation);
        FORK_JOIN_POOL.execute(task);

        return writableImage;
    }

    public static WritableImage runImageMirrorWriterTask(Image image, Image maskImage, double opacity, ImageMirrorWriterTask.Orientation orientation) {
        LOGGER.debug("Run image mirror with orientation " + orientation.name());

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage(pixelReader, (int) image.getWidth(), (int) image.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new ImageMirrorWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter, maskImage, opacity, orientation);
        FORK_JOIN_POOL.execute(task);

        return writableImage;
    }

    public static WritableImage runMaskBlurTask(Image image) {
        LOGGER.debug("Run mask blur");

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage(pixelReader, (int) image.getWidth(), (int) image.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new MaskBlurWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter, ConfigurationManager.APP.getMaskConfiguration().getColor());
        FORK_JOIN_POOL.execute(task);

        return writableImage;
    }

    public static WritableImage runMaskToHeightMapTask(Image image) {
        LOGGER.debug("Run mask to height map");

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage(pixelReader, (int) image.getWidth(), (int) image.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new MaskToHeightMapWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter);
        FORK_JOIN_POOL.execute(task);

        return writableImage;
    }

    public static WritableImage runMaskResetTask(Image image) {
        LOGGER.debug("Run mask reset");

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage(pixelReader, (int) image.getWidth(), (int) image.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new MaskResetWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter, ConfigurationManager.APP.getMaskConfiguration().getColor());
        FORK_JOIN_POOL.execute(task);

        return writableImage;
    }

    public static WritableImage runMaskFromHeightMapTask(Image image) {
        LOGGER.debug("Run mask from height map");

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage(pixelReader, (int) image.getWidth(), (int) image.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new MaskFromHeightMapWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter, ConfigurationManager.APP.getMaskConfiguration().getColor());
        FORK_JOIN_POOL.execute(task);

        return writableImage;
    }

    public static WritableImage runMaskInverterTask(Image image) {
        LOGGER.debug("Run mask inverter");

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage(pixelReader, (int) image.getWidth(), (int) image.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new MaskInverterWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter, ConfigurationManager.APP.getMaskConfiguration().getColor());
        FORK_JOIN_POOL.execute(task);

        return writableImage;
    }

    public static WritableImage runMaskSpectrumSplitterTask(Image image) {
        LOGGER.debug("Run mask spectrum splitter");

        final PixelReader pixelReader = image.getPixelReader();
        final WritableImage writableImage = new WritableImage(pixelReader, (int) image.getWidth(), (int) image.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new MaskSpectrumSplitterWriterTask((int) image.getWidth(), (int) image.getHeight(),
                pixelReader, pixelWriter);
        FORK_JOIN_POOL.execute(task);

        return writableImage;
    }

    public static WritableImage runImageMergingTask(Image sourceImage, Image targetImage, Image maskImage, double opacity) {
        LOGGER.trace(">>> Run image merging");

        final PixelReader sourcePixelReader = sourceImage.getPixelReader();
        final PixelReader targetPixelReader = targetImage.getPixelReader();
        final PixelReader maskPixelReader = maskImage == null ? null : maskImage.getPixelReader();
        final WritableImage writableImage = new WritableImage(sourcePixelReader, (int) sourceImage.getWidth(), (int) sourceImage.getHeight());
        final PixelWriter pixelWriter = writableImage.getPixelWriter();

        final ImageWriterTask task = new ImageMergingWriterTask((int) sourceImage.getWidth(), (int) sourceImage.getHeight(),
                sourcePixelReader, pixelWriter, targetPixelReader, maskPixelReader, opacity);
        FORK_JOIN_POOL.execute(task);

        return writableImage;
    }

    private ImageWriterTaskRunner() {
    }
}
