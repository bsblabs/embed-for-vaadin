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

import java.io.File;

/**
 * A basic builder for an {@link EmbedVaadinServer}.
 *
 * @author Stephane Nicoll
 */
public abstract class EmbedVaadinServerBuilder<B extends EmbedVaadinServerBuilder<B, S>,
        S extends EmbedVaadinServer> {

    private EmbedVaadinConfig config;

    /**
     * Creates a new instance, initializing the properties with a default
     * {@link EmbedVaadinConfig} instance.
     *
     * @param loadDefault <tt>true</tt> to initialize the builder with the default config
     * @see EmbedVaadinConfig#load()
     */
    protected EmbedVaadinServerBuilder(boolean loadDefault) {
        if (loadDefault) {
            this.config = EmbedVaadinConfig.load();
        } else {
            this.config = EmbedVaadinConfig.defaultConfig();
        }
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
     * Applies the content of the specified {@link EmbedVaadinConfig}.
     *
     * @param config the configuration to use
     * @return this
     */
    public B withConfig(EmbedVaadinConfig config) {
        assertNotNull(config, "embed config could not be null.");
        this.config = new EmbedVaadinConfig(config);

        return self();
    }

    /**
     * Specifies the HTTP port to use.
     *
     * @param httpPort the http port
     * @return this
     */
    public B withHttpPort(int httpPort) {
        this.config.setPort(httpPort);
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

        // Special handling so that / can be used for the root context as well
        if (contextPath.equals("/") || contextPath.trim().equals("")) {
            this.config.setContextPath("");
        } else if (!contextPath.startsWith("/")) {
            this.config.setContextPath("/" + contextPath);
        } else {
            this.config.setContextPath(contextPath);
        }
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
        this.config.setContextRootDirectory(webappRootDirectory);
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
     * Specifies the vaadin theme to use for the application.
     *
     * @param theme the theme to use
     * @return this
     */
    public B withVaadinTheme(String theme) {
        assertNotNull(theme, "theme could not be null.");

        this.config.setTheme(theme);
        return self();
    }

    /**
     * Specifies the vaadin widgetSet to use for the application. The
     * specified <tt>widgetSet</tt> can be <tt>null</tt> to mention that the
     * default widgetSet should be used.
     *
     * @param widgetSet the widgetSet to use
     * @return this
     */
    public B withWidgetSet(String widgetSet) {
        this.config.setWidgetSet(widgetSet);
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
        this.config.setWaiting(shouldWait);
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
        this.config.setOpenBrowser(open);
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


    // Helpers for concrete builders

    /**
     * Returns the underling {@link EmbedVaadinConfig} that this instance is managing.
     *
     * @return the configuration
     */
    protected EmbedVaadinConfig getConfig() {
        return config;
    }


    protected void assertNotNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
