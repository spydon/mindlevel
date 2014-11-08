package net.mindlevel.client.widgets;

import java.util.List;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class LoginsSection extends Composite {

    private final FlowPanel p;

    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    /**
     * Constructs a CommentSection that controls a number of ReadBox and WriteBox
     *
     */
    public LoginsSection(final int number) {
        p = new FlowPanel();
        HTML header = new HTML("<h2>Last login</h2>");
        final LoadingElement l = new LoadingElement();
        header.addStyleName("users-header");
        p.add(header);
        p.add(l);


        userService.getLastLogins(number, new AsyncCallback<List<User>>() {

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
                        p.add(new UserElement(u, true));
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