package org.pcsoft.tools.image_fx.ui.items.list;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptLayerProperty;
import org.pcsoft.tools.image_fx.ui.items.list.controller.LayerFragmentListItemController;

import java.io.IOException;

/**
 * Created by Christoph on 05.07.2014.
 */
public final class LayerFragmentListItemFactory {

    public static void useForList(Image image, ListView<ImageScriptLayerProperty.ImageScriptHolderProperty> lst) {
        lst.setCellFactory(param -> new ListCell<ImageScriptLayerProperty.ImageScriptHolderProperty>() {
            @Override
            protected void updateItem(ImageScriptLayerProperty.ImageScriptHolderProperty item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    final LayerFragmentListItemController controller = new LayerFragmentListItemController(image,
                            item.getScriptInfo(), item.getVariantInfo());

                    final FXMLLoader loader = new FXMLLoader();
                    loader.setControllerFactory(param -> controller);

                    final Pane pane;
                    try {
                        pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/item.list.layer.fragment.fxml"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    setText(null);
                    setGraphic(pane);
                }
            }
        });
    }

    private LayerFragmentListItemFactory() {
    }
}
