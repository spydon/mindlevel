package net.mindlevel.client.widgets;

import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.client.tools.HtmlTools;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.SimplePanel;

public class UserTagElement extends Composite implements HasClickHandlers {

    private final UserServiceAsync userService = GWT
            .create(UserService.class);
    public static enum SIZE {SMALL, MEDIUM, LARGE}

    public UserTagElement(final User user, final boolean hasPicture, final boolean hasName) {
        this(user, hasPicture, hasName, SIZE.MEDIUM);
    }

    /**
     * Constructs an UserElement with the given user displayed.
     *
     */
    public UserTagElement(final User user, final boolean hasPicture, final boolean hasName, final SIZE size) {
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
            Image image = new Image();
            image.setUrl(Mindlevel.PATH + "pictures/" + user.getThumbnail());
            image.addStyleName("user-tag-picture");

            switch (size) {
            case SMALL:
                image.addStyleName("user-tag-picture-small");
                break;
            case MEDIUM:
                image.addStyleName("user-tag-picture-medium");
                break;
            case LARGE:
                image.addStyleName("user-tag-picture-large");
                image.setUrl(Mindlevel.PATH + "pictures/" + user.getPicture());
                break;
            }
            p.add(image);
        }
        if (hasName) {
            InlineHTML username = new InlineHTML(HtmlTools.getAnchor("user", user.getUsername(), user.getUsername() + "(" + user.getScore() + ")"));
            p.add(username);
        }

        addClickHandler(handler);
        initWidget(p);
        setStyleName("user-tag");
    }

    public UserTagElement(final String username, final boolean hasPicture, final boolean hasName, final SIZE size) {
        final SimplePanel p = new SimplePanel();
        p.setWidget(new LoadingElement());
        userService.getUser(username, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                p.clear();
                p.setWidget(new UserTagElement(user, hasPicture, hasName, size));
            }

            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
            }
        });
        initWidget(p);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}