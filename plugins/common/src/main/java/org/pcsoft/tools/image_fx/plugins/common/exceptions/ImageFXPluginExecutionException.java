package org.pcsoft.tools.image_fx.plugins.common.exceptions;

/**
 * Created by pfeifchr on 21.08.2014.
 */
public class ImageFXPluginExecutionException extends ImageFXPluginException {

    public ImageFXPluginExecutionException() {
    }

    public ImageFXPluginExecutionException(String message) {
        super(message);
    }

    public ImageFXPluginExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageFXPluginExecutionException(Throwable cause) {
        super(cause);
    }

    public ImageFXPluginExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
