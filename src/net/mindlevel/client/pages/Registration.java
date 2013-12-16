package net.mindlevel.client.pages;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.RegistrationService;
import net.mindlevel.client.services.RegistrationServiceAsync;
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
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Registration {
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final RegistrationServiceAsync regService = GWT
			.create(RegistrationService.class);
	
	public Registration() {
		init();
	}
	
	private void init() {
		final Button sendButton = new Button("Register");
		final TextBox userField = new TextBox();
		final PasswordTextBox passField = new PasswordTextBox();
		final PasswordTextBox passField2 = new PasswordTextBox();
		passField.setWidth("162px");
		passField2.setWidth("162px");
		final CheckBox adultBox = new CheckBox();
		final Label errorLabel = new Label();
		final Label userLabel = new Label("User: ");
		final Label passLabel = new Label("Pass: ");
		final Label passLabel2 = new Label("Retype Pass: ");
		final Label adultLabel = new Label("Are you 18+?");
		final DialogBox registrationBox = new DialogBox(true);
		final Button lbCloseButton = new Button("Close");
		final Label textToServerLabel = new Label();
		final HTML serverResponse = new HTML();
		//lbCloseButton.getElement().setId("closeButton");
		VerticalPanel loginPanel = new VerticalPanel();
		Grid gridPanel = new Grid(5, 2);
		
		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		
		// Add a panel for the errorLabels, so they don't appear 
		// on different places
		HorizontalPanel errorPanel = new HorizontalPanel();
		errorPanel.add(errorLabel);
		errorPanel.add(serverResponse);

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		gridPanel.setWidget(0, 0, userLabel);
		gridPanel.setWidget(0, 1, userField);
		gridPanel.setWidget(1, 0, passLabel);
		gridPanel.setWidget(1, 1, passField);
		gridPanel.setWidget(2, 0, passLabel2);
		gridPanel.setWidget(2, 1, passField2);		
		gridPanel.setWidget(3, 0, adultLabel);
		gridPanel.setWidget(3, 1, adultBox);
		gridPanel.setWidget(4, 1, errorPanel);
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(sendButton);
		buttonPanel.add(lbCloseButton);
		loginPanel.add(gridPanel);
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
		lbCloseButton.addClickHandler(new ClickHandler() {
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
			public void onClick(ClickEvent event) {
				sendButton.setEnabled(false);
				sendCredentials();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendButton.setEnabled(false);
					sendCredentials();
				}
			}

			/**
			 * Send the credentials to the server and wait for a response.
			 */
			private void sendCredentials() {
				// First, we validate the input.
				errorLabel.setText("");
				String username = userField.getText();
				String password = passField.getText();
				String password2 = passField2.getText();
				boolean adult = adultBox.getValue();

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(username);
				serverResponse.setText("");
				if(!FieldVerifier.isValidName(username)) {
					errorLabel.setText("The username is not valid.");
					sendButton.setEnabled(true);
				} else if (!FieldVerifier.isValidPassword(password, password2)) {
					errorLabel.setText("The passwords does not match.");
					sendButton.setEnabled(true);
				} else {
					username.toLowerCase();
					regService.register(username, password, adult,
							new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									// Show the RPC error message to the user
									registrationBox.setText("Failure");
									serverResponse.addStyleName("serverResponseLabelError");
									//serverResponseLabel.setHTML(SERVER_ERROR);
									serverResponse.setHTML(caught.getMessage());
									registrationBox.center();
									lbCloseButton.setFocus(true);
									sendButton.setEnabled(true);
								}
	
								public void onSuccess(String result) {
									serverResponse.removeStyleName("serverResponseLabelError");
									serverResponse.setHTML("Congratulations, you are now registered!");
									registrationBox.hide();
									HandyTools.showDialogBox("Success!", serverResponse);
								}
							});
				}
			}
		}

		// Add a handler to send the name to the server
		SendHandler handler = new SendHandler();
		sendButton.addClickHandler(handler);
		userField.addKeyUpHandler(handler);
		passField.addKeyUpHandler(handler);
	}
}
