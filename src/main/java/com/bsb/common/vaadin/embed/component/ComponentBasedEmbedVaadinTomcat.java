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

import com.bsb.common.vaadin.embed.AbstractEmbedVaadinTomcat;
import com.vaadin.ui.Component;
import org.apache.catalina.Wrapper;

/**
 * A {@link com.bsb.common.vaadin.embed.EmbedVaadinServer} implementation that
 * deploys a initialized component.
 * <p/>
 * To be used for quick prototyping. Does not handle session or multi-tabs.
 *
 * @author Stephane Nicoll
 */
@SuppressWarnings("serial")
public class ComponentBasedEmbedVaadinTomcat extends AbstractEmbedVaadinTomcat implements ComponentBasedVaadinServer {

    private final EmbedComponentConfig config;
    private final Component component;

    /**
     * Creates a new instance.
     *
     * @param config the config to use
     * @param component the component to display
     */
    public ComponentBasedEmbedVaadinTomcat(EmbedComponentConfig config, Component component) {
        super(config);
        this.config = config;
        this.component = component;
    }

    public EmbedComponentConfig getConfig() {
        return config;
    }

    @Override
    protected void configure() {
        initConfiguration();

        // Setup vaadin servlet
        final Wrapper wrapper = initializeVaadinServlet(
                new DevApplicationServlet(this, component));
        // Silly but otherwise the parent class will complain
        wrapper.addInitParameter("application", DevApplication.class.getName());
    }
}
