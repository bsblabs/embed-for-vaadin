package com.bsb.common.vaadin.embed;

import com.vaadin.ui.Component;

/**
 * A builder for a server embedding a component.
 *
 * @author Stephane Nicoll
 */
public class EmbedVaadinComponent extends EmbedVaadinServerBuilder<EmbedVaadinComponent, EmbedVaadinServer> {

    private final Component component;

    public EmbedVaadinComponent(Component component) {
        super(true);
        assertNotNull(component, "component could not be null.");
        this.component = component;
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
    public EmbedVaadinServer build() {
        return new ComponentBasedEmbedVaadinTomcat(getConfig(), getComponent());
    }
}
