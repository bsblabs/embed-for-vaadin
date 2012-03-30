package com.bsb.common.vaadin.embed.component;

import com.bsb.common.vaadin.embed.EmbedVaadinConfig;
import com.bsb.common.vaadin.embed.EmbedVaadinServer;
import com.bsb.common.vaadin.embed.EmbedVaadinServerBuilder;
import com.vaadin.ui.Component;

import java.util.Properties;

/**
 * A builder for a server embedding a component.
 *
 * @author Stephane Nicoll
 */
public class EmbedVaadinComponent extends EmbedVaadinServerBuilder<EmbedVaadinComponent, EmbedVaadinServer> {

    private final Component component;
    private EmbedComponentConfig config;

    public EmbedVaadinComponent(Component component) {
        super();
        assertNotNull(component, "component could not be null.");
        this.component = component;
        withConfigProperties(EmbedVaadinConfig.loadProperties());
    }

    /**
     * Specifies the vaadin theme to use for the application.
     *
     * @param theme the theme to use
     * @return this
     */
    public EmbedVaadinComponent withTheme(String theme) {
        assertNotNull(theme, "theme could not be null.");
        getConfig().setTheme(theme);
        return self();
    }

    /**
     * Returns the {@link Component} that was used to initialize this instance, if any.
     *
     * @return the component or <tt>null</tt> if an application was set
     */
    protected Component getComponent() {
        return component;
    }

    @Override
    protected EmbedVaadinComponent self() {
        return this;
    }

    @Override
    public ComponentBasedVaadinServer build() {
        return new ComponentBasedEmbedVaadinTomcat(config, getComponent());
    }

    @Override
    public EmbedVaadinComponent withConfigProperties(Properties properties) {
        this.config = new EmbedComponentConfig(properties);
        return self();
    }

    @Override
    protected EmbedComponentConfig getConfig() {
        return config;
    }
}
