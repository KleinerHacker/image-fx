package org.pcsoft.tools.image_fx.core.exceptions;

import org.pcsoft.tools.image_fx.common.exceptions.ImageFXException;

/**
 * Created by pfeifchr on 18.06.2014.
 */
public class ImageFXColorCalculatorException extends ImageFXException {

    public ImageFXColorCalculatorException() {
    }

    public ImageFXColorCalculatorException(String message) {
        super(message);
    }

    public ImageFXColorCalculatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageFXColorCalculatorException(Throwable cause) {
        super(cause);
    }

    public ImageFXColorCalculatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
