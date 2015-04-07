package org.pcsoft.tools.image_fx.core.threading;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.pcsoft.tools.image_fx.common.listeners.ProgressListener;
import org.pcsoft.tools.image_fx.core.exceptions.ImageFXColorCalculatorException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Created by pfeifchr on 12.06.2014.
 */
public abstract class ImageWriterTask extends RecursiveTask<Void> {

    private static int SPLIT_X = 10;
    private static int SPLIT_Y = 10;

    private final int startX, endX, startY, endY;
    private final PixelReader pixelReader;
    private final PixelWriter pixelWriter;
    private final boolean splitted;
    protected final ImageInformation imageInformation;
    private ProgressListener progressListener = null;

    protected ImageWriterTask(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter) {
        this(0, width, 0, height, new ImageInformation(width, height), pixelReader, pixelWriter, false);
    }

    protected ImageWriterTask(int startX, int endX, int startY, int endY, ImageInformation imageInformation, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.pixelReader = pixelReader;
        this.pixelWriter = pixelWriter;
        this.splitted = splitted;
        this.imageInformation = imageInformation;
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    protected Void compute() {
        if (!splitted) {
            onStartProgress();

            final int splitWidth = (endX - startX) / SPLIT_X;
            final int splitHeight = (endY - startY) / SPLIT_Y;

            final List<ImageWriterTask> imageWriterTaskList = new ArrayList<>();
            for (int y = 0; y < SPLIT_Y; y++) {
                for (int x = 0; x < SPLIT_X; x++) {
                    final int _startX = x * splitWidth, _startY = y * splitHeight;
                    final int _endX = (x + 1) == SPLIT_X ? endX - startX - 1 : (x + 1) * splitWidth - 1;
                    final int _endY = (y + 1) == SPLIT_Y ? endY - startY - 1 : (y + 1) * splitHeight - 1;

                    imageWriterTaskList.add(createTask(_startX, _endX, _startY, _endY, pixelReader, pixelWriter, true));
                }
            }

            final ImageWriterTask ownTask = imageWriterTaskList.get(0);
            imageWriterTaskList.remove(0);

            for (ImageWriterTask task : imageWriterTaskList) {
                task.fork();
            }
            ownTask.compute();
            onProgressUpdate(1f / (float) (SPLIT_X * SPLIT_Y));
            int counter = 1;
            for (ImageWriterTask task : imageWriterTaskList) {
                task.join();
                counter++;

                onProgressUpdate((float) counter / (float) (SPLIT_X * SPLIT_Y));
            }

            onFinishProgress();
        } else {
            for (int y = startY; y <= endY; y++) {
                for (int x = startX; x <= endX; x++) {
                    try {
                        final Color newColor = calculateColor(x, y, pixelReader);
                        pixelWriter.setColor(x, y, newColor);
                    } catch (ImageFXColorCalculatorException e) {
                        e.printStackTrace();//TODO
                    } catch (Exception e) {
                        System.out.println("Cannot set color: " + e.getMessage());
                    }
                }
            }
        }

        return null;
    }

    protected abstract Color calculateColor(int x, int y, PixelReader pixelReader) throws ImageFXColorCalculatorException;

    protected abstract ImageWriterTask createTask(int startX, int endX, int startY, int endY, PixelReader pixelReader, PixelWriter pixelWriter, boolean splitted);

    protected void onStartProgress() {
        if (progressListener != null) {
            progressListener.onStartProgress();
        }
    }

    protected void onFinishProgress() {
        if (progressListener != null) {
            progressListener.onFinishProgress();
        }
    }

    protected void onProgressUpdate(float progress) {
        if (progressListener != null) {
            progressListener.onProgressUpdate(progress);
        }
    }
}
