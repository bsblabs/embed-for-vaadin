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
package com.bsb.common.vaadin.embed.application;

import com.bsb.common.vaadin.embed.EmbedVaadinConfig;
import com.bsb.common.vaadin.embed.EmbedVaadinServer;
import com.bsb.common.vaadin.embed.EmbedVaadinServerBuilder;
import com.vaadin.ui.UI;

import java.util.Properties;

/**
 * A builder for a server embedding an application.
 *
 * @author Stephane Nicoll
 */
public class EmbedVaadinApplication extends EmbedVaadinServerBuilder<EmbedVaadinApplication, EmbedVaadinServer> {

    private final Class<? extends UI> uiClass;
    private EmbedVaadinConfig config;

    /**
     * Creates a new instance for the specified UI.
     *
     * @param uiClass the class of the UI to deploy
     */
    public EmbedVaadinApplication(Class<? extends UI> uiClass) {
        super();
        assertNotNull(uiClass, "uiClass could not be null.");
        this.uiClass = uiClass;
        initializeConfig(EmbedVaadinConfig.loadProperties());
    }

    /**
     * Returns the {@link UI} type that was used to initialize this instance, if any.
     *
     * @return the UI class or <tt>null</tt> if a component was set
     */
    protected Class<? extends UI> getUiClass() {
        return uiClass;
    }

    @Override
    protected EmbedVaadinApplication self() {
        return this;
    }

    @Override
    public EmbedVaadinServer build() {
        return new ApplicationBasedEmbedVaadinTomcat(getConfig(), getUiClass());
    }

    @Override
    public EmbedVaadinApplication withConfigProperties(Properties properties) {
        initializeConfig(properties);
        return self();
    }

    @Override
    protected EmbedVaadinConfig getConfig() {
        return config;
    }

    /**
     * Initializes a default configuration.
     *
     * @param properties the configuration
     */
    private void initializeConfig(Properties properties) {
        assertNotNull(properties, "properties could not be null.");
        this.config = new EmbedVaadinConfig(properties);
    }
}
