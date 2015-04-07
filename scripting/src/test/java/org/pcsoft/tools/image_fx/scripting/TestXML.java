package org.pcsoft.tools.image_fx.scripting;

import org.pcsoft.tools.image_fx.scripting.xml.Effects;
import org.pcsoft.tools.image_fx.scripting.xml.Renderers;
import junit.framework.Assert;
import org.junit.Test;

import javax.xml.bind.JAXB;

/**
 * Created by Christoph on 22.06.2014.
 */
public class TestXML {

    @Test
    public void testLoadEffects() {
        final Effects effects = JAXB.unmarshal(Thread.currentThread().getContextClassLoader().getResourceAsStream("scripts/effects.xml"), Effects.class);

        Assert.assertNotNull(effects);
        Assert.assertFalse(effects.getScriptGroup().isEmpty());
    }

    @Test
    public void testLoadRenderers() {
        final Renderers renderers = JAXB.unmarshal(Thread.currentThread().getContextClassLoader().getResourceAsStream("scripts/renderers.xml"), Renderers.class);

        Assert.assertNotNull(renderers);
        Assert.assertFalse(renderers.getScriptGroup().isEmpty());
    }

}
