package org.pcsoft.tools.image_fx.plugins.tooling.interfaces;

import org.pcsoft.tools.image_fx.plugins.common.ImageFXPlugin;
import org.pcsoft.tools.image_fx.plugins.common.exceptions.ImageFXPluginExecutionException;

/**
 * Created by Christoph on 09.08.2014.
 */
public interface ImageFXTooling extends ImageFXPlugin {

    void onExecute(ImageFXToolingContext context) throws ImageFXPluginExecutionException;

}
