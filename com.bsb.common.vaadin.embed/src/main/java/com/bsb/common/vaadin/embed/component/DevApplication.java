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

import com.vaadin.Application;
import com.vaadin.RootRequiresMoreInformationException;
import com.vaadin.terminal.WrappedRequest;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Root;

/**
 * A development application that displays a simple layout.
 *
 * @author Stephane Nicoll
 */
@SuppressWarnings("serial")
public class DevApplication extends Application {

    private final String theme;
    private final Root root;

    /**
     * Creates a new instance.
     *
     * @param server the server handling this application
     * @param root the root
     */
    public DevApplication(ComponentBasedVaadinServer server, Root root) {
        this.theme = server.getConfig().getTheme();
        this.root = root;
    }

    @Override
    protected Root getRoot(WrappedRequest request) throws RootRequiresMoreInformationException {
        return root;
    }

    @Override
    public String getThemeForRoot(Root root) {
        return theme;
    }

    /**
     * Returns the {@link Root} used by the application.
     *
     * @return the root
     */
    public Root getRoot() {
        return root;
    }

    /**
     * Returns the main content that is used by the application.
     *
     * @return the main content
     */
    public ComponentContainer getMainContent() {
        return getRoot().getContent();
    }

}
