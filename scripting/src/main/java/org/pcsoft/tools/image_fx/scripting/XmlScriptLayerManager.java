package org.pcsoft.tools.image_fx.scripting;

import org.pcsoft.tools.image_fx.common.IdUtils;
import org.pcsoft.tools.image_fx.core.configuration.ConfigurationManager;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptLoadingException;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayer;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptLayerGroup;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptParameterReference;
import org.pcsoft.tools.image_fx.scripting.types.image.prop.ImageScriptLayerProperty;
import org.pcsoft.tools.image_fx.scripting.xml.XLayer;
import org.pcsoft.tools.image_fx.scripting.xml.XLayerGroup;
import org.pcsoft.tools.image_fx.scripting.xml.XLayers;
import org.pcsoft.tools.image_fx.scripting.xml.XParameterReference;
import org.pcsoft.tools.image_fx.scripting.xml.XScriptElement;
import org.pcsoft.tools.image_fx.scripting.xml.XScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Christoph on 20.07.2014.
 */
public final class XmlScriptLayerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlScriptLayerManager.class);
    private static XmlScriptLayerManager instance = null;

    public static XmlScriptLayerManager getInstance() {
        if (instance == null) {
            instance = new XmlScriptLayerManager();
        }

        return instance;
    }

    private final List<ImageScriptLayer> scriptLayerList = new ArrayList<>();
    private final Path addOnPath, userDataFile;

    private XmlScriptLayerManager() {
        LOGGER.info("Initialize script layer maanger...");

        addOnPath = ConfigurationManager.LAYERS.getDirectory();
        userDataFile = ConfigurationManager.LAYERS.getUserDataFile();

        LOGGER.info(">>> OK");
    }

    public void loadElements() throws ImageFXScriptLoadingException {
        LOGGER.debug("Load script layer elements...");
        scriptLayerList.clear();

        final XLayers xLayers = JAXB.unmarshal(Thread.currentThread().getContextClassLoader().getResourceAsStream("scripts/layers.xml"), XLayers.class);
        for (XLayerGroup xLayerGroup : xLayers.getLayerGroup()) {
            scriptLayerList.addAll(xLayerGroup.getLayer().stream().map(xLayer -> new ImageScriptLayer(xLayer, xLayerGroup, true)).collect(Collectors.toList()));
        }

        //AddOn
        try {
            if (addOnPath.toAbsolutePath().toFile().exists()) {
                Files.walkFileTree(addOnPath.toAbsolutePath(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.toFile().getAbsolutePath().endsWith(".xml")) {
                            final List<XLayerGroup> addOnScriptLayerGroupList = loadFromAddOnFile(file.toFile());
                            for (XLayerGroup xLayerGroup : addOnScriptLayerGroupList) {
                                scriptLayerList.addAll(xLayerGroup.getLayer().stream().map(xLayer -> new ImageScriptLayer(xLayer, xLayerGroup, true)).collect(Collectors.toList()));
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (IOException e) {
            LOGGER.error("Cannot load additional scripts layers!", e);
            throw new ImageFXScriptLoadingException("Cannot load additional scripts layers!", e);
        }

        //UserData
        if (userDataFile.toFile().exists()) {
            final List<XLayerGroup> layerGroupList = JAXB.unmarshal(userDataFile.toFile(), XLayers.class).getLayerGroup();
            for (XLayerGroup xLayerGroup : layerGroupList) {
                scriptLayerList.addAll(xLayerGroup.getLayer().stream().map(xLayer -> new ImageScriptLayer(xLayer, xLayerGroup, false)).collect(Collectors.toList()));
            }
        }
    }

    public List<ImageScriptLayer> getScriptLayerList() {
        return scriptLayerList;
    }

    public List<ImageScriptLayerGroup> extractGroups() {
        final List<ImageScriptLayerGroup> groupInfoList = new ArrayList<>();
        for (ImageScriptLayer scriptLayer : scriptLayerList) {
            if (!groupInfoList.contains(scriptLayer.getGroupInfo()))
                groupInfoList.add((ImageScriptLayerGroup)scriptLayer.getGroupInfo());
        }

        return groupInfoList;
    }

    public ImageScriptLayerGroup findGroupById(String id) {
        for (ImageScriptLayerGroup layerGroup : extractGroups()) {
            if (layerGroup.getId().equals(id))
                return layerGroup;
        }

        return null;
    }

    public void storeLayer(String name, String groupId, String groupName, List<ImageScriptLayer.ImageScriptHolder> imageScriptHolderList) {
        LOGGER.debug("Store script layer...");

        final ImageScriptLayerGroup layerGroup;
        if (groupId != null) {
            final ImageScriptLayerGroup groupById = findGroupById(groupId);
            if (groupById == null) {
                layerGroup = new ImageScriptLayerGroup(IdUtils.generateId(), groupName, null);//TODO:Graphic
            } else {
                layerGroup = groupById;
            }
        } else {
            layerGroup = new ImageScriptLayerGroup(IdUtils.generateId(), groupName, null);//TODO:Graphic
        }

        final ImageScriptLayer imageScriptLayer = new ImageScriptLayer(IdUtils.generateId(), name, layerGroup,
                imageScriptHolderList.stream().map(ImageScriptLayerProperty.ImageScriptHolderProperty::new).collect(Collectors.toList()));

        saveToFile(imageScriptLayer);
    }

    private List<XLayerGroup> loadFromAddOnFile(File file) {
        return JAXB.unmarshal(file, XLayers.class).getLayerGroup();
    }

    private void saveToFile(ImageScriptLayer imageScriptLayer) {
        LOGGER.debug("Rebuild (save) script layer user data file");

        final XLayers xLayers;
        if (userDataFile.toFile().exists()) {
            xLayers = JAXB.unmarshal(userDataFile.toFile(), XLayers.class);
        } else {
            xLayers = new XLayers();
        }

        XLayerGroup xLayerGroup = null;
        for (XLayerGroup group : xLayers.getLayerGroup()) {
            if (group.getId().equals(imageScriptLayer.getGroupInfo().getId())) {
                xLayerGroup = group;
                break;
            }
        }
        if (xLayerGroup == null) {
            xLayerGroup = new XLayerGroup();
            xLayerGroup.setId(imageScriptLayer.getGroupInfo().getId());
            xLayerGroup.setName(imageScriptLayer.getGroupInfo().getName());
            xLayerGroup.setGraphic(imageScriptLayer.getGroupInfo().getGraphic());

            xLayers.getLayerGroup().add(xLayerGroup);
        }

        final XLayer xLayer = new XLayer();
        xLayer.setId(imageScriptLayer.getId());
        xLayer.setName(imageScriptLayer.getName());
        for (final ImageScriptLayer.ImageScriptHolder imageScriptHolder : imageScriptLayer.getScriptHolderList()) {
            final XScriptElement xScriptElement = new XScriptElement();
            xScriptElement.setScriptRef(imageScriptHolder.getScriptInfo().getId());
            xScriptElement.setGroupRef(imageScriptHolder.getScriptInfo().getGroupInfo().getId());
            switch (imageScriptHolder.getScriptInfo().getScriptType()) {
                case Effect:
                    xScriptElement.setType(XScriptType.EFFECT);
                    break;
                case Renderer:
                    xScriptElement.setType(XScriptType.RENDERER);
                    break;
                default:
                    throw new RuntimeException();
            }
            xScriptElement.setMask(imageScriptHolder.getMask());
            xScriptElement.setOpacity(imageScriptHolder.getOpacity());

            for (final ImageScriptParameterReference parameterReference : imageScriptHolder.getVariantInfo().getParameterReferenceMap().values()) {
                final XParameterReference xParameterReference = new XParameterReference();
                xParameterReference.setRef(parameterReference.getReference());
                xParameterReference.setValue(parameterReference.getPlainValue());

                xScriptElement.getParameterReference().add(xParameterReference);
            }

            xLayer.getScriptElement().add(xScriptElement);
        }

        xLayerGroup.getLayer().add(xLayer);

        JAXB.marshal(xLayers, userDataFile.toFile());
    }
}
