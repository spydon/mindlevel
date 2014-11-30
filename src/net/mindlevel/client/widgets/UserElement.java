package net.mindlevel.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.user.client.ui.*;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.client.tools.HtmlTools;
import net.mindlevel.shared.User;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;

public class UserElement extends Composite
implements HasClickHandlers {

    private Panel backPanel;

    public UserElement(final User user, final boolean isSimple) {
        this(user, isSimple, false);
    }

    /**
     * Constructs an UserElement with the given user displayed.
     *
     */
    public UserElement(final User user, final boolean isSimple, final boolean includeAbout) {
        ClickHandler handler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                AnchorElement eventTarget = (AnchorElement)event.getNativeEvent().getEventTarget().cast();
                if(eventTarget == null || !eventTarget.getTagName().equals("A")) {
                    History.newItem("user=" + user.getUsername());
                }
            }
        };

        HTML username = new HTML(HtmlTools.getAnchor("user", user.getUsername(), user.getUsername() + "(" + user.getScore() + ")"));
        HTML image = new HTML();
        backPanel = new FlowPanel();
        if(isSimple) {
            backPanel.add(image);
            backPanel.add(username);
            image.setHTML("<div style=background-image: " + Mindlevel.PATH + "pictures/" + user.getThumbnail());
            image.getElement().getStyle().setBackgroundImage(Mindlevel.PATH + "pictures/" + user.getThumbnail());
            image.getElement().setPropertyString("style", "background-image: " + Mindlevel.PATH + "pictures/" + user.getThumbnail());
            GWT.log(image.getElement().getStyle().getBackgroundImage());
        } else {
            username.addStyleName("user-username");
            backPanel.add(image);
            backPanel.add(username);
            image.getElement().getStyle().setBackgroundImage(Mindlevel.PATH + "pictures/" + user.getPicture());
            if(!user.getName().equals("")) {
                backPanel.add(new HTML("<b>Name:</b> " + user.getName()));
            }
            if(!user.getLocation().equals("")) {
                backPanel.add(new HTML("<b>Location:</b> " + HtmlTools.formatHtml(user.getLocation())));
            }
            backPanel.add(new HTML("<b>Last log in:</b> " + HandyTools.formatOnlyDate(user.getLastLogin())));
            if(includeAbout && !user.getAbout().equals("")) {
                backPanel.add(new HTML("<b>About:</b> " + HtmlTools.formatHtml(user.getAbout())));
            }
        }

        image.addStyleName("user-image");
        image.addHandler(handler, ClickEvent.getType());
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