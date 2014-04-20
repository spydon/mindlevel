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

public class LastLoginsSection extends Composite {

    private final VerticalPanel p;

    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    /**
     * Constructs a CommentSection that controls a number of ReadBox and WriteBox
     *
     */
    public LastLoginsSection(final int number) {
        p = new VerticalPanel();
        HTML header = new HTML("Last logins");
        header.addStyleName("last-logins-header");
        p.add(header);

        userService.getLastLogins(number, new AsyncCallback<List<User>>() {

            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(List<User> users) {
                if(users.size() > 0) {
                    for(User u : users) {
                        p.add(new LastLoginElement(u));
                    }
                }
            }
        });

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("news-section");
    }
}