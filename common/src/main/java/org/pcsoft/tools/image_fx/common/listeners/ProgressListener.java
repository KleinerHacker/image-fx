package org.pcsoft.tools.image_fx.common.listeners;

/**
 * Created by Christoph on 17.06.2014.
 */
public interface ProgressListener {
    void onStartProgress();
    void onProgressUpdate(double progress);
    void onFinishProgress();
}
