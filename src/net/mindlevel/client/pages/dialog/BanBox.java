package net.mindlevel.client.pages.dialog;


import java.util.List;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.shared.User;
import net.mindlevel.shared.UserTools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.datepicker.client.DatePicker;

public class BanBox {
    private final DialogBox popup;
    private final SuggestBox userBox;
    private final TextArea reasonArea;
    private final DatePicker expiryPicker;
    private final MultiWordSuggestOracle userOracle;
    private final Button banButton;

    private final UserServiceAsync userService = GWT
            .create(UserService.class);


    public BanBox() {
        popup = new DialogBox(true);
        userOracle = new MultiWordSuggestOracle();
        userBox = new SuggestBox(this.userOracle);
        reasonArea = new TextArea();
        expiryPicker = new DatePicker();
        banButton = new Button("Ban");
        educateOracle();
        init();
    }

    private void init() {
        FlexTable t = new FlexTable();
        Label userL = new Label("Ban user: ");


        userBox.addStyleName("fullwidth");
        userBox.getElement().setPropertyString("placeholder", "User...");
        userBox.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    ban();
                }
            }
        });

        Button cancelButton = new Button("Cancel");
        banButton.addStyleName("fullwidth");
        cancelButton.addStyleName("fullwidth");
        reasonArea.addStyleName("fullwidth");
        expiryPicker.addStyleName("fullwidth");
        reasonArea.getElement().setPropertyString("placeholder", "Reason");

        cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                popup.hide();
            }
        });

        banButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                banButton.setEnabled(false);
                ban();
            }
        });

        t.setWidget(0, 0, userL);
        t.setWidget(0, 1, userBox);
        t.setWidget(2, 0, reasonArea);
        t.setWidget(3, 0, expiryPicker);
        t.setWidget(4, 0, banButton);
        t.setWidget(5, 0, cancelButton);
        t.getFlexCellFormatter().setColSpan(2, 0, 2);
        t.getFlexCellFormatter().setColSpan(3, 0, 2);
        t.getFlexCellFormatter().setColSpan(4, 0, 2);
        t.getFlexCellFormatter().setColSpan(5, 0, 2);

        popup.setText("Ban");
        popup.add(t);
        popup.center();
        popup.show();
    }

    private void educateOracle() {
        userService.getUsers(new AsyncCallback<List<User>>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(List<User> users) {
                userOracle.clear();
                for(User user : users) {
                    userOracle.add(user.getUsername());
                }
            }
        });
    }

    private void ban() {
        userService.banUser(userBox.getText(),
                            reasonArea.getText(),
                            expiryPicker.getValue(),
                            UserTools.getUsername(),
                            UserTools.getToken(),
                            new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                popup.hide();
                HandyTools.showDialogBox("Successful", new HTML(userBox.getText() + " was successfully banned until " + expiryPicker.getValue()));
            }

            @Override
            public void onFailure(Throwable caught) {
                banButton.setEnabled(true);
                HandyTools.showDialogBox("Error!", new HTML(caught.getMessage()));
            }
        });
    }
}
