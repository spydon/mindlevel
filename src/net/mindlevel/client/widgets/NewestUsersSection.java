package net.mindlevel.client.widgets;

import java.util.List;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NewestUsersSection extends Composite {

    private final VerticalPanel p;

    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    /**
     * Constructs a CommentSection that controls a number of ReadBox and WriteBox
     *
     */
    public NewestUsersSection(final int number) {
        p = new VerticalPanel();
        HTML header = new HTML("Newest users");
        final LoadingElement l = new LoadingElement();
        header.addStyleName("users-header");
        p.add(header);
        p.add(l);


        userService.getNewestUsers(number, new AsyncCallback<List<User>>() {

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
                        p.add(new UserElement(u));
                    }
                }
                l.removeFromParent();
            }
        });

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("users-section");
    }
}