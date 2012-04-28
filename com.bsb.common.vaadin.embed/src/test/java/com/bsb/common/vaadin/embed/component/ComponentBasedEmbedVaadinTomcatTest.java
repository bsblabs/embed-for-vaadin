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
import com.bsb.common.vaadin.embed.EmbedVaadinServer;
import com.bsb.common.vaadin.embed.support.EmbedVaadin;
import com.vaadin.ui.Button;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Stephane Nicoll
 */
public class ComponentBasedEmbedVaadinTomcatTest extends AbstractEmbedTest {

    @Test
    public void startWithDefaultSettings() {
        final EmbedVaadinServer server = EmbedVaadin.forComponent(new Button("Hello"))
                .wait(false).build();

        // No HTTP port allocated
        assertEquals("Wrong default value for http port", 0, server.getConfig().getPort());
        assertDeployUrl(server.getConfig(), "http://localhost:[auto]/");

        server.start();

        // Once the server has started, the port should be set in the config
        assertTrue("A port should have been allocated automatically", server.getConfig().getPort() != 0);
        assertDeployUrl(server.getConfig(), "http://localhost:" + server.getConfig().getPort() + "/");

        checkVaadinIsDeployed(server.getConfig().getPort(), "");

        server.stop();
    }

    @Test
    public void startWithCustomWidgetSetAndCustomPort() {
        final EmbedVaadinServer server = EmbedVaadin.forComponent(new Button("Hello"))
                .wait(false).withHttpPort(18002)
                .withWidgetSet("com.vaadin.terminal.gwt.DefaultWidgetSet").start();

        checkVaadinIsDeployed(18002, "");

        server.stop();
    }

    @Test
    public void startWithCustomBrowserUrl() {
        final EmbedVaadinServer server = EmbedVaadin.forComponent(new Button("Hello"))
                .openBrowserAt("?debug").openBrowser(false).wait(false).build();

        assertOpenBrowserUrl(server.getConfig(), "http://localhost:[auto]/?debug");

        server.start();

        // Once the server has started, the port should be set in the config
        assertTrue("A port should have been allocated automatically", server.getConfig().getPort() != 0);
        assertOpenBrowserUrl(server.getConfig(), "http://localhost:" + server.getConfig().getPort() + "/?debug");

        checkVaadinIsDeployed(server.getConfig().getPort(), "");

        server.stop();
    }

}
