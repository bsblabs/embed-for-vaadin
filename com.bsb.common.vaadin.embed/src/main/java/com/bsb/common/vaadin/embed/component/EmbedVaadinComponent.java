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

import java.util.Properties;

import com.bsb.common.vaadin.embed.EmbedVaadinConfig;
import com.bsb.common.vaadin.embed.EmbedVaadinServer;
import com.bsb.common.vaadin.embed.EmbedVaadinServerBuilder;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Component;

/**
 * A builder for a server embedding a component.
 *
 * @author Stephane Nicoll
 */
public class EmbedVaadinComponent extends EmbedVaadinServerBuilder<EmbedVaadinComponent, EmbedVaadinServer> {

    private final Component component;
    private EmbedComponentConfig config;

    /**
     * Creates a new instance for the specified {@link Component}.
     *
     * @param component the component to deploy
     */
    public EmbedVaadinComponent(Component component) {
        super();
        assertNotNull(component, "component could not be null.");
        this.component = component;
        initializeConfig(EmbedVaadinConfig.loadProperties());
    }

    /**
     * Specifies the vaadin theme to use for the application.
     *
     * @param theme the theme to use
     * @return this
     */
    public EmbedVaadinComponent withTheme(String theme) {
        assertNotNull(theme, "theme could not be null.");
        getConfig().setTheme(theme);
        return self();
    }

    /**
	 * Specifies the vaadin theme to use for the application.
	 *
	 * @param theme
	 *            the theme to use
	 * @return this
	 */
	public EmbedVaadinComponent withPushMode(PushMode mode) {
		assertNotNull(mode, "push mode could not be null.");
		getConfig().setPushMode(mode);
		return self();
	}

	/**
	 * Specifies if the development header is added automatically to the created
	 * application.
	 *
	 * @param useHeader
	 *            <tt>true</tt> if the development header should be added
	 * @return this
	 */
    public EmbedVaadinComponent withDevelopmentHeader(boolean useHeader) {
        getConfig().setDevelopmentHeader(useHeader);
        return self();
    }


    /**
     * Returns the {@link Component} that was used to initialize this instance, if any.
     *
     * @return the component or <tt>null</tt> if an application was set
     */
    protected Component getComponent() {
        return component;
    }

    @Override
    protected EmbedVaadinComponent self() {
        return this;
    }

    @Override
    public ComponentBasedVaadinServer build() {
        return new ComponentBasedEmbedVaadinTomcat(config, getComponent());
    }

    @Override
    public EmbedVaadinComponent withConfigProperties(Properties properties) {
        initializeConfig(properties);
        return self();
    }

    @Override
    protected EmbedComponentConfig getConfig() {
        return config;
    }

    /**
     * Initializes a default configuration.
     *
     * @param properties the configuration
     */
    private void initializeConfig(Properties properties) {
        assertNotNull(properties, "properties could not be null.");
        this.config = new EmbedComponentConfig(properties);
    }
}
