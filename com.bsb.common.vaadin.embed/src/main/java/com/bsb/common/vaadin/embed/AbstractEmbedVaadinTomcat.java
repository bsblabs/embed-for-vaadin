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
package com.bsb.common.vaadin.embed;

import com.bsb.common.vaadin.embed.util.BrowserUtils;
import com.google.common.io.Files;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Serializable;

/**
 * A base {@link EmbedVaadinServer} implementation based on Apache Tomcat.
 *
 * @author Stephane Nicoll
 */
public abstract class AbstractEmbedVaadinTomcat implements EmbedVaadinServer, Serializable {

    public static final String PRODUCTION_MODE_PARAM = "productionMode";

    private static final long serialVersionUID = 8211718040277785632L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractEmbedVaadinTomcat.class);

    private final EmbedVaadinConfig config;

    // Note this class has to be serializable to make sure the session can be saved when
    // Tomcat stops

    private final transient Tomcat tomcat;
    private final transient Thread shutdownThread;
    private final transient File baseDir;
    private transient Context context;

    /**
     * Creates a new instance with the specified config.
     *
     * @param config the config to use
     */
    protected AbstractEmbedVaadinTomcat(EmbedVaadinConfig config) {
        this.config = config;

        this.tomcat = new Tomcat();
        this.shutdownThread = new TomcatShutdownHook(tomcat);
        this.baseDir = Files.createTempDir();
    }

    /**
     * Configures the current {@link Tomcat} instance with the current
     * {@link EmbedVaadinConfig config}.
     * <p/>
     * Port, context and servlet configuration(s) should be added here. Consider
     * calling {@link #initConfiguration()} to perform a basic initialization
     * of the tomcat server.
     *
     * @see #getTomcat()
     * @see #getConfig()
     * @see #initConfiguration()
     * @see #initializeVaadinServlet(com.vaadin.terminal.gwt.server.AbstractApplicationServlet)
     */
    protected abstract void configure();

    public void start() {
        configure();
        try {
            doStart();
            addShutdownHook();
        } catch (LifecycleException e) {
            throw new IllegalStateException("Failed to start tomcat", e);
        }
    }

    public boolean isWaiting() {
        return config.isWaiting();
    }

    public EmbedVaadinConfig getConfig() {
        return config;
    }

    public void stop() {
        try {
            doStop();
        } catch (LifecycleException e) {
            logger.warn("Failed to stop tomcat", e);
        } finally {
            // Prevents a second stop when the VM exit since we already stopped it manually
            removeShutdownHook();
        }
    }

    /**
     * Returns the {@link Tomcat} instance.
     */
    protected Tomcat getTomcat() {
        return tomcat;
    }

    /**
     * Returns the {@link Context} to use for the webapp.
     */
    protected Context getContext() {
        if (context == null) {
            context = getTomcat().addContext(getConfig().getContextPath(),
                    getConfig().getContextRootDirectory().getAbsolutePath());
        }
        return context;
    }

    /**
     * Adds the basic and default configuration of the the current {@link Tomcat}
     * instance with the current {@link EmbedVaadinConfig config}.
     * <p/>
     * Does not map any servlet, though. Sub-classes are responsible to do this
     */
    protected void initConfiguration() {
        getTomcat().setBaseDir(baseDir.getAbsolutePath());

        // Setup HTTP port listening
        getTomcat().setPort(getConfig().getPort());

        // Setup vaadin production mode
        getContext().addParameter(PRODUCTION_MODE_PARAM, String.valueOf(getConfig().isProductionMode()));
    }

    /**
     * Initializes the vaadin servlet and maps it to <tt>/*</tt>.
     * <p/>
     * Returns the associated {@link Wrapper} for further customization.
     *
     * @param servlet the servlet to use to handle vaadin calls.
     * @param <T> the type of the servlet
     * @return the created wrapper for the servlet
     */
    protected <T extends AbstractApplicationServlet> Wrapper initializeVaadinServlet(T servlet) {
        // Setup vaadin servlet
        final Wrapper wrapper = Tomcat.addServlet(getContext(),
                "vaadin", servlet);
        if (getConfig().getWidgetSet() != null) {
            wrapper.addInitParameter("widgetset", getConfig().getWidgetSet());
        }

        wrapper.addMapping("/*");
        return wrapper;
    }

    // private helpers

    /**
     * Fires the embedded tomcat that is assumed to be fully configured.
     *
     * @throws LifecycleException if tomcat failed to start
     */
    private void doStart() throws LifecycleException {
        logger.info("Deploying application to [" + getConfig().getDeployUrl() + "]");
        tomcat.start();

        // Let's set the port that was used to actually start the application if necessary
        if (getConfig().getPort() == EmbedVaadinConfig.DEFAULT_PORT) {
            getConfig().setPort(getTomcat().getConnector().getLocalPort());
        }

        logger.info("Application has been deployed to [" + getConfig().getDeployUrl() + "]");
        if (config.shouldOpenBrowser()) {
            BrowserUtils.openBrowser(getConfig().getOpenBrowserUrl());
        }
        if (isWaiting()) {
            tomcat.getServer().await();
        }
    }

    /**
     * Stops the embedded tomcat.
     *
     * @throws LifecycleException if tomcat failed to stop
     */
    private void doStop() throws LifecycleException {
        logger.info("Stopping tomcat.");
        long startTime = System.currentTimeMillis();
        tomcat.stop();
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Tomcat shutdown finished in " + duration + " ms.");
    }

    /**
     * Adds a  shutdown hook to stop the server when the JVM is stopped.
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

    /**
     * Removes the shutdown hook in case the server is stopped manually
     */
    private void removeShutdownHook() {
        Runtime.getRuntime().removeShutdownHook(shutdownThread);
    }

    private static final class TomcatShutdownHook extends Thread {

        private final Tomcat tomcat;

        private TomcatShutdownHook(Tomcat tomcat) {
            this.tomcat = tomcat;
        }

        /**
         * Stops the server when the VM exits.
         */
        public void run() {
            try {
                tomcat.stop();
                logger.info("Stopped Tomcat");
            } catch (LifecycleException e) {
                logger.warn("Failed to stop Tomcat", e);
            }
        }
    }
}
