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

import com.bsb.common.vaadin.embed.util.PropertiesHelper;
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
 * <li><tt>vaadin.productionMode</tt>: to specify if the production mode should be enabled or not</li>
 * <li><tt>open.browser</tt>: to specify if the browser should be opened automatically</li>
 * </ul>
 *
 * @author Stephane Nicoll
 */
public class EmbedVaadinConfig implements Serializable {

    private static final long serialVersionUID = 2171821427648037241L;

    private static final Logger logger = LoggerFactory.getLogger(EmbedVaadinConfig.class);

    private static final int PORT_AUTO = 0;


    public static final String CONFIG_LOCATION = "/embed-vaadin.properties";

    /**
     * The key defining  the HTTP port to use.
     */
    public static final String KEY_PORT = "server.port";

    /**
     * The default HTTP port to use if none is set. By default, an available port
     * is taken. Holds an integer.
     */
    public static final int DEFAULT_PORT = PORT_AUTO;

    /**
     * The key defining the context path to use.
     */
    public static final String KEY_CONTEXT_PATH = "context.path";

    /**
     * The default context path to use, by default uses the root context path.
     */
    public static final String DEFAULT_CONTEXT_PATH = "";

    /**
     * The key defining the root directory to use for the web application.
     */
    public static final String KEY_CONTEXT_ROOT_DIR = "context.rootDir";

    /**
     * The key defining if the thread that started the server should be blocked. Holds a boolean.
     */
    public static final String KEY_WAITING = "server.await";

    /**
     * Blocks the thread that has started the server by default.
     */
    public static final boolean DEFAULT_WAITING = true;

    /**
     * The key defining a custom widget set to use.
     */
    public static final String KEY_WIDGET_SET = "vaadin.widgetSet";

    /**
     * The key defining the production mode. Holds a boolean.
     */
    public static final String KEY_PRODUCTION_MODE = "vaadin.productionMode";

    /**
     * Do not set production mode by default.
     */
    public static final boolean DEFAULT_PRODUCTION_MODE = false;

    /**
     * The key defining if the browser should be opened once the server has started. Holds a boolean.
     */
    public static final String KEY_OPEN_BROWSER = "open.browser";

    /**
     * Do not start the browser by default.
     */
    public static final boolean DEFAULT_OPEN_BROWSER = false;

    /**
     * The key defining if the browser should be opened at a custom url. Holds a String which could be
     * either a relative path, an absolute path or a full url.
     */
    public static final String KEY_CUSTOM_BROWSER_URL = "browser.customUrl";

    private int port;
    private String contextPath;
    private File contextRootDirectory;
    private boolean waiting;

    private String widgetSet;
    private boolean productionMode;

    private boolean openBrowser;
    private String customBrowserUrl;

