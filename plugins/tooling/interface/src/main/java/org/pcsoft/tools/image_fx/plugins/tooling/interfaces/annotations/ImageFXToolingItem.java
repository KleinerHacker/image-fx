package org.pcsoft.tools.image_fx.plugins.tooling.interfaces.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by Christoph on 09.08.2014.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={TYPE})
public @interface ImageFXToolingItem {

    /**
     * Name of optional group (in tool menu)
     * @return
     */
    String group() default "";

    /**
     * Name of item (title) in tool menu
     * @return
     */
    String title();

    /**
     * Defines that this plugin needs a loaded image. Default is <code>true</code>
     * @return
     */
    boolean needLoadedImage() default true;

    /**
     * Optional key combination as string, e. g. CTRL+SHIFT+S
     * @return
     */
    String keyCombination() default "";

    /**
     * Icon resource path (optional)
     * @return
     */
    String icon() default "";

}
