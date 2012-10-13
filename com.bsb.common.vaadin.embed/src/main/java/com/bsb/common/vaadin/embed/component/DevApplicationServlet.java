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
import com.vaadin.server.ServiceException;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServiceSession;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
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
@SuppressWarnings("serial")
public class DevApplicationServlet extends VaadinServlet {

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

    private final class DevVaadinServletService extends VaadinServletService {

        private DevVaadinServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration) {
            super(servlet, deploymentConfiguration);
        }

        @Override
        protected VaadinServiceSession createVaadinSession(VaadinRequest request) throws ServiceException {
            return new DevVaadinServiceSession(this);
        }
    }

    private final class DevVaadinServiceSession extends VaadinServiceSession {

        private final List<UIProvider> uiProviders;

        private DevVaadinServiceSession(VaadinService service) {
            super(service);
            final UIProvider provider = new DevUIProvider(ui, theme);
            this.uiProviders = Collections.singletonList(provider);
        }

        @Override
        public List<UIProvider> getUIProviders() {
            return uiProviders;
        }
    }

}
