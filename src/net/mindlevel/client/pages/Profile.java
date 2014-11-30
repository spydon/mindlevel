package net.mindlevel.client.pages;

import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.pages.dialog.ChangePassword;
import net.mindlevel.client.pages.dialog.UpdateProfile;
import net.mindlevel.client.pages.dialog.UploadProfilePicture;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.client.tools.HtmlTools;
import net.mindlevel.client.tools.UserTools;
import net.mindlevel.client.widgets.GallerySection;
import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.SearchType;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

public class Profile extends Page {
    private final static int PICTURE_MAXWIDTH = 150;
    private final static int PICTURE_MAXHEIGHT = 300;
    private User user;
    private String username = "system";
    private String userId;

    /**
     * Create a remote service proxy to talk to the server-side user
     * service.
     */
    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    public Profile(String userId) {
        super();
        History.newItem("user=" + userId, false);
        this.userId = userId;
        init();
    }

    @Override
    protected void init() {
        final FlowPanel profilePanel = new FlowPanel();
        final FlowPanel picturePanel = new FlowPanel();
        final FlowPanel infoPanel = new FlowPanel();
        final Button changePicture = new Button("Change picture");
        final Button changeInfo = new Button("Edit info");
        final Button changePassword = new Button("Change password");
        final Button logout = new Button("Logout");
        final Button settings = new Button("|||");
        changePicture.addStyleName("profile-change-pic-button");
        profilePanel.addStyleName("profile-panel");
        picturePanel.addStyleName("profile-picture-container");
        infoPanel.addStyleName("profile-info-panel");
        settings.addStyleName("profile-settings");
        userService.getUser(userId, new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                appArea.clear();
                new Home();
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
                infoPanel.add(new HTML("<b>Name:</b> " + HtmlTools.formatHtml(user.getName())));
                infoPanel.add(new HTML("<b>Score:</b> " + user.getScore()));
                infoPanel.add(new HTML("<b>Location:</b> " + HtmlTools.formatHtml(user.getLocation())));
                infoPanel.add(new HTML("<b>About:</b><br>" + HtmlTools.formatHtml(user.getAbout())));
                infoPanel.add(new HTML("<b>Last log in:</b> " + HandyTools.formatDate(user.getLastLogin())));

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
                    final FlowPanel settingsPanel = new FlowPanel();
                    settingsPanel.addStyleName("profile-settings-panel");
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
                    settingsPanel.add(changeInfo);
                    changePassword.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent arg0) {
                            new ChangePassword();
                        }
                    });
                    settingsPanel.add(changePassword);
                    logout.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent arg0) {
                            History.newItem("logout");
                        }
                    });
                    settingsPanel.add(logout);
                    settings.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            final DialogBox db = new DialogBox(true, false);
                            db.addHandler(new BlurHandler() {

                                @Override
                                public void onBlur(BlurEvent event) {
                                    // TODO: Handle hiding when somebody presses a button
                                    db.hide();
                                }
                            }, BlurEvent.getType());
                            db.setWidget(settingsPanel);
                            db.setPopupPosition(event.getClientX()+5, event.getClientY()+5);
                            db.show();
                        }
                    });
                    infoPanel.add(settings);
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
                        HTML galleryHeader = new HTML("<h1>Finished missions by " + username + "</h>");
                        galleryHeader.addStyleName("profile-gallery-header");
                        appArea.add(galleryHeader);
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
