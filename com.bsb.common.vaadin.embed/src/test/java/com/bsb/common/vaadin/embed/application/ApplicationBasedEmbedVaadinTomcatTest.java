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
package com.bsb.common.vaadin.embed.application;

import com.bsb.common.vaadin.embed.AbstractEmbedTest;
import com.bsb.common.vaadin.embed.EmbedVaadinServer;
import com.bsb.common.vaadin.embed.support.EmbedVaadin;
import com.bsb.common.vaadin.embed.test.TestRoot;
import org.junit.Test;

/**
 * @author Stephane Nicoll
 */
public class ApplicationBasedEmbedVaadinTomcatTest extends AbstractEmbedTest {

    @Test
    public void start() {
        final EmbedVaadinServer server = EmbedVaadin.forRoot(
                TestRoot.class).wait(false).withHttpPort(18101).start();

        checkVaadinIsDeployed(18101, "");

        server.stop();
    }

}
