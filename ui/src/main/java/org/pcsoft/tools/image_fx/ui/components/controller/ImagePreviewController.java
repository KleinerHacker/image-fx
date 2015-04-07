package org.pcsoft.tools.image_fx.ui.components.controller;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.pcsoft.tools.image_fx.scripting.threading.JavaScriptImageWriterTaskRunner;
import org.pcsoft.tools.image_fx.scripting.types.image.AbstractImageScript;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayer;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by pfeifchr on 18.06.2014.
 */
public class ImagePreviewController implements Initializable {

    public static interface OnImageApplyListener {
        void onUseEffect(ImageScriptElement scriptInfo, ImageScriptVariant variantInfo);

        void onUseEffect(ImageScriptLayer scriptLayer);

        void onPrepareEffect(ImageScriptElement scriptInfo);

        void onPrepareEffect(ImageScriptLayer scriptLayer);
    }

    @FXML
    public BorderPane pnlWait;
    @FXML
    private Pagination pgnImages;
    @FXML
    private Label lblTitle;
    private List<ImageView> imgPreviewList = new ArrayList<>();

    private final AbstractImageScript scripting;
    private Image image = null;

    private OnImageApplyListener onImageApplyListener;

    public ImagePreviewController(AbstractImageScript scripting) {
        this.scripting = scripting;
    }

    public void setOnImageApplyListener(OnImageApplyListener onImageApplyListener) {
        this.onImageApplyListener = onImageApplyListener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (scripting instanceof ImageScriptElement) {
            final ImageScriptElement scriptInfo = (ImageScriptElement) scripting;
            imgPreviewList.addAll(scriptInfo.getVariantMap().values().stream().map(this::createImageView).collect(Collectors.toList()));
            if (imgPreviewList.isEmpty()) { //No variants found
                imgPreviewList.add(createImageView(null));
            }
        } else if (scripting instanceof ImageScriptLayer) {
            imgPreviewList.add(createImageView());
        } else
            throw new RuntimeException();

        pgnImages.setPageCount(imgPreviewList.size());
        pgnImages.setCurrentPageIndex(0);
        pgnImages.setMaxPageIndicatorCount(Math.min(imgPreviewList.size(), 7));
        pgnImages.setPageFactory(pos -> imgPreviewList.get(pos));
    }

    public String getTitle() {
        return lblTitle.getText();
    }

    public void setTitle(String title) {
        lblTitle.setText(title);
    }

    public Image getImage() {
        return image;
    }

    public void showWaiter() {
        pnlWait.setVisible(true);

        final FadeTransition transition = new FadeTransition(new Duration(300), pnlWait);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }

    public void hideWaiter() {
        final FadeTransition transition = new FadeTransition(new Duration(300), pnlWait);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setOnFinished(actionEvent -> Platform.runLater(() -> pnlWait.setVisible(false)));
        transition.play();
    }

    /**
     * UI Thread Safe: Can called from another thread
     *
     * @param image
     */
    public void setImage(Image image) {
        this.image = image;

        for (final ImageView imageView : imgPreviewList) {
            if (scripting instanceof ImageScriptElement) {
                final WritableImage writableImage = JavaScriptImageWriterTaskRunner.runJavaScriptTask(
                        image, null, 1, (ImageScriptElement) scripting, (ImageScriptVariant) imageView.getUserData(), null
                );
                Platform.runLater(() -> imageView.setImage(writableImage));
            } else if (scripting instanceof ImageScriptLayer) {
                final ImageScriptLayer scriptLayer = (ImageScriptLayer) scripting;
                final Image resultImage = JavaScriptImageWriterTaskRunner.runJavaScriptTask(image, null, 1, scriptLayer.getScriptHolderList(), null);

                Platform.runLater(() -> imageView.setImage(resultImage));
            } else
                throw new RuntimeException();
        }
    }

    @FXML
    protected void onPrepare(ActionEvent actionEvent) {
        if (onImageApplyListener != null) {
            if (scripting instanceof ImageScriptElement) {
                onImageApplyListener.onPrepareEffect((ImageScriptElement) scripting);
            } else if (scripting instanceof ImageScriptLayer) {
                onImageApplyListener.onPrepareEffect((ImageScriptLayer) scripting);
            } else
                throw new RuntimeException();
        }
    }

    private ImageView createImageView(final ImageScriptVariant variantInfo) {
        final ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setUserData(variantInfo);
        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseClicked(mouseEvent -> {
            if (onImageApplyListener != null) {
                onImageApplyListener.onUseEffect((ImageScriptElement) scripting, variantInfo);
            }
        });

        return imageView;
    }

    private ImageView createImageView() {
        final ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setUserData(scripting);
        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseClicked(mouseEvent -> {
            if (onImageApplyListener != null) {
                onImageApplyListener.onUseEffect((ImageScriptLayer) scripting);
            }
        });

        return imageView;
    }
}
