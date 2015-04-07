package org.pcsoft.tools.image_fx.plugins.common.exceptions;

/**
 * Created by pfeifchr on 21.08.2014.
 */
public class ImageFXPluginInitializationException extends ImageFXPluginException {

    public ImageFXPluginInitializationException() {
    }

    public ImageFXPluginInitializationException(String message) {
        super(message);
    }

    public ImageFXPluginInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageFXPluginInitializationException(Throwable cause) {
        super(cause);
    }

    public ImageFXPluginInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