    /**
     * Creates a new instance using the configuration in the given {@link Properties}
     *
     * @param properties configuration properties
     */
    public EmbedVaadinConfig(Properties properties) {
        final PropertiesHelper helper = new PropertiesHelper(properties);
        port = helper.getIntProperty(KEY_PORT, DEFAULT_PORT);
        setContextPath(properties.getProperty(KEY_CONTEXT_PATH, DEFAULT_CONTEXT_PATH));
        final String contextBase = properties.getProperty(KEY_CONTEXT_ROOT_DIR);
        if (contextBase == null) {
            contextRootDirectory = Files.createTempDir();
        } else {
            contextRootDirectory = new File(contextBase);
        }
        waiting = helper.getBooleanProperty(KEY_WAITING, DEFAULT_WAITING);

        widgetSet = properties.getProperty(KEY_WIDGET_SET);
        productionMode = helper.getBooleanProperty(KEY_PRODUCTION_MODE, DEFAULT_PRODUCTION_MODE);

        openBrowser = helper.getBooleanProperty(KEY_OPEN_BROWSER, DEFAULT_OPEN_BROWSER);
        customBrowserUrl = properties.getProperty(KEY_CUSTOM_BROWSER_URL);

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
        this.productionMode = clone.productionMode;
        this.openBrowser = clone.openBrowser;
        this.customBrowserUrl = clone.customBrowserUrl;
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
     * <p/>
     * When the server has been started and an available port should
     * be allocated, this would return the actual port being used
     * by the server.
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
     * Specifies if the production mode should be enabled or not.
     *
     * @return <tt>true</tt> to enable production mode
     * @see #DEFAULT_PRODUCTION_MODE
     */
    public boolean isProductionMode() {
        return productionMode;
    }

    /**
     * Specifies if the browser should be opened on startup or not.
     *
     * @return <tt>true</tt> to open the browser once the server has been started
     */
    public boolean shouldOpenBrowser() {
        return openBrowser;
    }

    /**
     * Returns any customization made to the url to use to open the browser or
     * <tt>null</tt> if the default url should be used.
     *
     * @return a custom browser url or <tt>null</tt> if none is specified
     */
    public String getCustomBrowserUrl() {
        return customBrowserUrl;
    }

    /**
     * Returns the full url of the application, according to the port and
     * context path.
     * <p/>
     * Note that if the application has not been started yet and an available
     * port should be automatically allocated, this would not return a usable
     * url.
     *
     * @return the url of the web application
     */
    public String getDeployUrl() {
        if (getContextPath().isEmpty()) {
            return buildUrl(getPort(), "/");
        } else {
            return buildUrl(getPort(), getContextPath());
        }
    }

    /**
     * Returns the url to use when the browser opens, according to both the deploy url
     * and a custom browser url, if set.
     *
     * @return the url to use when to open the browser
     */
    public String getOpenBrowserUrl() {
        final String customUrl = getCustomBrowserUrl();
        final String deployUrl = getDeployUrl();
        if (customUrl == null) {
            return deployUrl;
        } else if (customUrl.startsWith("http://")) {
            return customUrl;
        } else if (customUrl.startsWith("/")) {
            return buildUrl(getPort(), customUrl);
        } else {
            return deployUrl + customUrl;
        }
    }

    void setPort(int port) {
        this.port = port;
    }

    final void setContextPath(String contextPath) {
        this.contextPath = cleanContextPath(contextPath);
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

    void setProductionMode(boolean productionMode) {
        this.productionMode = productionMode;
    }

    void setOpenBrowser(boolean openBrowser) {
        this.openBrowser = openBrowser;
    }

    final void setCustomBrowserUrl(String customBrowserUrl) {
        this.customBrowserUrl = customBrowserUrl;
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

    /**
     * Cleans the specified context path. Adds a forward slash if one
     * is missing and make sure to interpret both the empty string and
     * '/' as the root context.
     *
     * @param contextPath the context path to clean
     * @return the context path to use
     */
    static String cleanContextPath(String contextPath) {
        // Special handling so that / can be used for the root context as well
        if (contextPath.equals("/") || contextPath.trim().equals("")) {
            return "";
        } else if (!contextPath.startsWith("/")) {
            return "/" + contextPath;
        } else {
            return contextPath;
        }
    }

    /**
     * Build a url for the specified port and context.
     * <p/>
     * If the <tt>httpPort</tt> is equal to {@link #PORT_AUTO}, the
     * returned url is not used as is because no http port has been
     * allocated yet.
     *
     * @param httpPort the http port to use
     * @param context the context of the url
     * @return a <tt>localhost</tt> url for the specified port and context
     */
    static String buildUrl(int httpPort, String context) {
        final StringBuilder sb = new StringBuilder();

        sb.append("http://localhost:");
        if (httpPort == PORT_AUTO) {
            sb.append("[auto]");
        } else {
            sb.append(httpPort);
        }
        sb.append(context);
        return sb.toString();
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
                .add("waiting", waiting).add("widgetSet", widgetSet)
                .add("productionMode", productionMode)
                .add("openBrowser", openBrowser).toString();
    }

}
