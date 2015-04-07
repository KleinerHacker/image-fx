package org.pcsoft.tools.image_fx.scripting.exceptions;

/**
 * Created by pfeifchr on 19.06.2014.
 */
public class ImageFXScriptLoadingException extends ImageFXScriptException {

    public ImageFXScriptLoadingException() {
    }

    public ImageFXScriptLoadingException(String message) {
        super(message);
    }

    public ImageFXScriptLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageFXScriptLoadingException(Throwable cause) {
        super(cause);
    }

    public ImageFXScriptLoadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
