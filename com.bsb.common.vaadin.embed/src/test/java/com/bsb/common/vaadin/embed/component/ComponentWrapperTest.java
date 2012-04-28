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
package com.bsb.common.vaadin.embed.component;

import com.bsb.common.vaadin.embed.AbstractEmbedVaadinTomcat;
import com.vaadin.terminal.WrappedRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Root;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Stephane Nicoll
 */
@SuppressWarnings("serial")
public class ComponentWrapperTest {

    private final ComponentWrapper instance = createWrapper(EmbedComponentConfig.defaultConfig());

    @Test
    public void wrapRoot() {
        final Root root = new Root() {
            @Override
            protected void init(WrappedRequest request) {
                setContent(new VerticalLayout());
            }
        };
        final DevApplication app = instance.wrap(root);
        assertEquals("root was not set properly", root, app.getRoot());
    }

    @Test
    public void wrapLayoutWithDevelopmentHeader() {
        final HorizontalLayout layout = new HorizontalLayout();
        final DevApplication app = instance.wrap(layout);

        final Layout l = assertWrappingLayout(app);

        assertEquals("Layout was not set properly", layout, l);
    }

    @Test
    public void wrapLayoutWithoutDevelopmentHeader() {
        final EmbedComponentConfig config = EmbedComponentConfig.defaultConfig();
        config.setDevelopmentHeader(false);
        final ComponentWrapper wrapper = createWrapper(config);

        final HorizontalLayout layout = new HorizontalLayout();
        // Wrap without development header should just set the layout as the main content of the window
        final DevApplication app = wrapper.wrap(layout);

        assertNotNull("Main content must not be null", app.getMainContent());
        assertEquals("Layout should be set as the main layout since no header was expected", layout,
                app.getMainContent());
    }

    @Test
    public void wrapSimpleComponent() {
        final Button component = new Button("Hello");
        final DevApplication app = instance.wrap(component);

        final Layout l = assertWrappingLayout(app);
        assertEquals("Main content must be vertical layout", VerticalLayout.class,
                l.getClass());
        final VerticalLayout layout = (VerticalLayout) l;
        assertEquals("Should have a single component", 1, layout.getComponentCount());
        assertEquals("Component was not set properly", component, layout.getComponent(0));
    }

    @Test
    public void wrapWindow() {
        final VerticalLayout content = new VerticalLayout();
        final Window w = new Window("Test", content);

        final DevApplication app = instance.wrap(w);
        assertEquals("window was not wrapped properly", w, app.getMainContent());
    }

    private Layout assertWrappingLayout(DevApplication app) {
        assertNotNull("Main content must not be null", app.getMainContent());
        assertEquals("VerticalSplitPanel with header was expected", VerticalSplitPanel.class,
                app.getMainContent().getClass());
        final VerticalSplitPanel mainLayout = (VerticalSplitPanel) app.getMainContent();
        assertEquals("Two components were expected, header and actual layout", 2, mainLayout.getComponentCount());
        assertEquals("Header was not set properly", DevApplicationHeader.class, mainLayout.getFirstComponent().getClass());
        assertTrue("Layout to display was not set properly [" + mainLayout.getSecondComponent() + "]",
                mainLayout.getSecondComponent() instanceof Layout);
        return (Layout) mainLayout.getSecondComponent();
    }

    private ComponentWrapper createWrapper(EmbedComponentConfig config) {
        return new ComponentWrapper(new TestableEmbedVaadinServer(config));
    }


    // A testable server that does not need any vaadin component
    @SuppressWarnings("serial")
    private static final class TestableEmbedVaadinServer extends AbstractEmbedVaadinTomcat
            implements ComponentBasedVaadinServer {
        private final EmbedComponentConfig config;

        private TestableEmbedVaadinServer(EmbedComponentConfig config) {
            super(config);
            this.config = config;
        }

        @Override
        protected void configure() {
        }

        public EmbedComponentConfig getConfig() {
            return config;
        }
    }


}
