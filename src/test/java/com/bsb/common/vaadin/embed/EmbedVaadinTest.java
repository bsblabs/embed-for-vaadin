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

import com.google.common.io.Files;
import com.vaadin.ui.Button;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

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

    @Test(expected = IllegalArgumentException.class)
    public void withNullConfig() {
        EmbedVaadin.forComponent(component).withConfig(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withNullTheme() {
        EmbedVaadin.forComponent(component).withVaadinTheme(null);
    }

    @Test
    public void withPort() {
        final EmbedVaadin embed = EmbedVaadin.forComponent(component).withHttpPort(8080);
        assertEquals("was not detected as expected", 8080, embed.getConfig().getPort());
    }

    @Test
    public void withRootContextPath() {
        final EmbedVaadin embed = EmbedVaadin.forComponent(component).withContextPath("/");
        assertEquals("/ was not detected as root context properly", "", embed.getConfig().getContextPath());
    }

    @Test
    public void withEmptyContextPath() {
        final EmbedVaadin embed = EmbedVaadin.forComponent(component).withContextPath(" ");
        assertEquals("empty string was not detected as root context properly",
                "", embed.getConfig().getContextPath());
    }

    @Test
    public void withContextWithoutForwardSlash() {
        final EmbedVaadin embed = EmbedVaadin.forComponent(component).withContextPath("foo");
        assertEquals("forward slash was not added to context as expected",
                "/foo", embed.getConfig().getContextPath());
    }

    @Test
    public void withSimpleContextPath() {
        final EmbedVaadin embed = EmbedVaadin.forComponent(component).withContextPath("/bar");
        assertEquals("was not detected as expected", "/bar", embed.getConfig().getContextPath());
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
        final EmbedVaadin embed = EmbedVaadin.forComponent(component).withContextRootDirectory(rootContextDir);
        assertEquals("was not detected as expected", rootContextDir, embed.getConfig().getContextRootDirectory());
    }

    @Test
    public void withVaadinTheme() {
        final EmbedVaadin embed = EmbedVaadin.forComponent(component).withVaadinTheme("showcase");
        assertEquals("was not detected as expected", "showcase", embed.getConfig().getTheme());
    }

    @Test
    public void withOpenBrowser() {
        final EmbedVaadin embed = EmbedVaadin.forComponent(component).openBrowser(true);
        assertEquals("was not detected as expected", true, embed.getConfig().shouldOpenBrowser());
    }

    @Test
    public void withCustomConfig() {
        final EmbedVaadinConfig config = EmbedVaadinConfig.load("/config/simple-embed-vaadin.properties");
        final EmbedVaadin embed = EmbedVaadin.forComponent(component).withConfig(config);

        assertServerConfig(embed.getConfig(), 12345, "/foo", false);
        assertVaadinConfig(embed.getConfig(), "myTheme");
    }


}
