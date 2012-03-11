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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * A utility to launch a browser.
 *
 * @author Stephane Nicoll
 */
public class BrowserUtils {

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
    public static final boolean isWindows = LOWER_CASE_OS_NAME.startsWith("windows");

    /**
     * Specifies if the current OS is MacOS X.
     */
    public static final boolean isMac = LOWER_CASE_OS_NAME.startsWith("mac");

    /**
     * Opens the specified <tt>url</tt> in the default browser.
     *
     * @param url the url to open
     */
    public static void openBrowser(String url) {
        logger.debug("Launching browser at [" + url + "]");
        final String[] command = getOpenBrowserCommand(url);
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("Using command " + Arrays.asList(command));
            }
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to open browser using command " + Arrays.asList(command));
        }
    }

    /**
     * Returns the command to execute to open the specified URL, according to the
     * current OS.
     *
     * @param url the url to open
     * @return the command to execute to open the url with the default browser
     */
    private static String[] getOpenBrowserCommand(String url) {
        if (isWindows) {
            return new String[]{("cmd.exe"), "/c", escapeParameter("start " + url)};
        } else if (isMac) {
            return new String[]{"/usr/bin/open", url};
        }
        return null;
    }

    /**
     * Escape the specified <tt>param</tt>.
     *
     * @param param the param to handle
     * @return the param surrounded by <tt>"</tt>
     */
    private static String escapeParameter(String param) {
        return "\"" + param + "\"";
    }

}
