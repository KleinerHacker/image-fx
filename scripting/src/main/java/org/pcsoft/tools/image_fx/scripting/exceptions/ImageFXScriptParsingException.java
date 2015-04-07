package org.pcsoft.tools.image_fx.scripting.exceptions;

/**
 * Created by Christoph on 17.06.2014.
 */
public class ImageFXScriptParsingException extends ImageFXScriptException {

    public ImageFXScriptParsingException() {
    }

    public ImageFXScriptParsingException(String message) {
        super(message);
    }

    public ImageFXScriptParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageFXScriptParsingException(Throwable cause) {
        super(cause);
    }

    public ImageFXScriptParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
