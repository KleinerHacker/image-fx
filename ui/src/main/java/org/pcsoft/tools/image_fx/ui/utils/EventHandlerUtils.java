package org.pcsoft.tools.image_fx.ui.utils;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * Created by pfeifchr on 14.08.2014.
 */
public final class EventHandlerUtils {

    public static final class TextFieldHandlers {
        public static EventHandler<KeyEvent> createNumericIntegerInputRestrictionHandler(final int maxLength) {
            return event -> {
                if (!event.getCharacter().matches("[0-9]+") || ((TextField)event.getSource()).getText().length() >= maxLength) {
                    event.consume();
                }
            };
        }

        private TextFieldHandlers() {
        }
    }

    private EventHandlerUtils() {
    }
}
