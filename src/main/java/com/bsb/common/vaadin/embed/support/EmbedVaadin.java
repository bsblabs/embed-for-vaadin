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
package com.bsb.common.vaadin.embed.support;

import com.bsb.common.vaadin.embed.EmbedVaadinConfig;
import com.bsb.common.vaadin.embed.EmbedVaadinServer;
import com.bsb.common.vaadin.embed.EmbedVaadinServerBuilder;
import com.vaadin.Application;
import com.vaadin.ui.Component;

/**
 * Embeds vaadin in a web application deployed in the current VM.
 *
 * @author Stephane Nicoll
 */
public class EmbedVaadin extends EmbedVaadinServerBuilder<EmbedVaadin, EmbedVaadinServer> {

    private Component component;
    private Class<? extends Application> applicationClass;

    protected EmbedVaadin(Component component) {
        super(true);
        assertNotNull(component, "component could not be null.");
        this.component = component;
    }

    protected EmbedVaadin(Class<? extends Application> applicationClass) {
        super(true);
        assertNotNull(applicationClass, "applicationClass could not be null.");
        this.applicationClass = applicationClass;
    }

    /**
     * Creates a new instance to display the specified vaadin {@link Component}.
     * <p/>
     * The specified <tt>component</tt> could be of various types which will be
     * wrapped automatically if necessary as follows:
     * <ul>
     * <li><tt>Window</tt>: the window becomes the main window of the application</li>
     * <li><tt>Layout</tt>: a simple window is created using that layout</li>
     * <li>other: a <tt>VerticalLayout</tt> is created to hold the component</li>
     * </ul>
     *
     * @param component the component to use for the vaadin application
     * @return an instance handling that component
     */
    public static EmbedVaadin forComponent(Component component) {
        return new EmbedVaadin(component);
    }

    /**
     * Creates a new instance to manage a vaadin {@link Application} defined by the
     * specified class.
     * <p/>
     * This is an easy way to deploy a complete vaadin application locally from the
     * IDE.
     *
     * @param applicationClass the application class
     * @return an instance handling that component
     */
    public static EmbedVaadin forApplication(Class<? extends Application> applicationClass) {
        return new EmbedVaadin(applicationClass);
    }

    @Override
    protected EmbedVaadin self() {
        return this;
    }

    @Override
    protected EmbedVaadinConfig getConfig() {
        return super.getConfig();
    }

    /**
     * Returns the {@link Component} that was used to initialize this instance, if any.
     * 
     * @return the component or <tt>null</tt> if an application was set
     */
    protected Component getComponent() {
        return component;
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
    public EmbedVaadinServer build() {
        if (component != null) {
            return new ComponentBasedEmbedVaadinTomcat(getConfig(), component);
        } else if (applicationClass != null) {
            return new ApplicationBasedEmbedVaadinTomcat(getConfig(), applicationClass);
        } else {
            throw new IllegalStateException("Should not happen");
        }
    }

}
