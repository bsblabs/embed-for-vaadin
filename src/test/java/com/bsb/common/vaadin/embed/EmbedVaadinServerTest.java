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

import com.bsb.common.vaadin.embed.component.EmbedComponentConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Stephane Nicoll
 */
public class EmbedVaadinServerTest extends AbstractEmbedTest {

    @Test
    public void getDeployUrlWithDefaultSettings() {
        final TestableEmbedVaadinServer server = new TestableEmbedVaadinServer(EmbedComponentConfig.defaultConfig());
        assertEquals("http://localhost:[auto]/", server.getConfig().getDeployUrl());
    }

    @Test
    public void getDeployUrlWithCustomHttpPort() {
        final TestableEmbedVaadinServer server = new TestableEmbedVaadinServer(createCustomConfig(8080, null));
        assertEquals("http://localhost:8080/", server.getConfig().getDeployUrl());
    }

    @Test
    public void getDeployUrlWithCustomHttpPortAndContextRoot() {
        final TestableEmbedVaadinServer server = new TestableEmbedVaadinServer(createCustomConfig(8080, "/foo"));
        assertEquals("http://localhost:8080/foo", server.getConfig().getDeployUrl());
    }

    @SuppressWarnings("serial")
    private static final class TestableEmbedVaadinServer extends AbstractEmbedVaadinTomcat {


        private TestableEmbedVaadinServer(EmbedVaadinConfig config) {
            super(config);
        }

        @Override
        protected void configure() {
        }
    }
}
