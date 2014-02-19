package net.mindlevel.client.pages.dialog;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UpdateProfile {
    private FlexTable t;
    private final HTML header;
    private final DecoratedPopupPanel popup;
    private final VerticalPanel panel;
    private final User user;

    private final UserServiceAsync userService = GWT
            .create(UserService.class);


    public UpdateProfile(User user) {
        this.user = user;
        popup = new DecoratedPopupPanel(false);
        panel = new VerticalPanel();
        header = new HTML("<h1>Edit profile</h1>");
        init();
    }

    private void init() {
        // Initiate the FlexTable
        t = new FlexTable();

        Label nameL = new Label("Real Name");
        final TextBox nameTB = new TextBox();
        nameTB.setText(user.getName());
        Label locationL = new Label("Location");
        final TextBox locationTB = new TextBox();
        locationTB.setText(user.getLocation());
        Label aboutL = new Label("About");
        final TextArea aboutTA = new TextArea();
        aboutTA.setStylePrimaryName("about-textarea");
        aboutTA.setText(user.getAbout());
        Label adultL = new Label("Adult");
        final CheckBox adultCB = new CheckBox();
        adultCB.setValue(user.isAdult());
        Button uploadB = new Button("Finish editing");
        uploadB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                userService.updateProfile(nameTB.getText(),
                                          locationTB.getText(),
                                          aboutTA.getText(),
                                          adultCB.getValue(),
                                          Mindlevel.user.getUsername(),
                                          Mindlevel.user.getToken(),
                                          new AsyncCallback<Void>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                            }

                            @Override
                            public void onSuccess(Void result) {
                                HandyTools.showDialogBox("Success", new HTML("Successfully edited profile!"));
                                popup.hide();
                                History.fireCurrentHistoryState();
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
        t.setWidget(0, 0, nameL);
        t.setWidget(0, 1, nameTB);
        t.setWidget(1, 0, locationL);
        t.setWidget(1, 1, locationTB);
        t.setWidget(2, 0, aboutL);
        t.setWidget(2, 1, aboutTA);
        t.setWidget(3, 0, adultL);
        t.setWidget(3, 1, adultCB);
        t.setWidget(4, 1, uploadB);
        t.setWidget(5, 1, closeB);

        panel.add(header);
        panel.add(t);
        popup.add(panel);
        popup.center();
        popup.show();
    }
}
