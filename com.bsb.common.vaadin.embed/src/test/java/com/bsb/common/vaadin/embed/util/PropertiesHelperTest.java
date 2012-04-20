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

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author Stephane Nicoll
 */
public class PropertiesHelperTest {

    @Test(expected = IllegalArgumentException.class)
    public void createWithNull() {
        createInstance(null);
    }

    @Test
    public void getBooleanNoMatch() {
        final Properties properties = new Properties();
        properties.put("key", "value");
        assertEquals("No match so default value should be used", true,
                createInstance(properties).getBooleanProperty("anotherKey", true));
    }

    @Test
    public void getBooleanWithMatch() {
        final Properties properties = new Properties();
        properties.put("key", "false");
        assertEquals("Match so value should be used", false,
                createInstance(properties).getBooleanProperty("key", true));
    }

    @Test
    public void getIntNoMatch() {
        final Properties properties = new Properties();
        properties.put("key", "value");
        assertEquals("No match so default value should be used", 2,
                createInstance(properties).getIntProperty("anotherKey", 2));
    }

    @Test
    public void getIntWithMatch() {
        final Properties properties = new Properties();
        properties.put("key", "4");
        assertEquals("Match so value should be used", 4,
                createInstance(properties).getIntProperty("key", 23));
    }

    private PropertiesHelper createInstance(Properties properties) {
        return new PropertiesHelper(properties);
    }
}
