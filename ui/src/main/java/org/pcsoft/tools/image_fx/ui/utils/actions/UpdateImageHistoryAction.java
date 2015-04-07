package org.pcsoft.tools.image_fx.ui.utils.actions;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by pfeifchr on 21.08.2014.
 */
public final class UpdateImageHistoryAction extends AbstractImageHistoryAction {

    private final String actionName;
    private final Image newImage;
    private final Image lastImage;

    public UpdateImageHistoryAction(ImageView imgImage, String actionName, Image newImage, Image lastImage) {
        super(imgImage);
        this.actionName = actionName;
        this.newImage = newImage;
        this.lastImage = lastImage;
    }

    @Override
    public void undo() {
        imgImage.setImage(lastImage);
    }

    @Override
    public void redo() {
        imgImage.setImage(newImage);
    }

    @Override
    public String getActionName() {
        return actionName;
    }
}
