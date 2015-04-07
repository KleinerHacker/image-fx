package org.pcsoft.tools.image_fx.ui.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.pcsoft.tools.image_fx.core.threading.ImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.ui.Constants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Christoph on 25.06.2014.
 */
public final class ImageUtils {

    public static Image createExamplePreviewThumbnail() {
        return createPreviewThumbnail(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/example.png"));
    }

    public static Image createPreviewThumbnail(InputStream in) {
        return new Image(in, Constants.THUMB_WIDTH, Constants.THUMB_HEIGHT, false, true);
    }

    public static Image createPreviewThumbnail(File file) throws IOException {
        try (FileInputStream in = new FileInputStream(file)) {
            return createPreviewThumbnail(in);
        }
    }

    public static Image createPreviewThumbnail(Image imageIn) {
        final byte[] bytes = imageToByteArray(imageIn);
        try (final ByteArrayInputStream bin = new ByteArrayInputStream(bytes)) {
            return createPreviewThumbnail(bin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Image createMaskFromHeightMap(InputStream hm, int width, int height) {
        return ImageWriterTaskRunner.runMaskFromHeightMapTask(
                new Image(hm, width, height, false, true)
        );
    }

    public static byte[] imageToByteArray(Image image) {
        try (final ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
            final BufferedImage bufferedImage = new BufferedImage(
                    (int) image.getWidth(),
                    (int) image.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            SwingFXUtils.fromFXImage(image, bufferedImage);
            ImageIO.write(bufferedImage, "png", bout);

            return bout.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ImageUtils() {
    }
}
