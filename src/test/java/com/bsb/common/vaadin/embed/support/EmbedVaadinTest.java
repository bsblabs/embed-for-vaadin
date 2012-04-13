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
package com.bsb.common.vaadin.embed.support;

import com.bsb.common.vaadin.embed.AbstractEmbedTest;
import com.bsb.common.vaadin.embed.component.EmbedComponentConfig;
import com.bsb.common.vaadin.embed.component.EmbedVaadinComponent;
import com.google.common.io.Files;
import com.vaadin.ui.Button;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Stephane Nicoll
 */
public class EmbedVaadinTest extends AbstractEmbedTest {

    private final Button component = new Button("Test");

    @Test(expected = IllegalArgumentException.class)
    public void withNullContextPath() {
        EmbedVaadin.forComponent(component).withContextPath(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withNullContextRootDirectory() {
        EmbedVaadin.forComponent(component).withContextRootDirectory((File) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withNullContextRootDirectoryAsFile() {
        EmbedVaadin.forComponent(component).withContextRootDirectory((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void withNullConfigPath() {
        EmbedVaadin.forComponent(component).withConfigPath(null);
    }

    @Test(expected = NullPointerException.class)
    public void withNullConfigProperties() {
        EmbedVaadin.forComponent(component).withConfigProperties(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withNullTheme() {
        EmbedVaadin.forComponent(component).withTheme(null);
    }

    @Test
    public void withPort() {
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component).withHttpPort(8080);
        assertEquals("was not detected as expected", 8080, embed.build().getConfig().getPort());
    }

    @Test
    public void withRootContextPath() {
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component).withContextPath("/");
        assertEquals("/ was not detected as root context properly", "", embed.build().getConfig().getContextPath());
    }

    @Test
    public void withEmptyContextPath() {
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component).withContextPath(" ");
        assertEquals("empty string was not detected as root context properly",
                "", embed.build().getConfig().getContextPath());
    }

    @Test
    public void withContextWithoutForwardSlash() {
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component).withContextPath("foo");
        assertEquals("forward slash was not added to context as expected",
                "/foo", embed.build().getConfig().getContextPath());
    }

    @Test
    public void withSimpleContextPath() {
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component).withContextPath("/bar");
        assertEquals("was not detected as expected", "/bar", embed.build().getConfig().getContextPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void withUnknownContextRootDirectory() {
        EmbedVaadin.forComponent(component).withContextRootDirectory(new File(".", "/foo/bar/"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void withFileContextRootDirectory() throws IOException {
        EmbedVaadin.forComponent(component).withContextRootDirectory(File.createTempFile("fake", "foo"));
    }

    @Test
    public void withContextRootDirectoryAsFile() {
        final File rootContextDir = Files.createTempDir();
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component).withContextRootDirectory(rootContextDir);
        assertEquals("was not detected as expected", rootContextDir, embed.build().getConfig().getContextRootDirectory());
    }

    @Test
    public void withContextRootDirectoryAsRelativeDirectory() {
        final File f = new File(".", "src/main/java");
        assertTrue("src/main/java should exist [" + f.getAbsolutePath() + "] make sure you are running " +
                "the tests with the working directory set to this module.", f.exists());
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component).withContextRootDirectory("src/main/java");
        assertEquals("was not detected as expected", f, embed.build().getConfig().getContextRootDirectory());
    }

    @Test
    public void withVaadinTheme() {
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component).withTheme("showcase");
        assertEquals("was not detected as expected", "showcase", embed.build().getConfig().getTheme());
    }

    @Test
    public void withWidgetSet() {
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component).withWidgetSet("com.bsb.foo.MyWidgetSet");
        assertEquals("was not detected as expected", "com.bsb.foo.MyWidgetSet", embed.build().getConfig().getWidgetSet());
    }

    @Test
    public void withNullWidgetSet() {
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component).withWidgetSet(null);
        assertEquals("was not detected as expected", null, embed.build().getConfig().getWidgetSet());
    }

    @Test
    public void withOpenBrowser() {
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component).openBrowser(true);
        assertEquals("was not detected as expected", true, embed.build().getConfig().shouldOpenBrowser());
    }

    @Test
    public void withCustomConfig() {
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component)
                .withConfigPath("/config/simple-embed-vaadin.properties");

        final EmbedComponentConfig config = embed.build().getConfig();
        assertServerConfig(config, 12345, "/foo", false);
        assertVaadinConfig(config, "com.bsb.foo.MyWidgetSet");
    }

    @Test
    public void withDevelopmentHeader() {
        final EmbedVaadinComponent embed = EmbedVaadin.forComponent(component).withDevelopmentHeader(true);
        assertEquals("was not detected as expected", true, embed.build().getConfig().isDevelopmentHeader());
    }


}
