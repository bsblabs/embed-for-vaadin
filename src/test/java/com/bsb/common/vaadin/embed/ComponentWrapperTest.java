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

import com.bsb.common.vaadin.embed.ComponentWrapper;
import com.bsb.common.vaadin.embed.EmbedVaadinConfig;
import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Stephane Nicoll
 */
public class ComponentWrapperTest {

    private final ComponentWrapper instance = new ComponentWrapper(EmbedVaadinConfig.defaultConfig());

    @Test
    public void wrapWindow() {
        final Window w = new Window("Test");
        final Application app = instance.wrap(w);
        assertEquals("Main window was not set properly", w, app.getMainWindow());
    }

    @Test
    public void wrapLayout() {
        final HorizontalLayout layout = new HorizontalLayout();
        final Application app = instance.wrap(layout);
        assertNotNull("Main window must not be null", app.getMainWindow());
        assertEquals("Layout was not set properly", layout, app.getMainWindow().getContent());
    }

    @Test
    public void wrapSimpleComponent() {
        final Button component = new Button("Hello");
        final Application app = instance.wrap(component);
        assertNotNull("Main window must not be null", app.getMainWindow());
        assertNotNull("Main content of window must not be null", app.getMainWindow().getContent());
        assertEquals("Main content must be vertical layout", VerticalLayout.class,
                app.getMainWindow().getContent().getClass());
        final VerticalLayout layout = (VerticalLayout) app.getMainWindow().getContent();
        assertEquals("Should have a single component", 1, layout.getComponentCount());
        assertEquals("Component was not set properly", component, layout.getComponent(0));
    }


}
