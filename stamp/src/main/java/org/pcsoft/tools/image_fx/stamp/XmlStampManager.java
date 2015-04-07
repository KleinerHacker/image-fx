package org.pcsoft.tools.image_fx.stamp;

import org.pcsoft.tools.image_fx.common.listeners.ProgressListener;
import org.pcsoft.tools.image_fx.stamp.types.Stamp;
import org.pcsoft.tools.image_fx.stamp.types.StampGroup;
import org.pcsoft.tools.image_fx.stamp.xml.XStamp;
import org.pcsoft.tools.image_fx.stamp.xml.XStampGroup;
import org.pcsoft.tools.image_fx.stamp.xml.XStamps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christoph on 16.08.2014.
 */
public final class XmlStampManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlStampManager.class);

    private final List<Stamp> stampList = new ArrayList<>();

    private static XmlStampManager instance = null;

    public static XmlStampManager getInstance() {
        if (instance == null) {
            instance = new XmlStampManager();
        }

        return instance;
    }

    private XmlStampManager() {
    }

    public void loadElements(ProgressListener listener) {
        LOGGER.info("Load stamp elements...");

        if (listener != null) {
            listener.onStartProgress();
        }

        final XStamps XStamps = JAXB.unmarshal(Thread.currentThread().getContextClassLoader().getResourceAsStream("stamps/stamps.xml"), XStamps.class);

        int counter = 0;
        for (final XStampGroup xStampGroup : XStamps.getStampGroup()) {
            final StampGroup stampGroup = new StampGroup(xStampGroup);
            counter++;

            if (listener != null) {
                listener.onProgressUpdate((double)counter / (double)XStamps.getStampGroup().size());
            }

            for (final XStamp xStamp : xStampGroup.getStamp()) {
                final Stamp stamp = new Stamp(xStamp, stampGroup);
                stampList.add(stamp);
            }
        }

        Collections.sort(stampList, (o1, o2) -> {
            final int compare = o1.getMaskGroup().getName().compareTo(o2.getMaskGroup().getName());
            if (compare != 0)
                return compare;

            return o1.getName().compareTo(o2.getName());
        });

        if (listener != null) {
            listener.onFinishProgress();
        }
    }

    public List<Stamp> getStampList() {
        return stampList;
    }

    public Map<StampGroup, List<Stamp>> extractMaskGroupMap() {
        final Map<StampGroup, List<Stamp>> map = new LinkedHashMap<>();
        for (Stamp stamp : stampList) {
            if (!map.containsKey(stamp.getMaskGroup())) {
                map.put(stamp.getMaskGroup(), new ArrayList<>());
            }
            map.get(stamp.getMaskGroup()).add(stamp);
        }

        return map;
    }
}
