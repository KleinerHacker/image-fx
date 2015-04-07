package org.pcsoft.tools.image_fx.scripting;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.pcsoft.tools.image_fx.common.listeners.ProgressListener;
import org.pcsoft.tools.image_fx.scripting.exceptions.ImageFXScriptException;

/**
 * Created by pfeifchr on 18.06.2014.
 */
public class TestEffects {

    @BeforeClass
    public static void init() throws ImageFXScriptException {
        System.out.print("Load effects");
        XmlEffectScriptManager.getInstance().loadElements(new ProgressListener() {
            @Override
            public void onStartProgress() {

            }

            @Override
            public void onProgressUpdate(double progress) {
                System.out.print(".");
            }

            @Override
            public void onFinishProgress() {

            }
        });
        System.out.println();
    }

    @Ignore("To Fix")
    @Test
    public void testGrayscale() throws ImageFXScriptException {
        final WritableImage image = new WritableImage(1, 1);
        image.getPixelWriter().setColor(0, 0, Color.RED);

        final Color color = JavaScriptManager.runEffectScript(
                XmlEffectScriptManager.getInstance().getElementBy("grayscale", "default"), null, 1, 1, 0, 0, image.getPixelReader());

        Assert.assertEquals(Color.RED.grayscale(), color);
    }

}
