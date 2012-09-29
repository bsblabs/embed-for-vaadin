package $package;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DemoLayout extends VerticalLayout {

    public DemoLayout() {
        setSpacing(true);
        setMargin(true);
        setSizeFull();

        final Button hello = new Button("Say hello");
        hello.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Hello World!");
            }
        });
        addComponent(hello);
        setComponentAlignment(hello, Alignment.MIDDLE_CENTER);
    }
}
