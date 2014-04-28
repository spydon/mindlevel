package net.mindlevel.client.widgets;

import java.util.List;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class UserSection extends Composite {

    private final FlowPanel p;

    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    /**
     * Constructs a GallerySection that controls a number of PictureElements
     *
     */
    public UserSection(final Constraint constraint) {
        p = new FlowPanel();
        final LoadingElement l = new LoadingElement();
        p.add(l);

        userService.getUsers(0, 20, constraint,
                new AsyncCallback<List<User>>() {

            @Override
            public void onFailure(Throwable caught) {
                l.removeFromParent();
                caught.printStackTrace();
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(List<User> users) {
                if(users.size() > 0) {
                    for(User u : users) {
                        p.add(new UserElement(u, false));
                    }
                }
                l.removeFromParent();
            }
        });

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("gallery-section");
    }
}