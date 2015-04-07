package org.pcsoft.tools.image_fx.ui.items.tree;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayer;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayerGroup;
import org.pcsoft.tools.image_fx.ui.items.tree.controller.LayerTreeItemController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by pfeifchr on 25.07.2014.
 */
public final class LayerTreeItemFactory {

    public static void useForTree(Image image, TreeView<Object> list) {
        list.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {
            @Override
            public TreeCell<Object> call(TreeView<Object> objectTreeView) {
                return new TreeCell<Object>() {
                    @Override
                    protected void updateItem(Object o, boolean b) {
                        super.updateItem(o, b);

                        if (o == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            if (o instanceof ImageScriptLayerGroup) {
                                final ImageScriptLayerGroup scriptLayerGroup = (ImageScriptLayerGroup) o;

                                setText(scriptLayerGroup.getName());
                                if (scriptLayerGroup.hasGraphic()) {
                                    setGraphic(new ImageView(new Image(new ByteArrayInputStream(
                                            scriptLayerGroup.getGraphic()))));
                                }
                                setGraphic(null);
                            } else if (o instanceof ImageScriptLayer) {
                                final LayerTreeItemController controller = new LayerTreeItemController(image,
                                        (ImageScriptLayer) o);

                                final FXMLLoader loader = new FXMLLoader();
                                loader.setControllerFactory(param -> controller);

                                final Pane pane;
                                try {
                                    pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/item.tree.layer.fxml"));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                setText(null);
                                setGraphic(pane);
                            }
                        }
                    }
                };
            }
        });
    }

    private LayerTreeItemFactory() {
    }
}
