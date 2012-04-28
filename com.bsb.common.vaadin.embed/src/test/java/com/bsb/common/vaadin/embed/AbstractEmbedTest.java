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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author Stephane Nicoll
 */
public abstract class AbstractEmbedTest {

    protected void assertServerConfig(EmbedVaadinConfig config, int port, String contextPath, boolean waiting) {
        assertConfigIsNotNull(config);
        assertEquals("Wrong http port", port, config.getPort());
        assertEquals("Wrong contextPath", contextPath, config.getContextPath());
        assertEquals("Wrong waiting flag", waiting, config.isWaiting());
    }

    protected void assertDeployUrl(EmbedVaadinConfig config, String deployUrl) {
        assertEquals("Wrong deploy url", deployUrl, config.getDeployUrl());
    }

    protected void assertVaadinConfig(EmbedVaadinConfig config, String widgetSet, boolean productionMode) {
        assertConfigIsNotNull(config);
        assertEquals("Wrong widgetSet", widgetSet, config.getWidgetSet());
        assertEquals("Wrong production mode", productionMode, config.isProductionMode());
    }

    protected void assertBrowserConfig(EmbedVaadinConfig config, boolean openBrowser, String customBrowserUrl) {
        assertConfigIsNotNull(config);
        assertEquals("Wrong openBrowser flag", openBrowser, config.shouldOpenBrowser());
        assertEquals("Wrong custom browser url", customBrowserUrl, config.getCustomBrowserUrl());
    }

    protected void assertOpenBrowserUrl(EmbedVaadinConfig config, String openBrowserUrl) {
        assertEquals("Wrong open browser url", openBrowserUrl, config.getOpenBrowserUrl());
    }


    protected void checkVaadinIsDeployed(int port, String context) {
        final StringBuilder sb = new StringBuilder();
        sb.append("http://localhost:").append(port);
        if (context.trim().isEmpty() || context.equals("/")) {
            sb.append("/");
        } else {
            sb.append(context);
        }
        final String url = sb.toString();

        final HttpClient client = new HttpClient();
        client.getParams().setConnectionManagerTimeout(2000);
        final GetMethod method = new GetMethod(url);

        try {
            assertEquals("Wrong return code invoking [" + url + "]", HttpStatus.SC_OK, client.executeMethod(method));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to invoke url [" + url + "]", e);
        }
    }

    protected void assertConfigIsNotNull(EmbedVaadinConfig config) {
        assertNotNull("config could not be null.", config);
    }

    protected EmbedVaadinConfig createCustomConfig(Integer httpPort, String contextRoot) {
        final Properties properties = new Properties();
        if (httpPort != null) {
            properties.put(EmbedComponentConfig.KEY_PORT, String.valueOf(httpPort));
        }
        if (contextRoot != null) {
            properties.put(EmbedComponentConfig.KEY_CONTEXT_PATH, contextRoot);
        }
        return new EmbedVaadinConfig(properties);
    }
}
