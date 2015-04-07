package org.pcsoft.tools.image_fx.ui.components;

import org.pcsoft.tools.image_fx.scripting.types.image.AbstractImageScript;
import org.pcsoft.tools.image_fx.ui.components.controller.ImagePreviewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Created by pfeifchr on 18.06.2014.
 */
public final class ImagePreviewFactory {

    public static final class ImagePreview {
        private ImagePreviewController controller;
        private final boolean needRebuild;

        public ImagePreview(boolean needRebuild) {
            this.needRebuild = needRebuild;
        }

        public String getTitle() {
            return controller.getTitle();
        }

        public void setTitle(String title) {
            controller.setTitle(title);
        }

        public Image getImage() {
            return controller.getImage();
        }

        /**
         * UI Thread Safe: Can called from another thread
         * @param image
         */
        public void setImage(Image image) {
            controller.setImage(image);
        }

        public boolean isNeedRebuild() {
            return needRebuild;
        }

        public void setOnEffectListener(ImagePreviewController.OnImageApplyListener onImageApplyListener) {
            controller.setOnImageApplyListener(onImageApplyListener);
        }

        public void showWaiter() {
            controller.showWaiter();
        }

        public void hideWaiter() {
            controller.hideWaiter();
        }
    }

    /**
     * Creates and add the image preview component to root pane
     * @param scripting Script element to create preview for
     * @param root
     * @return
     */
    public static ImagePreview create(final AbstractImageScript scripting, Pane root, final boolean needRebuild) {
        final ImagePreview imagePreview = new ImagePreview(needRebuild);

        final FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(aClass -> {
            final ImagePreviewController controller = new ImagePreviewController(scripting);
            imagePreview.controller = controller;

            return controller;
        });
        try {
            final Pane pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/component.image.preview.fxml"));
            root.getChildren().add(pane);

            imagePreview.setTitle(scripting.getName());
            return imagePreview;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ImagePreviewFactory() {
    }
}
