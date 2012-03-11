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

/**
 * An embedded server running a Vaadin application.
 *
 * @author Stephane Nicoll
 */
public interface EmbedVaadinServer {

    /**
     * Starts the server. This may or may not block according to {@link #isWaiting()}.
     */
    void start();

    /**
     * Specifies if the server should block once it has started. This makes the
     * use of {@link #stop()} useless since the exit of the VM will actually
     * stop the server. Use this mode when prototyping. In other scenario such
     * as unit test, this should be set to false so that the actual test can run.
     *
     * @return <tt>true</tt> to block the thread once the server has been started
     */
    boolean isWaiting();

    /**
     * Stops the server.
     */
    void stop();
}
