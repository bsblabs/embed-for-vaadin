package com.bsb.common.vaadin.embed.component;

import com.bsb.common.vaadin.embed.AbstractEmbedTest;
import com.bsb.common.vaadin.embed.EmbedVaadinConfig;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Wouter Coekaerts
 */
public class EmbedComponentConfigTest extends AbstractEmbedTest {

    @Test
    public void createDefaultInstance() {
        final EmbedComponentConfig config = EmbedComponentConfig.defaultConfig();
        assertServerConfig(config, EmbedVaadinConfig.DEFAULT_PORT, EmbedVaadinConfig.DEFAULT_CONTEXT_PATH,
                EmbedVaadinConfig.DEFAULT_WAITING);
        assertComponentConfig(config, EmbedComponentConfig.DEFAULT_THEME);
        assertVaadinConfig(config, null);
        assertBrowserConfig(config, EmbedVaadinConfig.DEFAULT_START_BROWSER);
    }

    @Test
    public void load() {
        final EmbedComponentConfig config = new EmbedComponentConfig(
                EmbedVaadinConfig.loadProperties("/config/simple-embed-vaadin.properties"));
        assertServerConfig(config, 12345, "/foo", false);
        assertVaadinConfig(config, "com.bsb.foo.MyWidgetSet");
        assertComponentConfig(config, "myTheme");
        assertBrowserConfig(config, true);
    }

    @Test
    public void cloneConfig() {
        final EmbedComponentConfig config = EmbedComponentConfig.defaultConfig();
        final EmbedComponentConfig clone = new EmbedComponentConfig(config);

        // Change stuff
        config.setTheme("fooBarTheme");

        // Now validate the clone has not changed
        assertComponentConfig(clone, EmbedComponentConfig.DEFAULT_THEME);
    }

    protected void assertComponentConfig(EmbedComponentConfig config, String theme) {
        assertConfigIsNotNull(config);
        assertEquals("Wrong theme", theme, config.getTheme());
    }
}
