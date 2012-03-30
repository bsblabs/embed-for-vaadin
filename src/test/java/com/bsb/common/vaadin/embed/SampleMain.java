package com.bsb.common.vaadin.embed;

import com.bsb.common.vaadin.embed.support.EmbedVaadin;
import com.vaadin.ui.Button;

/**
 * Example main class launching a server and starting the browser.
 *
 * @author Wouter Coekaerts
 */
public class SampleMain {
    public static void main(String[] args) {
        EmbedVaadin.forComponent(new Button("Hello")).openBrowser(true).start();
    }
}
