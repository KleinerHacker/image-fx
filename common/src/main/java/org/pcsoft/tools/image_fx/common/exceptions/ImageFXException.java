package org.pcsoft.tools.image_fx.common.exceptions;

/**
 * Created by Christoph on 17.06.2014.
 */
public abstract class ImageFXException extends Exception {

    protected ImageFXException() {
    }

    protected ImageFXException(String message) {
        super(message);
    }

    protected ImageFXException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ImageFXException(Throwable cause) {
        super(cause);
    }

    protected ImageFXException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
