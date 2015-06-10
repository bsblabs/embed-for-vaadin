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
package com.bsb.common.vaadin.embed.test;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * A stupid application with a button
 *
 * @author Stephane Nicoll
 */
@SuppressWarnings("serial")
public class TestUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        final Button hello = new Button("Hello");
        hello.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                final Notification n =
                        new Notification("Yes, hello", Notification.Type.HUMANIZED_MESSAGE);
                n.setDelayMsec(-1);
                n.show(Page.getCurrent());
            }
        });
        layout.addComponent(hello);
        layout.setComponentAlignment(hello, Alignment.TOP_CENTER);
        setContent(layout);
    }
}
