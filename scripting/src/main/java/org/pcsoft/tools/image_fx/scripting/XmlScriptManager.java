package org.pcsoft.tools.image_fx.scripting;

import org.pcsoft.tools.image_fx.common.listeners.ProgressListener;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptException;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptLoadingException;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptParsingException;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElement;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptElementGroup;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptParameter;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptParameterGroup;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.scripting.utils.ObjectConverterUtils;
import org.pcsoft.tools.image_fx.scripting.xml.XParameterDefinition;
import org.pcsoft.tools.image_fx.scripting.xml.XParameterGroup;
import org.pcsoft.tools.image_fx.scripting.xml.XScript;
import org.pcsoft.tools.image_fx.scripting.xml.XScriptGroup;
import org.pcsoft.tools.image_fx.scripting.xml.XScriptParameterDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christoph on 17.06.2014.
 */
public abstract class XmlScriptManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlScriptManager.class);

    private final List<ImageScriptElement> scriptInfoList = new ArrayList<>();
    protected final ImageScriptElement.ScriptType scriptType;
    protected final Path addOnPath;

    protected XmlScriptManager(ImageScriptElement.ScriptType scriptType, Path addOnPath) {
        LOGGER.info("Initialize script manager '" + getClass().getSimpleName() + "'...");

        this.scriptType = scriptType;
        this.addOnPath = addOnPath;

        LOGGER.info(">>> OK");
    }

    public void loadElements(final ProgressListener listener) throws ImageFXScriptException {
        LOGGER.debug("Load script elements ('" + getClass().getSimpleName() + "')...");

        if (listener != null) {
            listener.onStartProgress();
        }

        loadVariants();

        try {
            scriptInfoList.clear();
            scriptInfoList.addAll(buildElementList());

            int counter = 0;
            for (final ImageScriptElement element : scriptInfoList) {
                addToScriptManager(element);
                counter++;

                if (listener != null) {
                    listener.onProgressUpdate((double) counter / (double) scriptInfoList.size());
                }
            }
        } catch (Exception e) {
            throw new ImageFXScriptLoadingException(e);
        } finally {
            if (listener != null) {
                listener.onFinishProgress();
            }
        }
    }

    protected abstract void loadVariants() throws ImageFXScriptException;

    public abstract Map<String, ImageScriptVariant> getVariantMap(String group, String script);

    public final void storeVariant(String group, String script, String name, ImageScriptVariant variantInfo) {
        LOGGER.debug("Store variant...");

        final ImageScriptVariant imageScriptVariant = _storeVariant(group, script, name, variantInfo);
        for (ImageScriptElement scriptInfo : scriptInfoList) {
            if (scriptInfo.getGroupInfo().getId().equals(group) &&
                    scriptInfo.getId().equals(script)) {
                scriptInfo.getVariantMap().put(imageScriptVariant.getId(), imageScriptVariant);
            }
        }
    }

    protected abstract ImageScriptVariant _storeVariant(String group, String script, String name, ImageScriptVariant variantInfo);

    public List<ImageScriptElement> getScriptInfoList() {
        return scriptInfoList;
    }

    public ImageScriptElement getElementBy(String scriptId, String scriptGroupId) {
        return scriptInfoList.stream().filter(
                obj -> obj.getId().equals(scriptId) && obj.getGroupInfo().getId().equals(scriptGroupId)
        ).findFirst().orElse(null);
    }

    public List<ImageScriptElementGroup> extractElementGroupList() {
        final List<ImageScriptElementGroup> groups = new ArrayList<>();
        for (final ImageScriptElement scriptInfo : scriptInfoList) {
            if (!groups.contains(scriptInfo.getGroupInfo()))
                groups.add((ImageScriptElementGroup) scriptInfo.getGroupInfo());
        }

        Collections.sort(groups, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        return groups;
    }

    public static Map<String, Object> buildScriptParameterValueMap(Map<String, ImageScriptParameter> parameterInfoMap, ImageScriptVariant variantInfo) throws ImageFXScriptParsingException {
        return buildScriptParameterValueMap(parameterInfoMap.values(), variantInfo);
    }

    public static Map<String, Object> buildScriptParameterValueMap(Collection<ImageScriptParameter> parameterInfoList, ImageScriptVariant variantInfo) throws ImageFXScriptParsingException {
        final Map<String, Object> map = new HashMap<>();
        for (ImageScriptParameter parameterInfo : parameterInfoList) {
            if (!variantInfo.containsParameterReferenceFor(parameterInfo))
                throw new IllegalArgumentException("List of parameter info and the given variant info object not fit: \n" +
                        parameterInfoList.toString() + "\n" + variantInfo.toString());

            final String plainValue = variantInfo.getParameterReferenceFor(parameterInfo).getPlainValue();
            final Object value = ObjectConverterUtils.convertFromString(plainValue, parameterInfo.getType());

            map.put(parameterInfo.getId(), value);
        }

        return map;
    }

    public static Map<String, ImageScriptParameter> getScriptParameterMap(XScript element) throws ImageFXScriptException {
        final Map<String, ImageScriptParameter> map = new HashMap<>();
        fillParameterInfoMap(element.getScriptParameterDefinition(), null, map);

        return map;
    }

    public static Map<String, ImageScriptParameterGroup> extractScriptGroupInfoMap(Collection<ImageScriptParameter> parameterInfoList) {
        final Map<String, ImageScriptParameterGroup> map = new HashMap<>();
        for (ImageScriptParameter parameterInfo : parameterInfoList) {
            if (parameterInfo.getGroupInfo() == null)
                continue;

            if (!map.containsKey(parameterInfo.getGroupInfo().getId())) {
                map.put(parameterInfo.getGroupInfo().getId(), parameterInfo.getGroupInfo());
            }
        }

        return map;
    }

    public static Map<ImageScriptParameterGroup, List<ImageScriptParameter>> extractParameterInfoGroupList(ImageScriptElement scriptInfo) {
        final Map<ImageScriptParameterGroup, List<ImageScriptParameter>> map = new HashMap<>();
        for (final ImageScriptParameter parameterInfo : scriptInfo.getParameterMap().values()) {
            if (!map.containsKey(parameterInfo.getGroupInfo())) {
                map.put(parameterInfo.getGroupInfo(), new ArrayList<>());
            }
            final List<ImageScriptParameter> parameterInfoList = map.get(parameterInfo.getGroupInfo());
            parameterInfoList.add(parameterInfo);
        }

        return map;
    }

    private static void fillParameterInfoMap(XScriptParameterDefinition parameterDefinition, ImageScriptParameterGroup groupInfo, Map<String, ImageScriptParameter> map) {
        if (parameterDefinition == null)
            return;

        for (Object item : parameterDefinition.getParameterDefinitionAndParameterGroup()) {
            if (item instanceof XParameterDefinition) {
                final XParameterDefinition definition = (XParameterDefinition) item;
                map.put(definition.getId(), new ImageScriptParameter(definition, groupInfo));
            } else if (item instanceof XParameterGroup) {
                final XParameterGroup parameterGroup = (XParameterGroup) item;
                fillParameterInfoMap(parameterGroup, new ImageScriptParameterGroup(parameterGroup, groupInfo), map);
            }
        }
    }

    private List<ImageScriptElement> buildElementList() throws ImageFXScriptException {
        final List<XScriptGroup> scriptGroupList = loadFromBasicFile();
        final List<ImageScriptElement> scriptInfoList = new ArrayList<>();

        for (XScriptGroup scriptGroup : scriptGroupList) {
            for (XScript scriptElement : scriptGroup.getScriptElement()) {
                scriptInfoList.add(new ImageScriptElement(scriptElement, scriptGroup, scriptType, "scripts", true));
            }
        }

        //AddOn
        try {
            if (addOnPath.toAbsolutePath().toFile().exists()) {
                Files.walkFileTree(addOnPath.toAbsolutePath(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.toFile().getAbsolutePath().endsWith(".xml")) {
                            try {
                                final List<XScriptGroup> addOnScriptGroupList = loadFromAddOnFile(file.toFile());
                                for (XScriptGroup scriptGroup : addOnScriptGroupList) {
                                    for (XScript scriptElement : scriptGroup.getScriptElement()) {
                                        scriptInfoList.add(new ImageScriptElement(scriptElement, scriptGroup,
                                                ImageScriptElement.ScriptType.Effect, file.getParent().toFile().getAbsolutePath(), false));
                                    }
                                }
                            } catch (ImageFXScriptException e) {
                                throw new IOException("Cannot load file: " + file.toFile().getAbsolutePath(), e);
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (IOException e) {
            throw new ImageFXScriptLoadingException("Cannot load additional scripts!", e);
        }

        return scriptInfoList;
    }

    protected abstract List<XScriptGroup> loadFromBasicFile() throws ImageFXScriptException;

    protected abstract List<XScriptGroup> loadFromAddOnFile(File file) throws ImageFXScriptException;

    protected abstract void addToScriptManager(ImageScriptElement element) throws ImageFXScriptException;
}
