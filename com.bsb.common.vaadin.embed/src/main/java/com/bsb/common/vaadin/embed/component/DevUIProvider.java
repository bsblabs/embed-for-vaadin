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

import com.bsb.common.vaadin.embed.component.ComponentWrapper.DevUI;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 * A specialized {@link UIProvider} that returns a configurable UI and theme.
 *
 * @author Stephane Nicoll
 */
@SuppressWarnings("serial")
final class DevUIProvider extends UIProvider {


    private final String theme;
	private final PushMode pushMode;

	ComponentBasedVaadinServer server;
	Component component;

    /**
     * Creates a new instance.
     *
     * @param ui the ui to use, regardless of the event
     * @param theme the theme to use, regardless of the event
     */
	DevUIProvider(ComponentBasedVaadinServer server, Component component, String theme,
			PushMode pushMode) {

        this.theme = theme;
		this.server = server;
		this.component = component;
		this.pushMode = pushMode;
    }

    @Override
    public UI createInstance(UICreateEvent event) {
		return new ComponentWrapper(server).wrap(component);
    }

    @Override
    public String getTheme(UICreateEvent event) {
        return theme;
    }

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		return DevUI.class;
    }

	@Override
	public PushMode getPushMode(UICreateEvent event) {
		return pushMode;
	}
}
