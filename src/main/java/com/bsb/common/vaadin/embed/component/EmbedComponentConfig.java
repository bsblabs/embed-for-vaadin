package com.bsb.common.vaadin.embed.component;

import com.bsb.common.vaadin.embed.EmbedVaadinConfig;

import java.util.Properties;

/**
 * Configuration for an embedded server showing a component.
 * <p>
 * This extends the {@link EmbedVaadinConfig} and is configured in the same way.
 * The properties file may contain any of the properties documented on {@link EmbedVaadinConfig} and
 * additionally:
 * <ul>
 * <li><tt>vaadin.theme</tt>: to specify the theme to use for the vaadin application</li>
 * </ul>
 * 
 * @author Wouter Coekaerts
 */
public final class EmbedComponentConfig extends EmbedVaadinConfig {
    /**
     * The default theme if none is set.
     */
    public static final String DEFAULT_THEME = "reindeer";
    
    private String theme;

    protected EmbedComponentConfig(Properties properties) {
        super(properties);
        theme = properties.getProperty("vaadin.theme", DEFAULT_THEME);
    }

    protected EmbedComponentConfig(EmbedComponentConfig clone) {
        super(clone);
        theme = clone.theme;
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
