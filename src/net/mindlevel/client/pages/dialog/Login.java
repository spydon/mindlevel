package net.mindlevel.client.pages.dialog;

import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.services.LoginService;
import net.mindlevel.client.services.LoginServiceAsync;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Login {

    /**
     * Create a remote service proxy to talk to the server-side Greeting
     * service.
     */
    private final LoginServiceAsync loginService = GWT
            .create(LoginService.class);

    private final String session;

    public Login(String session) {
        this.session = session;
        init();
    }

    private void init() {
        Mindlevel.forceFocus = false;
        final Button sendButton = new Button("Login");
        final TextBox userField = new TextBox();
        final PasswordTextBox passField = new PasswordTextBox();
        passField.setWidth("162px");
        final Label errorLabel = new Label();
        final Label userLabel = new Label("User: ");
        final Label passLabel = new Label("Pass: ");
        final DialogBox loginBox = new DialogBox(true);
        final Button closeButton = new Button("Close");
        final Label textToServerLabel = new Label();
        final HTML serverResponse = new HTML();
        // lbCloseButton.getElement().setId("closeButton");
        VerticalPanel loginPanel = new VerticalPanel();
        Grid gridPanel = new Grid(3, 2);

        // We can add style names to widgets
        sendButton.addStyleName("sendButton");

        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        gridPanel.setWidget(0, 0, userLabel);
        gridPanel.setWidget(0, 1, userField);
        gridPanel.setWidget(1, 0, passLabel);
        gridPanel.setWidget(1, 1, passField);
        gridPanel.setWidget(2, 0, errorLabel);
        gridPanel.setWidget(2, 1, serverResponse);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(sendButton);
        buttonPanel.add(closeButton);
        loginPanel.add(gridPanel);
        loginPanel.add(buttonPanel);

        // Create the popup dialog box
        // Add a handler to close the loginBox
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loginBox.hide();
                Mindlevel.forceFocus = true;
            }
        });

        // Create a handler for the sendButton and nameField
        class LoginHandler implements ClickHandler, KeyUpHandler {
            /**
             * Fired when the user clicks on the sendButton.
             */
            @Override
            public void onClick(ClickEvent event) {
                authenticate();
            }

            /**
             * Fired when the user types in the nameField.
             */
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    authenticate();
                }
            }

            /**
             * Send the name from the nameField to the server and wait for a
             * response.
             */
            private void authenticate() {
                // First, we validate the input.
                errorLabel.setText("");
                String username = userField.getText();
                String password = passField.getText();

                // Then, we send the input to the server.
                sendButton.setEnabled(false);
                textToServerLabel.setText(username);
                serverResponse.setText("");
                loginService.login(username, password,
                        new AsyncCallback<User>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                // Show the RPC error message to the user
                                loginBox.setText("Failure");
                                serverResponse
                                        .addStyleName("serverResponseLabelError");
                                // serverResponseLabel.setHTML(SERVER_ERROR);
                                serverResponse.setHTML(caught.getMessage());
                                loginBox.center();
                                closeButton.setFocus(true);
                                sendButton.setEnabled(true);
                            }

                            @Override
                            public void onSuccess(User user) {
                                UserTools.setLoggedIn(user);
                                loginBox.hide();
                                History.newItem(session);
                            }
                        });
            }
        }

        // Add a handler to send the name to the server
        LoginHandler handler = new LoginHandler();
        sendButton.addClickHandler(handler);
        userField.addKeyUpHandler(handler);
        passField.addKeyUpHandler(handler);

        // Focus the cursor on the name field when the app loads
        userField.setFocus(true);

        loginBox.setWidget(loginPanel);
        loginBox.setText("Login");
        loginBox.setAnimationEnabled(true);
        loginBox.center();
    }
}
