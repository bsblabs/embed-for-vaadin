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
import com.bsb.common.vaadin.embed.EmbedVaadinServer;
import com.google.common.io.Files;
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * A based {@link EmbedVaadinServer} implementation based on Apache Tomcat.
 *
 * @author Stephane Nicoll
 */
public abstract class AbstractEmbedVaadinTomcat implements EmbedVaadinServer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractEmbedVaadinTomcat.class);

    private final EmbedVaadinConfig config;

    private final Tomcat tomcat;
    private final Thread shutdownThread;
    private final File baseDir;
    private Context context;

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
     * Port, context and servlet configuration(s) are added here. By default,
     * the vaadin servlet is added and mapped to the root context.
     *
     * @see #getTomcat()
     * @see #getConfig()
     * @see #initConfiguration()
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
     * Returns the configuration.
     */
    protected EmbedVaadinConfig getConfig() {
        return config;
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
    protected <T extends ApplicationServlet> Wrapper initializeVaadinServlet(T servlet) {
        // Setup vaadin servlet
        final Wrapper wrapper = Tomcat.addServlet(getContext(),
                "vaadin", servlet);
        // TODO: this is where we would add our custom widgetset

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
        logger.info("Deploying application to [" + getDeployUrl() + "]");
        tomcat.start();
        logger.info("Application has been deployed to [" + getDeployUrl() + "]");
        if (config.shouldOpenBrowser()) {
            BrowserUtils.openBrowser(getDeployUrl());
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
        logger.info("Tomcat shutdown finished in " + (duration / 1000) + " seconds.");
    }

    /**
     * Returns the full url of the application, according to the port and
     * context path.
     *
     * @return the url of the web application
     */
    protected String getDeployUrl() {
        final StringBuilder sb = new StringBuilder();

        sb.append("http://localhost:");
        // This may be called while tomcat is not started yet
        final int localPort = getTomcat().getConnector().getLocalPort();
        if (localPort == -1) {
            sb.append("[auto]");
        } else {
            sb.append(localPort);
        }
        if (config.getContextPath().isEmpty()) {
            sb.append("/");
        } else {
            sb.append(config.getContextPath());
        }
        return sb.toString();
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
