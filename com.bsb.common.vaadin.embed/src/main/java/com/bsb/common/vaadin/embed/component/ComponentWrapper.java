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

import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;

/**
 * Wraps a component into an actual application.
 *
 * @author Stephane Nicoll
 */
public class ComponentWrapper {

    private final ComponentBasedVaadinServer server;

    /**
     * Creates a new instance.
     *
     * @param server the server handling this application
     */
    public ComponentWrapper(ComponentBasedVaadinServer server) {
        this.server = server;
    }

    /**
     * Wraps the specified {@link Component} into a UI
     *
     * @param component the component to wrap
     * @return an UI displaying that component
     * @see #wrapLayout(com.vaadin.ui.Layout)
     */
    public UI wrap(Component component) {
        if (component instanceof UI) {
            return (UI) component;
        }
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
    public UI wrapLayout(Layout layout) {
        // TODO: add a header to switch the style, etc
        // TODO: add bookmark to set the style
        if (server.getConfig().isDevelopmentHeader()) {
            final VerticalSplitPanel mainLayout = new VerticalSplitPanel();
            mainLayout.setSizeFull();
            mainLayout.setSplitPosition(20, Sizeable.Unit.PIXELS);
            mainLayout.setLocked(true);

            final DevApplicationHeader header = new DevApplicationHeader(server);
            header.setSpacing(true);
            mainLayout.setFirstComponent(header);

            mainLayout.setSecondComponent(layout);

            return new DevUI(mainLayout);
        } else {
            return new DevUI(layout);
        }
    }

    public UI wrapWindow(Window window) {
        final UI ui = wrapLayout(new VerticalLayout());
        ui.addWindow(window);
        return ui;
    }

    /**
     * A development {@link UI} that displays a simple layout.
     *
     * @author Stephane Nicoll
     */
    @SuppressWarnings("serial")
    static class DevUI extends UI {

        /**
         * Creates a new instance.
         *
         * @param content the content of the UI
         */
        public DevUI(ComponentContainer content) {
            setContent(content);
        }

        @Override
        protected void init(VaadinRequest vaadinRequest) {
        }

    }

}
