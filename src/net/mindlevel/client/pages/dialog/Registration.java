package net.mindlevel.client.pages.dialog;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.CaptchaService;
import net.mindlevel.client.services.CaptchaServiceAsync;
import net.mindlevel.client.services.RegistrationService;
import net.mindlevel.client.services.RegistrationServiceAsync;
import net.mindlevel.client.widgets.CaptchaElement;
import net.mindlevel.shared.FieldVerifier;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Registration {
    /**
     * Create a remote service proxy to talk to the server-side Registration service.
     */
    private final RegistrationServiceAsync regService = GWT
            .create(RegistrationService.class);

    /**
     * Create a remote service proxy to talk to the server-side captcha service.
     */
    private final CaptchaServiceAsync captchaService = GWT
            .create(CaptchaService.class);



    public Registration() {
        init();
    }

    private void init() {
        final CaptchaElement captcha = new CaptchaElement();
        captcha.addStyleName("captcha-margin");
        final TextBox userField = new TextBox();
        final TextBox emailField = new TextBox();
        final PasswordTextBox passField = new PasswordTextBox();
        final PasswordTextBox passField2 = new PasswordTextBox();
        passField.setWidth("162px");
        passField2.setWidth("162px");
        final CheckBox adultBox = new CheckBox();
        final CheckBox termsBox = new CheckBox();
        final Label errorLabel = new Label();
        final Label userLabel = new Label("User: ");
        final Label emailLabel = new Label("Email: ");
        final Label passLabel = new Label("Pass: ");
        final Label passLabel2 = new Label("Retype Pass: ");
        final Label adultLabel = new Label("Are you 18+?");
        final Label termsLabel = new HTML("Accept the <a href=\"#terms\">terms</a>?");
        final DialogBox registrationBox = new DialogBox(true);
        final Button regButton = new Button("Register");
        final Button closeButton = new Button("Close");
        final Label textToServerLabel = new Label();
        //lbCloseButton.getElement().setId("closeButton");
        VerticalPanel loginPanel = new VerticalPanel();
        Grid gridPanel = new Grid(7, 2);

        // We can add style names to widgets
        regButton.addStyleName("fullwidth");
        closeButton.addStyleName("fullwidth");

        // Add a panel for the errorLabels, so they don't appear
        // on different places
        HorizontalPanel errorPanel = new HorizontalPanel();
        errorPanel.add(errorLabel);
        errorLabel.addStyleName("serverResponseLabelError");

        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        gridPanel.setWidget(0, 0, userLabel);
        gridPanel.setWidget(0, 1, userField);
        gridPanel.setWidget(1, 0, passLabel);
        gridPanel.setWidget(1, 1, passField);
        gridPanel.setWidget(2, 0, passLabel2);
        gridPanel.setWidget(2, 1, passField2);
        gridPanel.setWidget(3, 0, emailLabel);
        gridPanel.setWidget(3, 1, emailField);
        gridPanel.setWidget(4, 0, adultLabel);
        gridPanel.setWidget(4, 1, adultBox);
        gridPanel.setWidget(5, 0, termsLabel);
        gridPanel.setWidget(5, 1, termsBox);
        gridPanel.setWidget(6, 1, errorPanel);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.addStyleName("fullwidth");
        buttonPanel.add(regButton);
        buttonPanel.add(closeButton);
        loginPanel.add(gridPanel);
        loginPanel.add(captcha);
        loginPanel.add(buttonPanel);
        registrationBox.setText("Registration");
        registrationBox.setWidget(loginPanel);
        registrationBox.setAnimationEnabled(true);
        registrationBox.center();

        // Focus the cursor on the name field when the app loads
        userField.setFocus(true);
        userField.selectAll();

        // Create the popup dialog box
        // Add a handler to close the loginBox
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                registrationBox.hide();
            }
        });

        // Create a handler for the sendButton and nameField
        class SendHandler implements ClickHandler, KeyUpHandler {
            /**
             * Fired when the user clicks on the sendButton.
             */
            @Override
            public void onClick(ClickEvent event) {
                regButton.setEnabled(false);
                sendCredentials();
            }

            /**
             * Fired when the user types in the nameField.
             */
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    regButton.setEnabled(false);
                    sendCredentials();
                }
            }

            /**
             * Send the credentials to the server and wait for a response.
             */
            private void sendCredentials() {
                // First, we validate the input.
                errorLabel.setText("");
                termsBox.removeStyleName("error-background");
                final String username = userField.getText();
                final String email = emailField.getText();
                final String password = passField.getText();
                final String password2 = passField2.getText();
                final boolean adult = adultBox.getValue();

                regButton.setEnabled(false);
                textToServerLabel.setText(username);
                errorLabel.setText("");
                if(!FieldVerifier.isValidUsername(username)) {
                    errorLabel.setText("The username is not valid.");
                    regButton.setEnabled(true);
                } else if (!FieldVerifier.isValidPassword(password, password2)) {
                    errorLabel.setText("The password is not valid.");
                    regButton.setEnabled(true);
                } else if (!FieldVerifier.isValidEmail(email)) {
                    errorLabel.setText("That is not a valid email.");
                    regButton.setEnabled(true);
                } else if (!termsBox.getValue()) {
                    errorLabel.setText("Terms of usage not accepted.");
                    termsBox.addStyleName("error-background");
                    regButton.setEnabled(true);
                } else {
                    captchaService.verify(captcha.getAnswer().toLowerCase(), captcha.getToken(), new AsyncCallback<Boolean>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            // Show the RPC error message to the user
                            registrationBox.setText("Failure");
                            errorLabel.setText(caught.getMessage());
                            registrationBox.center();
                            closeButton.setFocus(true);
                            regButton.setEnabled(true);
                        }

                        @Override
                        public void onSuccess(Boolean isValid) {
                            if(isValid) {
                                username.toLowerCase();
                                regService.register(username, email, password, adult,
                                        new AsyncCallback<Void>() {
                                            @Override
                                            public void onFailure(Throwable caught) {
                                                // Show the RPC error message to the user
                                                registrationBox.setText("Failure");
                                                errorLabel.setText(caught.getMessage());
                                                registrationBox.center();
                                                closeButton.setFocus(true);
                                                regButton.setEnabled(true);
                                            }

                                            @Override
                                            public void onSuccess(Void result) {
                                                registrationBox.hide();
                                                HandyTools.showDialogBox("Success!", new HTML("Welcome, you are now registered!"
                                                        + "<p>You might want to check out the <a href=\"#tutorial\">#tutorial</a>"));
                                            }
                                        });
                            } else {
                                // Show the RPC error message to the user
                                registrationBox.setText("Failure");
                                errorLabel.setText("The captcha answer was invalid");
                                registrationBox.center();
                                closeButton.setFocus(true);
                                regButton.setEnabled(true);
                            }
                        }
                    });
                }
            }
        }

        // Add a handler to send the name to the server
        SendHandler handler = new SendHandler();
        regButton.addClickHandler(handler);
        userField.addKeyUpHandler(handler);
        passField.addKeyUpHandler(handler);
    }
}
