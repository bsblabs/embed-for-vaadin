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
package com.bsb.common.vaadin.embed;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * @author Stephane Nicoll
 */
public class EmbedVaadinConfigTest extends AbstractEmbedTest {

    @Test
    public void createDefaultInstance() {
        final EmbedVaadinConfig config = EmbedVaadinConfig.defaultConfig();
        assertServerConfig(config, EmbedVaadinConfig.DEFAULT_PORT, EmbedVaadinConfig.DEFAULT_CONTEXT_PATH,
                EmbedVaadinConfig.DEFAULT_WAITING);
        assertDeployUrl(config, "http://localhost:[auto]/");
        assertVaadinConfig(config, null, EmbedVaadinConfig.DEFAULT_PRODUCTION_MODE);
        assertBrowserConfig(config, EmbedVaadinConfig.DEFAULT_START_BROWSER);
    }

    @Test(expected = IllegalStateException.class)
    public void loadUnknownFile() {
        EmbedVaadinConfig.loadProperties("/foo/bar/does-not-exist.properties");
    }

    @Test
    public void load() {
        final EmbedVaadinConfig config = new EmbedVaadinConfig(
                EmbedVaadinConfig.loadProperties("/config/simple-embed-vaadin.properties"));
        assertServerConfig(config, 12345, "/foo", false);
        assertDeployUrl(config, "http://localhost:12345/foo");
        assertVaadinConfig(config, "com.bsb.foo.MyWidgetSet", true);
        assertBrowserConfig(config, true);
    }

    @Test(expected = IllegalStateException.class)
    public void loadWithInvalidRootWebDir() {
        new EmbedVaadinConfig(EmbedVaadinConfig.loadProperties("/config/root-dir-embed-vaadin.properties"));
    }

    @Test
    public void cloneConfig() {
        final EmbedVaadinConfig config = EmbedVaadinConfig.defaultConfig();
        final EmbedVaadinConfig clone = new EmbedVaadinConfig(config);

        // Change stuff
        config.setPort(1);
        config.setContextPath("/foo/bar");
        config.setContextRootDirectory(new File("."));
        config.setWidgetSet("com.bar.MyAnotherWidgetSet");
        config.setProductionMode(true);
        config.setWaiting(true);
        config.setOpenBrowser(true);

        // Now validate the clone has not changed
        assertServerConfig(clone, EmbedVaadinConfig.DEFAULT_PORT, EmbedVaadinConfig.DEFAULT_CONTEXT_PATH,
                EmbedVaadinConfig.DEFAULT_WAITING);
        assertVaadinConfig(clone, null, EmbedVaadinConfig.DEFAULT_PRODUCTION_MODE);
        assertBrowserConfig(clone, EmbedVaadinConfig.DEFAULT_START_BROWSER);
    }

    @Test
    public void loadCleansContextPath() {
        final EmbedVaadinConfig config = createCustomConfig(8080, "foo");
        assertEquals("Context path should have been cleaned", "/foo", config.getContextPath());
    }

    @Test
    public void loadCleansRootContextPath() {
        final EmbedVaadinConfig config = createCustomConfig(8080, "/");
        assertEquals("Context path '/' should have been detected as root context", "", config.getContextPath());
    }

}

