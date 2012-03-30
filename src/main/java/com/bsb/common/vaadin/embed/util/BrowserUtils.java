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
package com.bsb.common.vaadin.embed.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 * A utility to launch a browser.
 *
 * @author Stephane Nicoll
 */
public final class BrowserUtils {

    private static final Logger logger = LoggerFactory.getLogger(BrowserUtils.class);

    /**
     * The system property defining the current OS
     *
     * @see System#getProperty(String)
     */
    public static final String OS_NAME = System.getProperty("os.name");

    private static final String LOWER_CASE_OS_NAME = OS_NAME.toLowerCase();

    /**
     * Specifies if the current OS is Microsoft Windows. Only take recent version of
     * Windows into account.
     */
    public static final boolean IS_WINDOWS = LOWER_CASE_OS_NAME.startsWith("windows");

    /**
     * Specifies if the current OS is MacOS X.
     */
    public static final boolean IS_MAC = LOWER_CASE_OS_NAME.startsWith("mac");

    /**
     * Specifies if the current OS is Linux.
     */
    public static final boolean IS_LINUX = LOWER_CASE_OS_NAME.startsWith("linux");

    private BrowserUtils() {
    }

    /**
     * Opens the specified <tt>url</tt> in the default browser.
     *
     * @param url the url to open
     */
    public static void openBrowser(String url) {
        logger.debug("Launching browser at [" + url + "]");

        try {
            if (! openBrowserJava6(url)) {
                final String[] command = getOpenBrowserCommand(url);

                if (logger.isTraceEnabled()) {
                    logger.trace("Using command " + Arrays.asList(command));
                }
                Runtime.getRuntime().exec(command);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to open browser", e);
        }
    }

    /**
     * Tries to open the browser using java.awt.Desktop.
     * 
     * @param url the url to open
     * @return true if succeeded, false if java.awt.Desktop is not available not or not supported.
     * @throws Exception if the feature is supported but opening a browser failed
     */
    private static boolean openBrowserJava6(String url) throws Exception {
            final Class<?> desktopClass;
            try {
                desktopClass = Class.forName("java.awt.Desktop");
            } catch (ClassNotFoundException e) {
                return false;
            }
            Boolean supported = (Boolean) desktopClass.getMethod("isDesktopSupported", new Class[0]).invoke(null);
            if (supported) {
                Object desktop = desktopClass.getMethod("getDesktop", new Class[0]).invoke(null);
                desktopClass.getMethod("browse", new Class[]{ URI.class }).invoke(desktop, new URI(url));
                return true;
            }
            return false;
    }

    /**
     * Returns the command to execute to open the specified URL, according to the
     * current OS.
     *
     * @param url the url to open
     * @return the command to execute to open the url with the default browser
     * @throws java.io.IOException if an I/O exception occurred while locating the browser
     * @throws InterruptedException if the thread is interrupted while locating the browser
     */
    private static String[] getOpenBrowserCommand(String url) throws IOException, InterruptedException {
        if (IS_WINDOWS) {
            return new String[]{"rundll32", "url.dll,FileProtocolHandler", url};
        } else if (IS_MAC) {
            return new String[]{"/usr/bin/open", url};
        } else if (IS_LINUX) {
            String[] browsers = { "google-chrome", "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
            for (String browser : browsers) {
                if (Runtime.getRuntime().exec(new String[] { "which", browser }).waitFor() == 0) {
                    return new String[]{browser, url};
                }
            }
            throw new UnsupportedOperationException("Cannot find a browser");
        } else {
            throw new UnsupportedOperationException("Opening browser is not implemented for your OS (" + OS_NAME + ")");
        }
    }
}
