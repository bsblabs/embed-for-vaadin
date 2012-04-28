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

import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.WrappedRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Root;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

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
     * Wraps the specified {@link Component} into a Vaadin application.
     *
     * @param component the component to wrap
     * @return an application displaying that component
     * @see #wrapLayout(com.vaadin.ui.Layout)
     * @see #wrapRoot(com.vaadin.ui.Root)
     */
    public DevApplication wrap(Component component) {
        if (component instanceof Root) {
            return wrapRoot((Root) component);
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
    public DevApplication wrapLayout(Layout layout) {
        // TODO: add a header to switch the style, etc
        // TODO: add bookmark to set the style
        final Root root;
        if (server.getConfig().isDevelopmentHeader()) {
            final VerticalSplitPanel mainLayout = new VerticalSplitPanel();
            mainLayout.setSizeFull();
            mainLayout.setSplitPosition(20, Sizeable.Unit.PIXELS);
            mainLayout.setLocked(true);

            final DevApplicationHeader header = new DevApplicationHeader(server);
            header.setSpacing(true);
            mainLayout.setFirstComponent(header);

            mainLayout.setSecondComponent(layout);

            root = new DevRoot(mainLayout);
        } else {
            root = new DevRoot(layout);
        }

        return new DevApplication(server, root);
    }

    /**
     * Wraps a {@link Root} into a Vaadin application.
     *
     * @param root the root to wrap
     * @return an application using that root as primary root
     */
    public DevApplication wrapRoot(Root root) {
        return new DevApplication(server, root);
    }

    /**
     * A development {@link Root} that displays a simple layout.
     *
     * @author Stephane Nicoll
     */
    @SuppressWarnings("serial")
    static class DevRoot extends Root {


        /**
         * Creates a new instance.
         *
         * @param content the content of the root
         */
        public DevRoot(ComponentContainer content) {
            setContent(content);
        }

        @Override
        protected void init(WrappedRequest request) {

        }

    }

}
