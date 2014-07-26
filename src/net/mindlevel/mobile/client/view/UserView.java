package net.mindlevel.mobile.client.view;

import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.client.widgets.LoadingElement;
import net.mindlevel.client.widgets.UserElement;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserView extends MPage {
    protected VerticalPanel main;

    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    public UserView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");
    }

    private void loadUser(String username) {
        main.clear();
        final LoadingElement progress = new LoadingElement();
        main.add(progress);
        userService.getUser(username, new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(User user) {
                main.remove(progress);
                UserElement userElement = new UserElement(user, false, true);
                userElement.addStyleName("m-user-element");
                main.add(userElement);
            }
        });
    }

    @Override
    public Widget asWidget() {
        loadUser(parameter);
        onLoad();
        return main;
    }
}