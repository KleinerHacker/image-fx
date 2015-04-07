package org.pcsoft.tools.image_fx.plugins.common.exceptions;

import org.pcsoft.tools.image_fx.common.exceptions.ImageFXException;

/**
 * Created by pfeifchr on 21.08.2014.
 */
public abstract class ImageFXPluginException extends ImageFXException {

    public ImageFXPluginException() {
    }

    public ImageFXPluginException(String message) {
        super(message);
    }

    public ImageFXPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageFXPluginException(Throwable cause) {
        super(cause);
    }

    public ImageFXPluginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
