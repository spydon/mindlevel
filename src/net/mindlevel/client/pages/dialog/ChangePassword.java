package net.mindlevel.client.pages.dialog;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ChangePassword {
    private FlexTable t;
    private final HTML header;
    private final DecoratedPopupPanel popup;
    private final VerticalPanel panel;

    private final UserServiceAsync userService = GWT
            .create(UserService.class);


    public ChangePassword() {
        popup = new DecoratedPopupPanel(false);
        panel = new VerticalPanel();
        header = new HTML("<h1>Change password</h1>");
        init();
    }

    private void init() {
        // Initiate the FlexTable
        t = new FlexTable();

        Label oldL= new Label("Old Password");
        final PasswordTextBox oldTB = new PasswordTextBox();
        Label passwordL = new Label("New Password");
        final PasswordTextBox passwordTB = new PasswordTextBox();
        Label password2L = new Label("Retype Password");
        final PasswordTextBox password2TB = new PasswordTextBox();
        Button changeB = new Button("Change pass");
        changeB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                userService.changePassword(Mindlevel.user.getUsername(),
                                          oldTB.getText(),
                                          passwordTB.getText(),
                                          Mindlevel.user.getToken(),
                                          new AsyncCallback<Void>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                            }

                            @Override
                            public void onSuccess(Void result) {
                                HandyTools.showDialogBox("Success", new HTML("Successfully changed password!"));
                                popup.hide();
                            }
                        });
            }
        });
        Button closeB = new Button("Close");
        closeB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popup.hide();
            }
        });
        t.setWidget(0, 0, oldL);
        t.setWidget(0, 1, oldTB);
        t.setWidget(1, 0, passwordL);
        t.setWidget(1, 1, passwordTB);
        t.setWidget(2, 0, password2L);
        t.setWidget(2, 1, password2TB);
        t.setWidget(4, 1, changeB);
        t.setWidget(5, 1, closeB);

        panel.add(header);
        panel.add(t);
        popup.add(panel);
        popup.center();
        popup.show();
    }
}
