package net.mindlevel.mobile.client.view;

import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.client.widgets.UserElement;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressBar;

public class UserView extends MPage {
    protected VerticalPanel main;

    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    public UserView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");
        init();
    }

    public void init() {

    }

    private void loadUser(String username) {
        main.clear();
        final ProgressBar progress = new ProgressBar();
        main.add(progress);
        userService.getUser(username, new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(User user) {
                main.remove(progress);
                UserElement userElement = new UserElement(user, false);
                main.add(userElement);
            }
        });
    }

    @Override
    public Widget asWidget() {
        return main;
    }

    @Override
    public void setId(int id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setId(String id) {
        loadUser(id);
    }
}