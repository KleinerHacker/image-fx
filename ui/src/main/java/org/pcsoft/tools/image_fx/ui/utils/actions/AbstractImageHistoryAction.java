package org.pcsoft.tools.image_fx.ui.utils.actions;

import javafx.scene.image.ImageView;
import org.pcsoft.tools.image_fx.ui.utils.HistoryStack;

/**
 * Created by pfeifchr on 21.08.2014.
 */
public abstract class AbstractImageHistoryAction implements HistoryStack.Action {

    protected final ImageView imgImage;

    protected AbstractImageHistoryAction(ImageView imgImage) {
        this.imgImage = imgImage;
    }
}
