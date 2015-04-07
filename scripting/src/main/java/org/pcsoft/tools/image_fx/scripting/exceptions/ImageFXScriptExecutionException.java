package org.pcsoft.tools.image_fx.scripting.exceptions;

/**
 * Created by Christoph on 17.06.2014.
 */
public class ImageFXScriptExecutionException extends ImageFXScriptException {

    public ImageFXScriptExecutionException() {
    }

    public ImageFXScriptExecutionException(String message) {
        super(message);
    }

    public ImageFXScriptExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageFXScriptExecutionException(Throwable cause) {
        super(cause);
    }

    public ImageFXScriptExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
