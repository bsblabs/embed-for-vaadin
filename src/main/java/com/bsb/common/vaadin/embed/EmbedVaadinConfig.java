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
package com.bsb.common.vaadin.embed;

import com.google.common.base.Objects;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 * Configuration for an embedded server. Provides sensitive default that can
 * be overridden on a module-per-module basis by placing a properties files
 * named "embed-vaadin.properties" at the root of the classpath.
 * <p/>
 * This properties file may hold the following:
 * <ul>
 * <li><tt>server.port</tt>: to specify the HTTP port to use</li>
 * <li><tt>context.path</tt>: to specify the context path of the deployed application</li>
 * <li><tt>context.rootDir</tt>: to specify the root directory of the web application</li>
 * <li><tt>server.await</tt>: to specify if the thread should block when the server has started</li>
 * <li><tt>vaadin.widgetSet</tt>: to specify the widgetSet to use for the vaadin application</li>
 * </ul>
 *
 * @author Stephane Nicoll
 */
public class EmbedVaadinConfig implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(EmbedVaadinConfig.class);

    public static final String CONFIG_LOCATION = "/embed-vaadin.properties";

    /**
     * The default HTTP port to use if none is set. By default, an available port
     * is taken
     */
    public static final int DEFAULT_PORT = 0;

    /**
     * The default context path to use, by default uses the root context path.
     */
    public static final String DEFAULT_CONTEXT_PATH = "";

    /**
     * Blocks the thread that has started the server by default.
     */
    public static final boolean DEFAULT_WAITING = true;

    /**
     * Do not start the browser by default.
     */
    public static final boolean DEFAULT_START_BROWSER = false;


    private int port;
    private String contextPath;
    private File contextRootDirectory;
    private boolean waiting;

    private String widgetSet;

    private boolean openBrowser;

    /**
     * Creates a new instance using the configuration in the given {@link Properties}
     * 
     * @param properties configuration properties
     */
    public EmbedVaadinConfig(Properties properties) {
        port = Integer.valueOf(properties.getProperty("server.port", String.valueOf(DEFAULT_PORT)));
        contextPath = properties.getProperty("context.path", DEFAULT_CONTEXT_PATH);
        final String contextBase = properties.getProperty("context.rootDir");
        if (contextBase == null) {
            contextRootDirectory = Files.createTempDir();
        } else {
            contextRootDirectory = new File(contextBase);
        }
        waiting = Boolean.valueOf(properties.getProperty("server.await", String.valueOf(DEFAULT_WAITING)));

        widgetSet = properties.getProperty("vaadin.widgetSet");

        openBrowser = Boolean.valueOf(properties.getProperty("open.browser", String.valueOf(DEFAULT_START_BROWSER)));

        logger.debug("Using " + this);

        // Validate
        validate();
    }

    /**
     * Creates a deep copy based on the specified <tt>clone</tt>.
     *
     * @param clone the instance to clone
     */
    protected EmbedVaadinConfig(EmbedVaadinConfig clone) {
        this.port = clone.port;
        this.contextPath = clone.contextPath;
        this.contextRootDirectory = clone.contextRootDirectory;
        this.waiting = clone.waiting;
        this.widgetSet = clone.widgetSet;
        this.openBrowser = clone.openBrowser;
    }

    /**
     * Creates a new instance with the default settings, i.e. does not attempt
     * to load customized settings from the {@link #CONFIG_LOCATION config location}.
     *
     * @return a new instance with the default settings
     */
    public static EmbedVaadinConfig defaultConfig() {
        return new EmbedVaadinConfig(new Properties());
    }

    /**
     * Returns the port to run the HTTP server on. If none is set
     * an available port is used. If you want a fixed port, please
     * specify a port.
     *
     * @return the http port to use
     * @see #DEFAULT_PORT
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns the context path, the location where the context will be exposed on the http server.
     * <p/>
     * For example if it's <code>/foo</code>, you could reach the application
     * at <code>http://localhost:port/foo</code>
     * <p/>
     * If none is set, the application is deployed in the root context
     *
     * @return the context path to use, an empty string to use the root context
     * @see #DEFAULT_CONTEXT_PATH
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * Returns the root directory for the deployed web application. This is not useful
     * unless you want to deploy static resources or themes as part of the application.
     * <p/>
     * This could be an absolute path or a path relative to the <tt>working</tt>
     * directory, for example <code>target/webapp</code>.
     * <p/>
     * If none is set, a default temporary directory is used
     *
     * @return the root directory for the web application
     */
    public File getContextRootDirectory() {
        return contextRootDirectory;
    }

    /**
     * Specifies if the server should block on startup or not.
     *
     * @return <tt>true</tt> to block the thread once the server has been started
     */
    public boolean isWaiting() {
        return waiting;
    }

    /**
     * Returns the vaadin widgetSet to use for the application. Returns <tt>null</tt>
     * if no specific widgetSet is configured and the default one should be used.
     *
     * @return the widgetSet to use or <tt>null</tt> if none is set
     */
    public String getWidgetSet() {
        return widgetSet;
    }

    /**
     * Specifies if the browser should be opened on startup or not.
     *
     * @return <tt>true</tt> to open the browser once the server has been started
     */
    public boolean shouldOpenBrowser() {
        return openBrowser;
    }

    void setPort(int port) {
        this.port = port;
    }

    void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    void setContextRootDirectory(File contextRootDirectory) {
        this.contextRootDirectory = contextRootDirectory;
    }

    void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    void setWidgetSet(String widgetSet) {
        this.widgetSet = widgetSet;
    }

    void setOpenBrowser(boolean openBrowser) {
        this.openBrowser = openBrowser;
    }

    private void validate() {
        if (!contextRootDirectory.exists()) {
            throw new IllegalStateException("Cannot find file [" + contextRootDirectory.getAbsolutePath() + "]. "
                    + "Hint: Make sure the active working directory is the root of the right module");
        }
    }

    /**
     * Loads a configuration file from the default {@link #CONFIG_LOCATION config location}.
     * <p/>
     * If no file is found, only the standard default are available.
     *
     * @return Properties loaded from the default locations, or empty Properties if the file doesn't exist.
     * @see #CONFIG_LOCATION
     */
    public static Properties loadProperties() {
        return loadProperties(CONFIG_LOCATION, false);
    }

    /**
     * Loads a configuration file from the specified location. Fails if the
     * specified <tt>path</tt> does not exist.
     *
     * @param path the location of a properties file in the classpath
     * @return Properties Properties loaded from the <code>.properties</code> file at the specified location
     * @throws IllegalStateException if no such properties file is found
     */
    public static Properties loadProperties(String path) {
        return loadProperties(path, true);
    }
    
    private static Properties loadProperties(String path, boolean failIfNotFound) {
        try {
            final Properties properties = new Properties();
            final InputStream stream = EmbedVaadinConfig.class.getResourceAsStream(path);
            if (stream != null) {
                properties.load(stream);
            } else if (failIfNotFound) {
                throw new IllegalStateException("Cannot find [" + path + "]. " +
                        "Hint: make sure that the file is in your classpath and the path starts with a forward slash.");
            }
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read configuration at [" + path + "]", e);
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("port", port)
                .add("context", contextPath).add("webapp dir", contextRootDirectory)
                .toString();
    }

}
