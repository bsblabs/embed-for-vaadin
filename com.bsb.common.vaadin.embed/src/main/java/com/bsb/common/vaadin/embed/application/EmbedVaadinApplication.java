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
import com.vaadin.Application;

import java.util.Properties;

/**
 * A builder for a server embedding an application.
 *
 * @author Stephane Nicoll
 */
public class EmbedVaadinApplication extends EmbedVaadinServerBuilder<EmbedVaadinApplication, EmbedVaadinServer> {

    private final Class<? extends Application> applicationClass;
    private EmbedVaadinConfig config;

    /**
     * Creates a new instance for the specified application.
     *
     * @param applicationClass the class of the application to deploy
     */
    public EmbedVaadinApplication(Class<? extends Application> applicationClass) {
        super();
        assertNotNull(applicationClass, "applicationClass could not be null.");
        this.applicationClass = applicationClass;
        withConfigProperties(EmbedVaadinConfig.loadProperties());
    }

    /**
     * Returns the {@link Application} type that was used to initialize this instance, if any.
     *
     * @return the application class or <tt>null</tt> if a component was set
     */
    protected Class<? extends Application> getApplicationClass() {
        return applicationClass;
    }

    @Override
    protected EmbedVaadinApplication self() {
        return this;
    }

    @Override
    public EmbedVaadinServer build() {
        return new ApplicationBasedEmbedVaadinTomcat(getConfig(), getApplicationClass());
    }

    @Override
    public final EmbedVaadinApplication withConfigProperties(Properties properties) {
        this.config = new EmbedVaadinConfig(properties);
        return self();
    }

    @Override
    protected EmbedVaadinConfig getConfig() {
        return config;
    }
}
