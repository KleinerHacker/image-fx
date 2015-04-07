package org.pcsoft.tools.image_fx.scripting;

import org.pcsoft.tools.image_fx.common.IdUtils;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptException;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptLoadingException;
import org.pcsoft.tools.image_fx.scripting.types.image.ImageScriptVariant;
import org.pcsoft.tools.image_fx.scripting.xml.*;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Christoph on 12.07.2014.
 */
abstract class XmlScriptVariantManager {

    public static final Logger LOGGER = LoggerFactory.getLogger(XmlScriptVariantManager.class);

    protected final class Key {
        private final String group;
        private final String script;

        protected Key(String group, String script) {
            this.group = group;
            this.script = script;
        }

        @Override
        public int hashCode() {
            return Objects.hash(group, script);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            return Objects.equals(this.group, other.group) && Objects.equals(this.script, other.script);
        }
    }

    private final Map<Key, List<ImageScriptVariant>> variantListMap = new HashMap<>();
    protected final Path addOnPath, userDataFile;

    protected XmlScriptVariantManager(Path addOnPath, Path userDataFile) {
        LOGGER.info("Initialize script variant manager '" + getClass().getSimpleName() + "'...");

        this.addOnPath = addOnPath;
        this.userDataFile = userDataFile;

        LOGGER.info(">>> OK");
    }

    public final void loadElements() throws ImageFXScriptException {
        LOGGER.debug("Load script variant elements ('" + getClass().getSimpleName() + "')...");

        try {
            variantListMap.clear();
            variantListMap.putAll(buildElementList());
        } catch (Exception e) {
            throw new ImageFXScriptLoadingException(e);
        }
    }

    public final Map<String, ImageScriptVariant> getVariantMap(String group, String script) {
        final Key key = new Key(group, script);
        if (!variantListMap.containsKey(key))
            return new HashMap<>();

        final Map<String, ImageScriptVariant> map = new LinkedHashMap<>();
        for (final ImageScriptVariant variantInfo : variantListMap.get(key)) {
            map.put(variantInfo.getId(), variantInfo);
        }

        return map;
    }

    public final ImageScriptVariant storeVariant(String group, String script, String name, ImageScriptVariant variantInfo) {
        LOGGER.debug("Store script variant...");

        final Key key = new Key(group, script);
        if (!variantListMap.containsKey(key)) {
            variantListMap.put(key, new ArrayList<>());
        }

        final ImageScriptVariant newVariantInfo = new ImageScriptVariant(IdUtils.generateId(), name, variantInfo);
        variantListMap.get(key).add(newVariantInfo);

        storeVariantToFile(group, script, newVariantInfo);

        return newVariantInfo;
    }

    private void storeVariantToFile(String group, String script, ImageScriptVariant variantInfo) {
        LOGGER.debug("Rebuild (save) script variant user data file...");

        final File file = userDataFile.toFile();
        final XVariants xVariants;
        if (file.exists()) {
            xVariants = JAXB.unmarshal(file, XVariants.class);
        } else {
            xVariants = new XVariants();
        }

        final XScriptVariants xScriptVariants = xVariants.getScriptVariants().stream().filter(
                xScriptVariantsIn -> xScriptVariantsIn.getGroupRef().equals(group) &&
                        xScriptVariantsIn.getScriptRef().equals(script)
        ).findFirst().orElseGet(() -> {
            final XScriptVariants tmp = new XScriptVariants();
            tmp.setGroupRef(group);
            tmp.setScriptRef(script);

            xVariants.getScriptVariants().add(tmp);

            return tmp;
        });

        final XVariant xVariant = new XVariant();
        xVariant.setDefault(false);
        xVariant.setId(variantInfo.getId());
        xVariant.setName(variantInfo.getName());
        xVariant.setSortId(999);
        xVariant.getParameterReference().addAll(
                variantInfo.getParameterReferenceMap().values().stream().map(xmlParameterReference -> {
                    final XParameterReference xParameterReference = new XParameterReference();
                    xParameterReference.setRef(xmlParameterReference.getReference());
                    xParameterReference.setValue(xmlParameterReference.getPlainValue());

                    return xParameterReference;
                }).collect(Collectors.toList())
        );

        xScriptVariants.getVariant().add(xVariant);

        JAXB.marshal(xVariants, file);
    }

    private Map<Key, List<ImageScriptVariant>> buildElementList() throws ImageFXScriptException {
        final Map<Key, List<ImageScriptVariant>> variantListMap = loadFromBasicFile();
        final Map<Key, List<ImageScriptVariant>> userDataVariantListMap = loadFromUserDataFile();
        addToMap(userDataVariantListMap, variantListMap);

        //AddOn
        try {
            if (addOnPath.toAbsolutePath().toFile().exists()) {
                Files.walkFileTree(addOnPath.toAbsolutePath(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.toFile().getAbsolutePath().endsWith(".xml")) {
                            try {
                                final Map<Key, List<ImageScriptVariant>> addOnVariantListMap = loadFromAddOnFile(file.toFile());
                                addToMap(addOnVariantListMap, variantListMap);
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

        for (List<ImageScriptVariant> variantInfoList : variantListMap.values()) {
            Collections.sort(variantInfoList, (o1, o2) -> ((Integer) o1.getVariant().getSortId()).compareTo(o2.getVariant().getSortId()));
        }

        return variantListMap;
    }

    private static void addToMap(Map<Key, List<ImageScriptVariant>> mapToInsert, Map<Key, List<ImageScriptVariant>> targetMap) {
        for (final Key key : mapToInsert.keySet()) {
            if (targetMap.containsKey(key)) {
                targetMap.get(key).addAll(mapToInsert.get(key));
            } else {
                targetMap.put(key, mapToInsert.get(key));
            }
        }
    }

    private Map<XmlScriptVariantManager.Key, List<ImageScriptVariant>> loadFromBasicFile() throws ImageFXScriptException {
        final XVariants xVariants = JAXB.unmarshal(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(getBasicFileName()),
                XVariants.class
        );

        return buildMap(xVariants, true);
    }

    private Map<XmlScriptVariantManager.Key, List<ImageScriptVariant>> loadFromUserDataFile() throws ImageFXScriptException {
        if (!userDataFile.toFile().exists())
            return new HashMap<>();

        final XVariants xVariants = JAXB.unmarshal(userDataFile.toFile(), XVariants.class);
        return buildMap(xVariants, false);
    }

    private Map<XmlScriptVariantManager.Key, List<ImageScriptVariant>> loadFromAddOnFile(File file) throws ImageFXScriptException {
        final XVariants xVariants = JAXB.unmarshal(file, XVariants.class);

        return buildMap(xVariants, true);
    }

    private Map<Key, List<ImageScriptVariant>> buildMap(XVariants xVariants, boolean readOnly) {
        final Map<Key, List<ImageScriptVariant>> map = new HashMap<>();
        for (XScriptVariants xScriptVariants : xVariants.getScriptVariants()) {
            final Key key = new Key(xScriptVariants.getGroupRef(), xScriptVariants.getScriptRef());
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }

            for (XVariant variant : xScriptVariants.getVariant()) {
                map.get(key).add(new ImageScriptVariant(variant, readOnly));
            }
        }

        return map;
    }

    protected abstract String getBasicFileName();
}
