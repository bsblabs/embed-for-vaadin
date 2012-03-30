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
package com.bsb.common.vaadin.embed.application;

import com.bsb.common.vaadin.embed.AbstractEmbedVaadinTomcat;
import com.bsb.common.vaadin.embed.EmbedVaadinConfig;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import org.apache.catalina.Wrapper;

/**
 * A {@link com.bsb.common.vaadin.embed.EmbedVaadinServer} implementation that
 * deploys a complete application.
 * <p/>
 * Can be used to deploy a regular application from the IDE.
 *
 * @author Stephane Nicoll
 */
public class ApplicationBasedEmbedVaadinTomcat extends AbstractEmbedVaadinTomcat {

    private final Class<? extends Application> applicationClass;

    /**
     * Creates a new instance.
     *
     * @param config the config to use
     * @param applicationClass the class of the application to handle
     */
    public ApplicationBasedEmbedVaadinTomcat(EmbedVaadinConfig config,
                                             Class<? extends Application> applicationClass) {
        super(config);
        this.applicationClass = applicationClass;
    }

    @Override
    protected void configure() {
        initConfiguration();
        // Setup vaadin servlet
        final Wrapper wrapper = initializeVaadinServlet(new ApplicationServlet());
        wrapper.addInitParameter("application", applicationClass.getName());
    }
}
