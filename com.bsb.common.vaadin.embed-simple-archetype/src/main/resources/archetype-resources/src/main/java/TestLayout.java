package $package;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TestLayout extends VerticalLayout {

    public TestLayout() {
        setSpacing(true);
        setMargin(true);
        setSizeFull();

        final Button hello = new Button("Say hello");
        hello.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                event.getComponent().getWindow().showNotification("Hello World!");
            }
        });
        addComponent(hello);
        setComponentAlignment(hello, Alignment.MIDDLE_CENTER);
    }
}
