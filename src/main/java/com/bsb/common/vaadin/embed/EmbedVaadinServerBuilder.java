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

import java.io.File;
import java.util.Properties;

/**
 * A basic builder for an {@link EmbedVaadinServer}.
 *
 * @author Stephane Nicoll
 */
public abstract class EmbedVaadinServerBuilder<B extends EmbedVaadinServerBuilder<B, S>,
        S extends EmbedVaadinServer> {

    /**
     * Creates a new instance
     */
    protected EmbedVaadinServerBuilder() {
    }

    /**
     * Returns this instance.
     *
     * @return this
     */
    protected abstract B self();

    /**
     * Builds an {@link EmbedVaadinServer} according to the state of this builder.
     *
     * @return an embed vaadin server instance
     */
    public abstract S build();

    /**
     * Loads a configuration file from the specified location. Fails if the
     * specified <tt>path</tt> does not exist.
     *
     * @param path the location of a properties file in the classpath
     * @return this
     * @throws IllegalStateException if no such properties file is found
     */
    public B withConfigPath(String path) {
        assertNotNull(path, "path could not be null.");
        withConfigProperties(EmbedVaadinConfig.loadProperties(path));
        return self();
    }

    /**
     * Loads configuration from the specified {@link Properties}, usually read from a <code>.properties</code> file.
     *
     * @param properties configuration properties
     * @return this
     */
    public abstract B withConfigProperties(Properties properties);

    /**
     * Specifies the HTTP port to use.
     *
     * @param httpPort the http port
     * @return this
     */
    public B withHttpPort(int httpPort) {
        getConfig().setPort(httpPort);
        return self();
    }

    /**
     * Specifies the context to use to deploy the web application. An empty
     * string or <tt>/</tt> means the root context.
     *
     * @param contextPath the context path to use
     * @return this
     */
    public B withContextPath(String contextPath) {
        assertNotNull(contextPath, "contextPath  could not be null.");
        getConfig().setContextPath(contextPath);

        return self();
    }

    /**
     * Specifies the context root directory to use for the web application. The specified
     * <tt>webappRootDirectory</tt> must exist and must be a directory.
     * <p/>
     * Any static resources under that directory will be available for the deployed web application.
     *
     * @param webappRootDirectory the root directory of the deployed web application
     * @return this
     */
    public B withContextRootDirectory(File webappRootDirectory) {
        assertNotNull(webappRootDirectory, "webapp root directory could not be null.");
        if (!webappRootDirectory.exists()) {
            throw new IllegalArgumentException("webapp root directory ["
                    + webappRootDirectory.getAbsolutePath() + "] must exist.");
        }
        if (!webappRootDirectory.isDirectory()) {
            throw new IllegalArgumentException("the specified value ["
                    + webappRootDirectory.getAbsolutePath() + "] is not a directory!");
        }
        getConfig().setContextRootDirectory(webappRootDirectory);
        return self();
    }

    /**
     * Specifies the context root directory to use as a relative path from the working
     * directory. This is a handy shortcut to use when the this runs with a working
     * directory equals to the root directory of the module. Specifying <code>src/main/webapp</code>
     * for a war project for instance would automatically serves all static resources that
     * are contained in the module. It is also possible to rely on the maven build itself,
     * for instance by specifying <tt>target/my-webapp</tt> where <tt>my-webapp</tt> is
     * the final name of your war project.
     *
     * @param relativeDirectory a directory relative to the working directory
     * @return this
     */
    public B withContextRootDirectory(String relativeDirectory) {
        assertNotNull(relativeDirectory, "webapp root directory could not be null.");
        return withContextRootDirectory(new File(".", relativeDirectory));
    }


    /**
     * Specifies the vaadin widgetSet to use for the application.
     * For example <code>"com.vaadin.terminal.gwt.DefaultWidgetSet"</code>
     * The specified <tt>widgetSet</tt> can be <tt>null</tt> to mention that the
     * default widgetSet should be used.
     *
     * @param widgetSet fully qualified name of the widgetSet to use.
     * @return this
     */
    public B withWidgetSet(String widgetSet) {
        getConfig().setWidgetSet(widgetSet);
        return self();
    }

    /**
     * Specifies if the vaadin application should run with production mode
     * enabled or not. When production mode is enabled, debug features are
     * not available for the application.
     *
     * @param productionMode <tt>true</tt> to enable production mode
     * @return this
     */
    public B withProductionMode(boolean productionMode) {
        getConfig().setProductionMode(productionMode);
        return self();
    }

    /**
     * Specifies if the created server should block after startup. This is the
     * default and is suitable to quickly display something while prototyping.
     * <p/>
     * In other scenario, such as unit test, this should be set to <tt>false</tt>
     * so that the actual test can run once the server has been started.
     *
     * @param shouldWait <tt>true</tt> to block the thread once the server has been started
     * @return this
     */
    public B wait(boolean shouldWait) {
        getConfig().setWaiting(shouldWait);
        return self();
    }

    /**
     * Specifies if the browser should be opened once the server has started. By default,
     * does not open the browser.
     *
     * @param open <tt>true</tt> to open the browser once the server has been started
     * @return this
     */
    public B openBrowser(boolean open) {
        getConfig().setOpenBrowser(open);
        return self();
    }

    /**
     * Builds an {@link EmbedVaadinServer} and starts it immediately.
     *
     * @return the created server
     */
    public S start() {
        final S server = build();
        server.start();
        return server;
    }

    /**
     * Returns the underling {@link EmbedVaadinConfig} that this instance is managing.
     * Must be implemented by a concrete builder because the type of configuration can be different.
     *
     * @return the configuration
     */
    protected abstract EmbedVaadinConfig getConfig();

    // Helpers for concrete builders

    protected void assertNotNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
