package net.mindlevel.client.pages;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.GreetingService;
import net.mindlevel.client.services.GreetingServiceAsync;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.client.widgets.ReadBox;
import net.mindlevel.shared.Comment;
import net.mindlevel.shared.FieldVerifier;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Home extends Page{
    private final RootPanel appArea;
    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    /**
     * Create a remote service proxy to talk to the server-side Greeting service.
     */
    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);


    /**
     * Create a remote service proxy to talk to the server-side user
     * service.
     */
    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    public Home(RootPanel appArea) {
        this.appArea = appArea;
        init();
    }

    @Override
    protected void init() {
        final Button sendButton = new Button("Send");
        final TextBox nameField = new TextBox();
        nameField.setText("Haxxor User");
        final Label errorLabel = new Label();
        // We can add style names to widgets
        sendButton.addStyleName("sendButton");

        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        appArea.add(nameField);
        appArea.add(sendButton);
        appArea.add(errorLabel);
        userService.getUser("aaaa", new AsyncCallback<User>() {

            @Override
            public void onSuccess(User result) {
                appArea.add(new ReadBox(
                        result,
                        new Comment(result.getUsername(),
                                "This is the first comment ever on this site, but now it's growing!!! How long can it be, that is the magical question.",
                                "Spydon",
                                "2014-01-26")));
            }

            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML("Something went wrong while loading comments! Guru meditation: RB01"));
            }
        });
        // Focus the cursor on the name field when the app loads
//        nameField.setFocus(true);
//        nameField.selectAll();

        // Create the popup dialog box
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Remote Procedure Call");
        dialogBox.setAnimationEnabled(true);
        final Button closeButton = new Button("Close");
        // We can set the id of a widget by accessing its Element
        closeButton.getElement().setId("closeButton");
        final Label textToServerLabel = new Label();
        final HTML serverResponseLabel = new HTML();
        VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
        dialogVPanel.add(textToServerLabel);
        dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
        dialogVPanel.add(serverResponseLabel);
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        dialogVPanel.add(closeButton);
        dialogBox.setWidget(dialogVPanel);

        // Add a handler to close the DialogBox
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dialogBox.hide();
                sendButton.setEnabled(true);
                sendButton.setFocus(true);
            }
        });

        // Create a handler for the sendButton and nameField
        class MyHandler implements ClickHandler, KeyUpHandler {
            /**
             * Fired when the user clicks on the sendButton.
             */
            @Override
            public void onClick(ClickEvent event) {
                sendNameToServer();
            }

            /**
             * Fired when the user types in the nameField.
             */
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    sendNameToServer();
                }
            }

            /**
             * Send the name from the nameField to the server and wait for a response.
             */
            private void sendNameToServer() {
                // First, we validate the input.
                errorLabel.setText("");
                String textToServer = nameField.getText();
                if (!FieldVerifier.isValidName(textToServer)) {
                    errorLabel.setText("Please enter at least four characters");
                    return;
                }

                // Then, we send the input to the server.
                sendButton.setEnabled(false);
                textToServerLabel.setText(textToServer);
                serverResponseLabel.setText("");
                greetingService.greetServer(textToServer,
                        new AsyncCallback<String>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                // Show the RPC error message to the user
                                dialogBox
                                        .setText("Remote Procedure Call - Failure");
                                serverResponseLabel
                                        .addStyleName("serverResponseLabelError");
                                serverResponseLabel.setHTML(SERVER_ERROR);
                                dialogBox.center();
                                closeButton.setFocus(true);
                            }

                            @Override
                            public void onSuccess(String result) {
                                dialogBox.setText("Remote Procedure Call");
                                serverResponseLabel
                                        .removeStyleName("serverResponseLabelError");
                                serverResponseLabel.setHTML(result);
                                dialogBox.center();
                                closeButton.setFocus(true);
                            }
                        });
            }
        }

        // Add a handler to send the name to the server
        MyHandler handler = new MyHandler();
        sendButton.addClickHandler(handler);
        nameField.addKeyUpHandler(handler);
    }
}
