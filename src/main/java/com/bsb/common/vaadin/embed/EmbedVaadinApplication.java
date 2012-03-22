package com.bsb.common.vaadin.embed;

import com.vaadin.Application;

/**
 * A builder for a server embedding an application.
 *
 * @author Stephane Nicoll
 */
public class EmbedVaadinApplication extends EmbedVaadinServerBuilder<EmbedVaadinApplication, EmbedVaadinServer> {

    private final Class<? extends Application> applicationClass;

    public EmbedVaadinApplication(Class<? extends Application> applicationClass) {
        super(true);
        assertNotNull(applicationClass, "applicationClass could not be null.");
        this.applicationClass = applicationClass;
    }

    /**
     * Returns the {@link Application} type that was used to initialize this instance, if any.
     *
     * @return the application class or <tt>null</tt> if a component was set
     */
    protected Class<? extends Application> getApplicationClass() {
        return applicationClass;
    }

    @Override
    protected EmbedVaadinApplication self() {
        return this;
    }

    @Override
    public EmbedVaadinServer build() {
        return new ApplicationBasedEmbedVaadinTomcat(getConfig(), getApplicationClass());
    }
}
