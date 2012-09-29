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

import com.bsb.common.vaadin.embed.EmbedVaadinServer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.themes.BaseTheme;

/**
 * A header added to an application to offer various development-related tools.
 *
 * @author Stephane Nicoll
 */
@SuppressWarnings("serial")
public class DevApplicationHeader extends HorizontalLayout {

    /**
     * Creates an new instance with the specified {@link EmbedVaadinServer}
     * to manage.
     *
     * @param server the server to manage
     */
    public DevApplicationHeader(final EmbedVaadinServer server) {
        final Button shutdown = new Button("shutdown");
        shutdown.setStyleName(BaseTheme.BUTTON_LINK);
        shutdown.setDescription("Shutdown the embed server and close this tab");
        addComponent(shutdown);
        setComponentAlignment(shutdown, Alignment.MIDDLE_CENTER);


        shutdown.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                // Stop the server in a separate thread.
                final Thread thread = new Thread(new Runnable() {
                    public void run() {
                        server.stop();
                    }
                });
                // avoid that catalina's WebappClassLoader.clearReferencesThreads warns about the thread because it is
                // part of the web application being stopped.
                thread.setContextClassLoader(null);

                thread.start();

                // Close the browser tab
                JavaScript.getCurrent().execute("close();");
            }
        });
    }
}
