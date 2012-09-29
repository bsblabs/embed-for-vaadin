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

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import java.util.Collections;
import java.util.List;

/**
 * A simple development {@link VaadinServlet} that takes the component
 * to display.
 * <p/>
 * Since this cannot create a new instance of the component, this mode does
 * not support session, multi tabs or application refresh. Will show
 * that component only.
 *
 * @author Stephane Nicoll
 */
public class DevApplicationServlet extends VaadinServlet {

    private static final long serialVersionUID = -5557485761575067731L;

    private final UI ui;
    private final String theme;

    /**
     * Creates a new instance.
     *
     * @param server the server handling this application
     * @param component the component to display
     */
    public DevApplicationServlet(ComponentBasedVaadinServer server, Component component) {
        this.ui = new ComponentWrapper(server).wrap(component);
        this.theme = server.getConfig().getTheme();
    }

    protected VaadinServletService createServletService(
            DeploymentConfiguration deploymentConfiguration) {
        return new DevVaadinServletService(this, deploymentConfiguration);
    }

    @SuppressWarnings("serial")
    private class DevVaadinServletService extends VaadinServletService {

        private final List<UIProvider> uiProviders;

        private DevVaadinServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration) {
            super(servlet, deploymentConfiguration);
            final UIProvider provider = new DevUIProvider(ui, theme);
            this.uiProviders = Collections.singletonList(provider);
        }

        @Override
        public List<UIProvider> getUIProviders(VaadinSession session) {
            return uiProviders;
        }
    }

}
