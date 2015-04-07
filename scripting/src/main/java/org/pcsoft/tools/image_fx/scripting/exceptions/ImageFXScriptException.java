package org.pcsoft.tools.image_fx.scripting.exceptions;

import org.pcsoft.tools.image_fx.common.exceptions.ImageFXException;

/**
 * Created by Christoph on 17.06.2014.
 */
public abstract class ImageFXScriptException extends ImageFXException {

    protected ImageFXScriptException() {
    }

    protected ImageFXScriptException(String message) {
        super(message);
    }

    protected ImageFXScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ImageFXScriptException(Throwable cause) {
        super(cause);
    }

    protected ImageFXScriptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
