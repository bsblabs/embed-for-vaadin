/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        assertComponentConfig(config, EmbedComponentConfig.DEFAULT_THEME,
                EmbedComponentConfig.DEFAULT_DEVELOPMENT_HEADER);
        assertVaadinConfig(config, null);
        assertBrowserConfig(config, EmbedVaadinConfig.DEFAULT_START_BROWSER);
    }

    @Test
    public void load() {
        final EmbedComponentConfig config = new EmbedComponentConfig(
                EmbedVaadinConfig.loadProperties("/config/simple-embed-vaadin.properties"));
        assertServerConfig(config, 12345, "/foo", false);
        assertVaadinConfig(config, "com.bsb.foo.MyWidgetSet");
        assertComponentConfig(config, "myTheme", false);
        assertBrowserConfig(config, true);
    }

    @Test
    public void cloneConfig() {
        final EmbedComponentConfig config = EmbedComponentConfig.defaultConfig();
        final EmbedComponentConfig clone = new EmbedComponentConfig(config);

        // Change stuff
        config.setTheme("fooBarTheme");
        config.setDevelopmentHeader(false);

        // Now validate the clone has not changed
        assertComponentConfig(clone, EmbedComponentConfig.DEFAULT_THEME,
                EmbedComponentConfig.DEFAULT_DEVELOPMENT_HEADER);
    }

    protected void assertComponentConfig(EmbedComponentConfig config, String theme, boolean developmentHeader) {
        assertConfigIsNotNull(config);
        assertEquals("Wrong theme", theme, config.getTheme());
        assertEquals("Wrong development header flag", developmentHeader, config.isDevelopmentHeader());
    }
}
