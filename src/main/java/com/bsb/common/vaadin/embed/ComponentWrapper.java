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
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Wraps a component into an actual application.
 *
 * @author Stephane Nicoll
 */
public class ComponentWrapper {

    private final EmbedVaadinConfig config;

    /**
     * Creates a new instance.
     *
     * @param config the config to use
     */
    public ComponentWrapper(EmbedVaadinConfig config) {
        this.config = config;
    }

    /**
     * Wraps the specified {@link Component} into a Vaadin application.
     *
     * @param component the component to wrap
     * @return an application displaying that component
     * @see #wrapLayout(com.vaadin.ui.Layout)
     * @see #wrapWindow(com.vaadin.ui.Window)
     */
    public Application wrap(Component component) {
        if (component instanceof Window) {
            return wrapWindow((Window) component);
        }
        if (component instanceof Layout) {
            return wrapLayout((Layout) component);
        }

        // Ok it's a component we cannot handle directly
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeFull();
        layout.addComponent(component);
        layout.setExpandRatio(component, 1);
        return wrapLayout(layout);
    }

    /**
     * Wraps a {@link Layout} into a Vaadin application.
     *
     * @param layout the layout to wrap
     * @return an application displaying that layout
     */
    public Application wrapLayout(Layout layout) {
        // TODO: add a header to switch the style, etc
        // TODO: add bookmark to set tye style
        final Window mainWindow = new Window("Dev");
        mainWindow.setContent(layout);

        return new DevApplication(config, mainWindow);
    }

    /**
     * Wraps a {@link Window} into a Vaadin application.
     *
     * @param window the window to wrap
     * @return an application using that window as primary window
     */
    public Application wrapWindow(Window window) {
        return new DevApplication(config, window);
    }

}
