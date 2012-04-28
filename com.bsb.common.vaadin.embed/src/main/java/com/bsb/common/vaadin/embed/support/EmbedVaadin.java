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
package com.bsb.common.vaadin.embed.support;

import com.bsb.common.vaadin.embed.application.EmbedVaadinApplication;
import com.bsb.common.vaadin.embed.component.EmbedVaadinComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Root;

/**
 * Embeds vaadin in a web application deployed in the current VM.
 *
 * @author Stephane Nicoll
 */
public final class EmbedVaadin {

    private EmbedVaadin() {
    }

    /**
     * Creates a new instance to display the specified vaadin {@link Component}.
     * <p/>
     * The specified <tt>component</tt> could be of various types which will be
     * wrapped automatically if necessary as follows:
     * <ul>
     * <li><tt>Root</tt>: the root becomes the root used by the application</li>
     * <li><tt>Window</tt>: a simple root is created with the window. Note that
     * this was common with Vaadin6 but you should consider using a <tt>Root</tt> instead</li>
     * <li><tt>Layout</tt>: a simple root is created using that layout</li>
     * <li>other: a <tt>VerticalLayout</tt> is created to hold the component</li>
     * </ul>
     *
     * @param component the component to use for the vaadin application
     * @return an instance handling that component
     */
    public static EmbedVaadinComponent forComponent(Component component) {
        return new EmbedVaadinComponent(component);
    }

    /**
     * Creates a new instance to manage a vaadin {@link Root} defined by the
     * specified class.
     * <p/>
     * This is an easy way to deploy a complete vaadin application locally from the
     * IDE.
     *
     * @param rootClass the root class
     * @return an instance handling that component
     */
    public static EmbedVaadinApplication forRoot(Class<? extends Root> rootClass) {
        return new EmbedVaadinApplication(rootClass);
    }

}
