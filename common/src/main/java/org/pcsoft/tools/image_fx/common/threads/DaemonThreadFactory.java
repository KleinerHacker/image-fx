package org.pcsoft.tools.image_fx.common.threads;

import java.util.concurrent.ThreadFactory;

/**
 * Created by Christoph on 20.04.2015.
 */
public class DaemonThreadFactory implements ThreadFactory {

    private final String name;

    public DaemonThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        final Thread thread = new Thread(r, name);
        thread.setDaemon(true);

        return thread;
    }
}
