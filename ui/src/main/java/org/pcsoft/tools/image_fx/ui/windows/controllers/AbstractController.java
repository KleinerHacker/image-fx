package org.pcsoft.tools.image_fx.ui.windows.controllers;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * Created by pfeifchr on 13.08.2014.
 */
abstract class AbstractController implements Initializable {

    protected final Stage parent;

    protected AbstractController(Stage parent) {
        this.parent = parent;
    }
}
