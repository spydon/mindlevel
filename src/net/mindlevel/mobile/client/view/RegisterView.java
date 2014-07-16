package net.mindlevel.mobile.client.view;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.CaptchaService;
import net.mindlevel.client.services.CaptchaServiceAsync;
import net.mindlevel.client.services.RegistrationService;
import net.mindlevel.client.services.RegistrationServiceAsync;
import net.mindlevel.client.widgets.CaptchaElement;
import net.mindlevel.client.widgets.QuoteElement;
import net.mindlevel.shared.FieldVerifier;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.form.FormEntry;
import com.googlecode.mgwt.ui.client.widget.input.MEmailTextBox;
import com.googlecode.mgwt.ui.client.widget.input.MPasswordTextBox;
import com.googlecode.mgwt.ui.client.widget.input.MTextBox;
import com.googlecode.mgwt.ui.client.widget.input.checkbox.MCheckBox;

public class RegisterView extends MPage {
    protected VerticalPanel main;
    private final Label errorLabel;
    private final MTextBox userField;
    private final MEmailTextBox emailField;
    private final MPasswordTextBox passField;
    private final MPasswordTextBox passField2;

    private final CaptchaElement captcha;

    private final MCheckBox adultBox, termsBox;

    private final Button registerButton;

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

    public RegisterView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");

        captcha = new CaptchaElement();

        Image logo = new Image("./images/logo.png");
        logo.addStyleName("m-logo");

        QuoteElement quote = new QuoteElement();
        quote.addStyleName("m-quote");

        errorLabel = new Label("");

        VerticalPanel formPanel = new VerticalPanel();
        formPanel.addStyleName("m-button-panel");

        userField = new MTextBox();
        emailField = new MEmailTextBox();
        passField = new MPasswordTextBox();
        passField2 = new MPasswordTextBox();
        adultBox = new MCheckBox();
        termsBox = new MCheckBox();

        adultBox.setValue(false);
        termsBox.setValue(false);

        FormEntry userEntry = new FormEntry("Username: ", userField);
        FormEntry emailEntry = new FormEntry("Email: ", emailField);
        FormEntry passEntry = new FormEntry("Password: ", passField);
        FormEntry passEntry2 = new FormEntry("Retype pass: ", passField2);
        FormEntry adultEntry = new FormEntry("Are you +18?", adultBox);
        FormEntry termsEntry = new FormEntry("Accept the terms?", termsBox);
        HTML terms = new HTML("Read the <a href=\"#terms\">terms</a> of usage!");
        HTML captchaText = new HTML("Answer the captcha/riddle below");

        errorLabel.addStyleName("m-margin");

        registerButton = new Button("Register");
        registerButton.addStyleName("m-button");

        registerButton.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                register();
            }
        });

        formPanel.add(userEntry);
        formPanel.add(emailEntry);
        formPanel.add(passEntry);
        formPanel.add(passEntry2);
        formPanel.add(adultEntry);
        formPanel.add(terms);
        formPanel.add(termsEntry);
        formPanel.add(captchaText);
        formPanel.add(captcha.getFormEntry());
        formPanel.add(errorLabel);
        formPanel.add(registerButton);

        main.add(logo);
        main.add(quote);
        main.add(formPanel);
    }

    private void register() {
        // First, we validate the input.
        errorLabel.setText("");
        termsBox.removeStyleName("error-background");
        final String username = userField.getText();
        final String email = emailField.getText();
        final String password = passField.getText();
        final String password2 = passField2.getText();
        final boolean adult = adultBox.getValue();

        registerButton.setDisabled(true);

        if(!FieldVerifier.isValidUsername(username)) {
            errorLabel.setText("The username is not valid.");
            registerButton.setDisabled(false);
        } else if (!FieldVerifier.isValidPassword(password, password2)) {
            errorLabel.setText("The password is not valid.");
            registerButton.setDisabled(false);
        } else if (!FieldVerifier.isValidEmail(email)) {
            errorLabel.setText("That is not a valid email.");
            registerButton.setDisabled(false);
        } else if (!termsBox.getValue()) {
            errorLabel.setText("Terms of usage not accepted.");
            termsBox.addStyleName("error-background");
            registerButton.setDisabled(false);
        } else {
            captchaService.verify(captcha.getAnswer().toLowerCase(), captcha.getToken(), new AsyncCallback<Boolean>() {

                @Override
                public void onFailure(Throwable caught) {
                    errorLabel.setText(caught.getMessage());
                    registerButton.setDisabled(false);
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
                                        errorLabel.setText(caught.getMessage());
                                        registerButton.setDisabled(false);
                                    }

                                    @Override
                                    public void onSuccess(Void result) {
                                        History.newItem("");
                                        HandyTools.showDialogBox("Success!", new HTML("Welcome, you are now registered!"
                                                + "<p>You might want to check out the <a href=\"#tutorial\">#tutorial</a></p>"));
                                    }
                                });
                    } else {
                        // Show the RPC error message to the user
                        errorLabel.setText("The captcha answer was invalid");
                        registerButton.setDisabled(false);
                    }
                }
            });
        }
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
        // TODO Auto-generated method stub
    }
}
