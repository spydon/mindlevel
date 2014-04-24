package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.shared.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

public class UserElement extends Composite
implements HasClickHandlers {

    private final Grid backPanel;

    /**
     * Constructs an NewsElement with the given news displayed.
     *
     * @param caption the caption to be displayed with the check box
     */
    public UserElement(final User user) {
        backPanel = new Grid(1,2);
        ClickHandler handler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                History.newItem("user=" + user.getUsername());
            }
        };
        Image image = new Image("pictures/" + user.getThumbnail());
        image.addStyleName("user-image");
        image.addClickHandler(handler);
        addClickHandler(handler);

        HTML username = new HTML(HandyTools.getAnchor("user", user.getUsername(), user.getUsername() + "(" + user.getScore() + ")"));

        backPanel.setWidget(0, 0, image);
        backPanel.setWidget(0, 1, username);

        // All composites must call initWidget() in their constructors.
        initWidget(backPanel);
        // Give the overall composite a style name.
        setStyleName("user-element");
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}