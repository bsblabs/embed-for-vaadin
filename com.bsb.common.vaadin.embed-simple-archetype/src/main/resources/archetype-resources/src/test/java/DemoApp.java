package $package;

import com.bsb.common.vaadin.embed.support.EmbedVaadin;

public class DemoApp {


    public static void main(String[] args) {

        EmbedVaadin.forComponent(new DemoLayout())
                .withDevelopmentHeader(true)
                .openBrowser(true)
                .start();
    }
}
