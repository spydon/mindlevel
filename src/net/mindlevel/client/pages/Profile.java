package net.mindlevel.client.pages;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.pages.dialog.UpdateProfile;
import net.mindlevel.client.pages.dialog.UploadProfilePicture;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.client.widgets.GallerySection;
import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.SearchType;
import net.mindlevel.shared.User;
import net.mindlevel.shared.UserTools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Profile {
    private final RootPanel appArea;
    private final String userId;
    private final Button changePicture;
    private final Button changeInfo;
    private final static int PICTURE_MAXWIDTH = 150;
    private final static int PICTURE_MAXHEIGHT = 300;
    private User user;
    private String username = "system";

    /**
     * Create a remote service proxy to talk to the server-side user
     * service.
     */
    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    public Profile(RootPanel appArea, String userId) {
        History.newItem("user=" + userId, false);
        this.appArea = appArea;
        this.userId = userId;
        this.changePicture = new Button("Change picture");
        this.changeInfo = new Button("Edit info");
        init();
    }

    private void init() {
        final HorizontalPanel profilePanel = new HorizontalPanel();
        final VerticalPanel picturePanel = new VerticalPanel();
        final VerticalPanel infoPanel = new VerticalPanel();
        profilePanel.setStylePrimaryName("profile-panel");
        userService.getUser(userId, new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                appArea.clear();
                new Home(appArea);
            }

            @Override
            public void onSuccess(User userinfo) {
                appArea.clear();
                user = userinfo;
                History.newItem("user="+user.getUsername());
                String special = "";
                if(user.isModerator()) {
                    special = user.isAdmin() ? "(Admin)" : "(Moderator)";
                }

                username = user.getUsername();
                infoPanel.add(new HTML("<b>Nick:</b> " + username + " " + special));
                infoPanel.add(new HTML("<b>Name:</b> " + user.getName()));
                infoPanel.add(new HTML("<b>Score:</b> " + user.getScore()));
                infoPanel.add(new HTML("<b>Location:</b> " + user.getLocation()));
                infoPanel.add(new HTML("<b>About:</b> " + user.getAbout()));
                infoPanel.add(new HTML("<b>Last log in:</b> " + HandyTools.formatDate(user.getLastLogin())));
                infoPanel.addStyleName("profile-info-panel");

                final Image profilePicture = new Image(Mindlevel.PATH + "pictures/" + userinfo.getPicture());
                profilePicture.setVisible(false);
                profilePicture.addStyleName("profile-picture");
                profilePicture.addLoadHandler(new LoadHandler() {

                    @Override
                    public void onLoad(LoadEvent event) {
                        if(profilePicture.getWidth() > PICTURE_MAXWIDTH) {
                            double relation = (double)profilePicture.getHeight()/(double)profilePicture.getWidth();
                            profilePicture.setWidth(PICTURE_MAXWIDTH + "px");
                            profilePicture.setHeight(PICTURE_MAXWIDTH*relation + "px");
                        }
                        if(profilePicture.getHeight() > PICTURE_MAXHEIGHT) {
                            double relation = (double)profilePicture.getWidth()/(double)profilePicture.getHeight();
                            profilePicture.setHeight(PICTURE_MAXHEIGHT + "px");
                            profilePicture.setWidth(PICTURE_MAXHEIGHT*relation + "px");
                        }
                        profilePicture.setVisible(true);
                    }
                });
                picturePanel.add(profilePicture);

                if(Mindlevel.user != null && userinfo.getUsername().equals(Mindlevel.user.getUsername())) {
                    changePicture.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            new UploadProfilePicture();
                        }
                    });
                    picturePanel.add(changePicture);
                    changeInfo.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            new UpdateProfile(user);
                        }
                    });
                    infoPanel.add(changeInfo);
                }
                profilePanel.add(picturePanel);
                profilePanel.add(infoPanel);

                final Button galleryButton = new Button("Show finished missions");
                galleryButton.addStyleName("profile-gallery-button");
                galleryButton.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent arg0) {
                        Constraint constraint = new Constraint();
                        constraint.setUsername(username);
                        constraint.setValidated(true);
                        constraint.setType(SearchType.PICTURE);
                        constraint.setToken(UserTools.getToken());
                        constraint.setAdult(UserTools.isAdult());
                        GallerySection gallerySection = new GallerySection(constraint);
                        appArea.add(new HTML("<h1>Finished missions by " + username + "</h>"));
                        appArea.add(gallerySection);
                        galleryButton.removeFromParent();
                    }
                });
                appArea.add(profilePanel);
                appArea.add(galleryButton);

            }
        });
    }
}
