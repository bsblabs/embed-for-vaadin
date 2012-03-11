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
import com.vaadin.Application;
import com.vaadin.ui.Window;

/**
 * A development application that displays a simple layout.
 *
 * @author Stephane Nicoll
 */
public class DevApplication extends Application {

    private final transient EmbedVaadinConfig config;
    private final Window mainWindow;

    /**
     * Creates a new instance.
     *
     * @param config the config to use
     * @param mainWindow the main window
     */
    public DevApplication(EmbedVaadinConfig config, Window mainWindow) {
        this.config = config;
        this.mainWindow = mainWindow;
    }

    @Override
    public void init() {
        setTheme(config.getTheme());

        setMainWindow(mainWindow);
    }

    /**
     * Returns the main {@link Window} that is used by this application.
     *
     * @return the main window
     */
    public Window getMainWindow() {
        return mainWindow;
    }
}
