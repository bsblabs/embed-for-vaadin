package com.bsb.common.vaadin.embed.component;

import com.bsb.common.vaadin.embed.EmbedVaadinServer;

/**
 * A {@link EmbedVaadinServer} that deploys a component.
 * 
 * @author Wouter Coekaerts
 */
public interface ComponentBasedVaadinServer extends EmbedVaadinServer {

    // overriding, narrowing return type
    EmbedComponentConfig getConfig();
}
