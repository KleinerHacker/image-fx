package org.pcsoft.tools.image_fx.ui.items.tree;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElementGroup;
import org.pcsoft.tools.image_fx.ui.components.addons.tree.ImageScriptElementTreeItem;
import org.pcsoft.tools.image_fx.ui.items.tree.controller.ScriptTreeItemController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by pfeifchr on 25.07.2014.
 */
public final class ScriptTreeItemFactory {

    public static void useForTree(Image image, TreeView<Object> treeView) {
        treeView.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {
            @Override
            public TreeCell<Object> call(TreeView<Object> treeView) {
                return new TreeCell<Object>() {
                    @Override
                    protected void updateItem(Object o, boolean b) {
                        super.updateItem(o, b);

                        if (o == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            if (o instanceof ImageScriptElement.ScriptType) {
                                final ImageScriptElement.ScriptType scriptType = (ImageScriptElement.ScriptType) o;

                                setText(scriptType.name());
                                switch (scriptType) {
                                    case Effect:
                                        setGraphic(new ImageView(new Image(
                                                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                                                        "icons/effects16.png"
                                                )
                                        )));
                                        break;
                                    case Renderer:
                                        setGraphic(new ImageView(new Image(
                                                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                                                        "icons/renderers16.png"
                                                )
                                        )));
                                        break;
                                    default:
                                        throw new RuntimeException();
                                }
                            } else if (o instanceof ImageScriptElementGroup) {
                                final ImageScriptElementGroup scriptElementGroup = (ImageScriptElementGroup) o;

                                setText(scriptElementGroup.getName());
                                if (scriptElementGroup.hasGraphic()) {
                                    setGraphic(new ImageView(new Image(new ByteArrayInputStream(
                                            scriptElementGroup.getGraphic()))));
                                }
                            } else if (o instanceof ImageScriptElementTreeItem.Holder) {
                                final ImageScriptElementTreeItem.Holder holder = (ImageScriptElementTreeItem.Holder) o;

                                final ScriptTreeItemController controller = new ScriptTreeItemController(image,
                                        holder.getScriptElement(), holder.getScriptVariant());

                                final FXMLLoader loader = new FXMLLoader();
                                loader.setControllerFactory(param -> controller);

                                final Pane pane;
                                try {
                                    pane = loader.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fxml/item.tree.script.fxml"));
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

    private ScriptTreeItemFactory() {
    }
}
