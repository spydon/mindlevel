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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;

public class UserTagElement extends Composite implements HasClickHandlers {

    /**
     * Constructs an UserElement with the given user displayed.
     *
     */
    public UserTagElement(final User user, final boolean hasPicture, final boolean hasName) {
        ClickHandler handler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                AnchorElement eventTarget = (AnchorElement)event.getNativeEvent().getEventTarget().cast();
                if(eventTarget == null || !eventTarget.getTagName().equals("A")) {
                    History.newItem("user=" + user.getUsername());
                }
            }
        };

        FlowPanel p = new FlowPanel();

        if (hasPicture) {
            Image image = new Image(Mindlevel.PATH + "pictures/" + user.getThumbnail());
            image.addStyleName("user-tag-picture");
            p.add(image);
        }
        if (hasName) {
            InlineHTML username = new InlineHTML(HandyTools.getAnchor("user", user.getUsername(), user.getUsername() + "(" + user.getScore() + ")"));
            p.add(username);
        }

        addClickHandler(handler);
        initWidget(p);
        setStyleName("user-tag");
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}