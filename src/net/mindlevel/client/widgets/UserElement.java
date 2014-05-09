package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.shared.User;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserElement extends Composite
implements HasClickHandlers {

    private Panel backPanel;

    /**
     * Constructs an NewsElement with the given news displayed.
     *
     * @param caption the caption to be displayed with the check box
     */
    public UserElement(final User user, final boolean isSimple) {
        ClickHandler handler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                AnchorElement eventTarget = (AnchorElement)event.getNativeEvent().getEventTarget().cast();
                if(eventTarget == null || !eventTarget.getTagName().equals("A")) {
                    History.newItem("user=" + user.getUsername());
                }
            }
        };


        HTML username = new HTML(HandyTools.getAnchor("user", user.getUsername(), user.getUsername() + "(" + user.getScore() + ")"));
        Image image;
        if(isSimple) {
            backPanel = new Grid(1,2);
            image = new Image(Mindlevel.PATH + "pictures/" + user.getThumbnail());
            ((Grid)backPanel).setWidget(0, 0, image);
            ((Grid)backPanel).setWidget(0, 1, username);
        } else {
            backPanel = new VerticalPanel();
            image = new Image(Mindlevel.PATH + "pictures/" + user.getPicture());
            backPanel.add(image);
            backPanel.add(username);
            backPanel.add(new HTML("Location: " + user.getLocation()));
            backPanel.add(new HTML("Last log in: " + HandyTools.formatDate(user.getLastLogin())));
        }

        image.addStyleName("user-image");
        image.addClickHandler(handler);
        addClickHandler(handler);

        // All composites must call initWidget() in their constructors.
        initWidget(backPanel);

        // Give the overall composite a style name.
        setStyleName(isSimple ? "simple-user-element" : "user-element");
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}