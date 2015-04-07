package org.pcsoft.tools.image_fx.ui.building;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import org.pcsoft.tools.image_fx.common.listeners.ProgressListener;
import org.pcsoft.tools.image_fx.scripting.XmlEffectScriptManager;
import org.pcsoft.tools.image_fx.scripting.XmlRendererScriptManager;
import org.pcsoft.tools.image_fx.scripting.XmlScriptLayerManager;
import org.pcsoft.tools.image_fx.scripting.types.image.AbstractImageScript;
import org.pcsoft.tools.image_fx.scripting.types.image.AbstractImageScriptGroup;
import org.pcsoft.tools.image_fx.ui.components.controller.ImagePreviewController;
import org.pcsoft.tools.image_fx.ui.managing.XmlScriptPreviewManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Created by Christoph on 17.06.2014.
 */
public final class MainWindowUIBuilder {

    public static void buildInitialPreviews(Accordion effectAccordion, Accordion rendererAccordion, Accordion layerAccordion, ProgressBar pbWork, Label lblWork, ImagePreviewController.OnImageApplyListener imageApplyListener) {
        buildFor(effectAccordion, imageApplyListener, XmlEffectScriptManager.getInstance().extractElementGroupList(), XmlEffectScriptManager.getInstance().getScriptInfoList());
        buildFor(rendererAccordion, imageApplyListener, XmlRendererScriptManager.getInstance().extractElementGroupList(), XmlRendererScriptManager.getInstance().getScriptInfoList());
        buildFor(layerAccordion, imageApplyListener, XmlScriptLayerManager.getInstance().extractGroups(), XmlScriptLayerManager.getInstance().getScriptLayerList());

        pbWork.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        pbWork.setVisible(true);
        lblWork.setText("Build initialize previews");
        lblWork.setVisible(true);

        effectAccordion.setDisable(true);
        rendererAccordion.setDisable(true);
        layerAccordion.setDisable(true);

        Executors.newCachedThreadPool().submit(() -> {
            try {
                XmlScriptPreviewManager.updateAllPreviewsFromImage(new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/example.png")), new ProgressListener() {
                    @Override
                    public void onStartProgress() {

                    }

                    @Override
                    public void onProgressUpdate(double progress) {
                        Platform.runLater(() -> pbWork.setProgress(progress));
                    }

                    @Override
                    public void onFinishProgress() {
                        Platform.runLater(() -> {
                            pbWork.setVisible(false);
                            lblWork.setVisible(false);

                            effectAccordion.setDisable(false);
                            rendererAccordion.setDisable(false);
                            layerAccordion.setDisable(false);
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void buildFor(Accordion accordion, ImagePreviewController.OnImageApplyListener imageApplyListener, List<? extends AbstractImageScriptGroup> groupInfoList, List<? extends AbstractImageScript> scriptInfoList) {
        final Map<String, VBox> groupPaneMap = buildTitledPanesFor(accordion, groupInfoList);

        for (final AbstractImageScript scripting : scriptInfoList) {
            final VBox pane = groupPaneMap.get(scripting.getGroupInfo().getId());
            XmlScriptPreviewManager.createPreview(scripting, pane, true, imageApplyListener);
        }
    }

    private static Map<String, VBox> buildTitledPanesFor(Accordion accordion, List<? extends AbstractImageScriptGroup> groupInfoList) {
        final Map<String, VBox> groupPaneMap = new HashMap<>();
        for (AbstractImageScriptGroup groupInfo : groupInfoList) {
            final VBox pane = new VBox();
            final TitledPane tp = new TitledPane(groupInfo.getName(), new ScrollPane(pane));
            accordion.getPanes().add(tp);

            groupPaneMap.put(groupInfo.getId(), pane);
        }
        return groupPaneMap;
    }

    private MainWindowUIBuilder() {
    }
}
