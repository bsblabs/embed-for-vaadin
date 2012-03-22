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
package com.bsb.common.vaadin.embed;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.ui.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * A simple development {@link ApplicationServlet} that takes the component
 * to display.
 * <p/>
 * Since this cannot create a new instance of the component, this mode does
 * not support session, multi tabs or application refresh. Will show
 * that component only.
 *
 * @author Stephane Nicoll
 */
public class DevApplicationServlet extends ApplicationServlet {

    private final Application application;

    /**
     * Creates a new instance.
     *
     * @param config the config to use
     * @param component the component to display
     */
    public DevApplicationServlet(EmbedVaadinConfig config, Component component) {
        this.application = new ComponentWrapper(config).wrap(component);
    }

    @Override
    protected Application getNewApplication(HttpServletRequest request) throws ServletException {
        return application;
    }

    @Override
    protected Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
        return DevApplication.class;
    }
}
