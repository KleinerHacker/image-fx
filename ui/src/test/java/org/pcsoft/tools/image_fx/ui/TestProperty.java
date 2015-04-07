package org.pcsoft.tools.image_fx.ui;

import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.apache.commons.lang.SystemUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pfeifchr on 23.07.2014.
 */
public class TestProperty {

    public static final class MyObject implements Observable {
        private String name;
        private int value;

        private final List<InvalidationListener> invalidationListenerList = new ArrayList<>();

        public MyObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
            onInvalidate();
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
            onInvalidate();
        }

        @Override
        public void addListener(InvalidationListener invalidationListener) {
            invalidationListenerList.add(invalidationListener);
        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {
            invalidationListenerList.remove(invalidationListener);
        }

        protected void onInvalidate() {
            for (InvalidationListener invalidationListener : invalidationListenerList) {
                invalidationListener.invalidated(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MyObject myObject = (MyObject) o;

            if (name != null ? !name.equals(myObject.name) : myObject.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "MyObject{" +
                    "name=" + name +
                    ", value=" + value +
                    '}';
        }
    }

    @Test
    public void test() {
        System.out.println("Java Version: " + SystemUtils.JAVA_VERSION);

        final MyObject hallo = new MyObject("Hallo", 12);
        final MyObject welt = new MyObject("Welt!", 999);

        hallo.addListener(observable -> {
            System.out.println("Invalidate (Hallo): " + hallo);
        });
        welt.addListener(observable -> {
            System.out.println("Invalidate (Welt): " + welt);
        });

        final ObservableMap<String, MyObject> map = new ObservableMapWrapper<>(new HashMap<>());
        map.put("1", hallo);
        map.put("2", welt);

        map.addListener((MapChangeListener<String, MyObject>) change -> {
            System.out.println("Changed (Map): " + change);
        });
        map.addListener((Observable observable) -> {
            System.out.println("Invalidate (Map)");
        });

        map.get("1").setName("???");
    }

    @Test
    public void testList() {
        final List<String> blaList = new ArrayList<>();
        blaList.remove(null);
    }

}
