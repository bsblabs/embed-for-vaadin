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

import java.util.Properties;

import com.bsb.common.vaadin.embed.EmbedVaadinConfig;
import com.vaadin.shared.communication.PushMode;

/**
 * Configuration for an embedded server showing a component.
 * <p/>
 * This extends the {@link EmbedVaadinConfig} and is configured in the same way.
 * The properties file may contain any of the properties documented on {@link EmbedVaadinConfig} and
 * additionally:
 * <ul>
 * <li><tt>vaadin.theme</tt>: to specify the theme to use for the vaadin application</li>
 * <li><tt>development.header</tt>: to specify if a development header should be added to the application</li>
 * </ul>
 *
 * @author Wouter Coekaerts
 */
public final class EmbedComponentConfig extends EmbedVaadinConfig {

    private static final long serialVersionUID = -1058773155435290313L;

    /**
     * The key defining the theme to use for the created vaadin application.
     */
    public static final String KEY_THEME = "vaadin.theme";

    /**
     * The default theme if none is set.
     */
    public static final String DEFAULT_THEME = "reindeer";

    /**
     * The key defining if the development header should be displayed. Holds a boolean.
     */
    public static final String KEY_DEVELOPMENT_HEADER = "development.header";

    /**
     * By default the development header is not added to the application.
     */
    public static final boolean DEFAULT_DEVELOPMENT_HEADER = false;

    private String theme;
	private PushMode pushMode;
    private boolean developmentHeader;

    protected EmbedComponentConfig(Properties properties) {
        super(properties);
        theme = properties.getProperty(KEY_THEME, DEFAULT_THEME);
        developmentHeader = Boolean.valueOf(properties.getProperty(KEY_DEVELOPMENT_HEADER,
                String.valueOf(DEFAULT_DEVELOPMENT_HEADER)));
		pushMode = PushMode.DISABLED;
    }

    protected EmbedComponentConfig(EmbedComponentConfig clone) {
        super(clone);
        theme = clone.theme;
        developmentHeader = clone.developmentHeader;
    }

    /**
     * Returns the vaadin theme to use for the application. Only taken into account
     * when a wrapper application is created for a component.
     *
     * @return the theme to use
     * @see #DEFAULT_THEME
     */
    public String getTheme() {
        return theme;
    }

    void setTheme(String theme) {
        this.theme = theme;
    }

	public PushMode getPushMode() {
		return pushMode;
	}

	public void setPushMode(PushMode pushMode) {
		this.pushMode = pushMode;
	}

    /**
     * Specifies if the development header is added automatically to the
     * created application.
     *
     * @return <tt>true</tt> if the development header is added
     */
    public boolean isDevelopmentHeader() {
        return developmentHeader;
    }

    void setDevelopmentHeader(boolean developmentHeader) {
        this.developmentHeader = developmentHeader;
    }

    /**
     * Creates a new instance with the default settings, i.e. does not attempt
     * to load customized settings from the {@link #CONFIG_LOCATION config location}.
     *
     * @return a new instance with the default settings
     */
    public static EmbedComponentConfig defaultConfig() {
        return new EmbedComponentConfig(new Properties());
    }
}
