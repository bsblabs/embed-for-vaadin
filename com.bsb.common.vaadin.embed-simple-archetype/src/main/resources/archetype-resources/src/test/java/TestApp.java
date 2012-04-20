package $package;

import com.bsb.common.vaadin.embed.support.EmbedVaadin;

public class TestApp {


    public static void main(String[] args) {

        EmbedVaadin.forComponent(new TestLayout())
                .withDevelopmentHeader(true)
                .openBrowser(true)
                .start();
    }
}
