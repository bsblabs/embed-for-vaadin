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

import java.util.Properties;

/**
 * Simple helper to cast property values.
 *
 * @author Stephane Nicoll
 */
public class PropertiesHelper {

    private final Properties properties;

    /**
     * Creates a new instance.
     *
     * @param properties the properties to wrap
     * @throws IllegalArgumentException if the specified <tt>properties</tt> is <tt>null</tt>
     */
    public PropertiesHelper(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("properties could not be null.");
        }
        this.properties = properties;
    }

    /**
     * Returns the boolean value for the specified property. If no such key is found,
     * returns the <tt>defaultValue</tt> instead.
     *
     * @param key the key of the property
     * @param defaultValue the default value if no such property is found
     * @return a boolean value
     * @see Properties#getProperty(String, String)
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        final String value = properties.getProperty(key, String.valueOf(defaultValue));
        return Boolean.valueOf(value);
    }

    /**
     * Returns the int value for the specified property. If no such key is found,
     * returns the <tt>defaultValue</tt> instead.
     *
     * @param key the key of the property
     * @param defaultValue the default value if no such property is found
     * @return an int value
     * @see Properties#getProperty(String, String)
     */
    public int getIntProperty(String key, int defaultValue) {
        final String value = properties.getProperty(key, String.valueOf(defaultValue));
        return Integer.valueOf(value);
    }


}
