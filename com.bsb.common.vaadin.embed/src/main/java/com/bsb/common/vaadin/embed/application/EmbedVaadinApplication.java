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
import com.vaadin.ui.Root;

import java.util.Properties;

/**
 * A builder for a server embedding an application.
 *
 * @author Stephane Nicoll
 */
public class EmbedVaadinApplication extends EmbedVaadinServerBuilder<EmbedVaadinApplication, EmbedVaadinServer> {

    private final Class<? extends Root> rootClass;
    private EmbedVaadinConfig config;

    /**
     * Creates a new instance for the specified root.
     *
     * @param rootClass the class of the root to deploy
     */
    public EmbedVaadinApplication(Class<? extends Root> rootClass) {
        super();
        assertNotNull(rootClass, "rootClass could not be null.");
        this.rootClass = rootClass;
        withConfigProperties(EmbedVaadinConfig.loadProperties());
    }

    /**
     * Returns the {@link Root} type that was used to initialize this instance, if any.
     *
     * @return the root class or <tt>null</tt> if a component was set
     */
    protected Class<? extends Root> getRootClass() {
        return rootClass;
    }

    @Override
    protected EmbedVaadinApplication self() {
        return this;
    }

    @Override
    public EmbedVaadinServer build() {
        return new ApplicationBasedEmbedVaadinTomcat(getConfig(), getRootClass());
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
