/*
 * Copyright 2006-2007 the original author or authors.
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
package com.bsb.common.vaadin.embed;

import org.junit.Test;

import java.io.File;

/**
 * @author Stephane Nicoll
 */
public class EmbedVaadinConfigTest extends AbstractEmbedTest {

    @Test
    public void createDefaultInstance() {
        final EmbedVaadinConfig config = EmbedVaadinConfig.defaultConfig();
        assertServerConfig(config, EmbedVaadinConfig.DEFAULT_PORT, EmbedVaadinConfig.DEFAULT_CONTEXT_PATH,
                EmbedVaadinConfig.DEFAULT_WAITING);
        assertVaadinConfig(config, EmbedVaadinConfig.DEFAULT_THEME, null);
        assertBrowserConfig(config, EmbedVaadinConfig.DEFAULT_START_BROWSER);
    }

    @Test(expected = IllegalStateException.class)
    public void loadUnknownFile() {
        EmbedVaadinConfig.load("/foo/bar/does-not-exist.properties");
    }

    @Test
    public void load() {
        final EmbedVaadinConfig config = EmbedVaadinConfig.load("/config/simple-embed-vaadin.properties");
        assertServerConfig(config, 12345, "/foo", false);
        assertVaadinConfig(config, "myTheme", "com.bsb.foo.MyWidgetSet");
        assertBrowserConfig(config, true);
    }

    @Test(expected = IllegalStateException.class)
    public void loadWithInvalidRootWebDir() {
        EmbedVaadinConfig.load("/config/root-dir-embed-vaadin.properties");
    }

    @Test
    public void cloneConfig() {
        final EmbedVaadinConfig config = EmbedVaadinConfig.defaultConfig();
        final EmbedVaadinConfig clone = new EmbedVaadinConfig(config);

        // Change stuff
        config.setPort(1);
        config.setContextPath("/foo/bar");
        config.setContextRootDirectory(new File("."));
        config.setTheme("fooBarTheme");
        config.setWidgetSet("com.bar.MyAnotherWidgetSet");
        config.setWaiting(true);
        config.setOpenBrowser(true);

        // Now validate the clone has not changed
        assertServerConfig(clone, EmbedVaadinConfig.DEFAULT_PORT, EmbedVaadinConfig.DEFAULT_CONTEXT_PATH,
                EmbedVaadinConfig.DEFAULT_WAITING);
        assertVaadinConfig(clone, EmbedVaadinConfig.DEFAULT_THEME, null);
        assertBrowserConfig(clone, EmbedVaadinConfig.DEFAULT_START_BROWSER);

    }

}

