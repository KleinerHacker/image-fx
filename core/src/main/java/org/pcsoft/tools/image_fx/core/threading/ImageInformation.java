package org.pcsoft.tools.image_fx.core.threading;

/**
 * Created by pfeifchr on 16.06.2014.
 */
public final class ImageInformation {

    private final int width, height;

    ImageInformation(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageInformation that = (ImageInformation) o;

        if (height != that.height) return false;
        if (width != that.width) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        return result;
    }

    @Override
    public String toString() {
        return "ImageInformation{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
