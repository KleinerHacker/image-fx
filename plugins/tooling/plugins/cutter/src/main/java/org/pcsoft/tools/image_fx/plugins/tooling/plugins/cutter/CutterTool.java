package org.pcsoft.tools.image_fx.plugins.tooling.plugins.cutter;

import org.pcsoft.tools.image_fx.plugins.common.exceptions.ImageFXPluginExecutionException;
import org.pcsoft.tools.image_fx.plugins.tooling.interfaces.ImageFXTooling;
import org.pcsoft.tools.image_fx.plugins.tooling.interfaces.ImageFXToolingContext;
import org.pcsoft.tools.image_fx.plugins.tooling.interfaces.annotations.ImageFXToolingItem;
import org.pcsoft.tools.image_fx.plugins.tooling.plugins.cutter.ui.dialogs.MainDialogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Christoph on 09.08.2014.
 */
@ImageFXToolingItem(title = "Cutter", group = "Cutting", keyCombination = "CTRL+SHIFT+C")
public final class CutterTool implements ImageFXTooling {

    private static final Logger LOGGER = LoggerFactory.getLogger(CutterTool.class);

    @Override
    public void onExecute(ImageFXToolingContext context) throws ImageFXPluginExecutionException{
        LOGGER.info("Execute cutter tool...");
        MainDialogFactory.show();
    }
}
