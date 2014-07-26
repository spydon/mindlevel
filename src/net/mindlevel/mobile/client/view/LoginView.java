package net.mindlevel.mobile.client.view;

import net.mindlevel.client.UserTools;
import net.mindlevel.client.services.LoginService;
import net.mindlevel.client.services.LoginServiceAsync;
import net.mindlevel.client.widgets.QuoteElement;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.form.FormEntry;
import com.googlecode.mgwt.ui.client.widget.input.MPasswordTextBox;
import com.googlecode.mgwt.ui.client.widget.input.MTextBox;

public class LoginView extends MPage {
    protected VerticalPanel main;
    private final Label errorLabel;
    private final MTextBox userField;
    private final MPasswordTextBox passField;

    private final Button loginButton;
    private final Button registerButton;

    /**
     * Create a remote service proxy to talk to the server-side login
     * service.
     */
    private final LoginServiceAsync loginService = GWT
            .create(LoginService.class);

    public LoginView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");

        Image logo = new Image("./images/logo.png");
        logo.addStyleName("m-logo");

        QuoteElement quote = new QuoteElement();
        quote.addStyleName("m-quote");

        errorLabel = new Label("");


        VerticalPanel formPanel = new VerticalPanel();
        formPanel.addStyleName("m-button-panel");

        userField = new MTextBox();
        passField = new MPasswordTextBox();

        FormEntry userEntry = new FormEntry("Username: ", userField);
        FormEntry passEntry = new FormEntry("Password: ", passField);

        passField.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    authenticate();
                }
            }
        });

//        userField.addStyleName("m-center-input");
//        passField.addStyleName("m-center-input");
        errorLabel.addStyleName("m-margin");

        loginButton = new Button("Login");
        registerButton = new Button("Register");
        loginButton.addStyleName("m-button");
        registerButton.addStyleName("m-button");

        loginButton.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                authenticate();
            }
        });

        registerButton.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("register");
            }
        });

        formPanel.add(userEntry);
        formPanel.add(passEntry);
        formPanel.add(errorLabel);
        formPanel.add(loginButton);
        formPanel.add(registerButton);

        main.add(logo);
        main.add(quote);
        main.add(formPanel);
    }

    private void authenticate() {
        // First, we validate the input.
        errorLabel.setText("");
        String username = userField.getText();
        String password = passField.getText();

        // Then, we send the input to the server.
        loginButton.setDisabled(true);
        loginService.login(username, password,
                new AsyncCallback<User>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        // Show the RPC error message to the user
                        errorLabel.addStyleName("error-label");
                        errorLabel.setText(caught.getMessage());
                        loginButton.setDisabled(false);
                    }

                    @Override
                    public void onSuccess(User user) {
                        UserTools.setLoggedIn(user);
                        History.newItem(session);
                    }
                });
    }

    @Override
    public Widget asWidget() {
        onLoad();
        return main;
    }
}
