package net.mindlevel.mobile.client.view;

import net.mindlevel.client.UserTools;
import net.mindlevel.client.services.PictureService;
import net.mindlevel.client.services.PictureServiceAsync;
import net.mindlevel.shared.MetaImage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.Button;

public class HomeView extends MPage {
    protected VerticalPanel main;
    private int pictureId = 0;
    private final Button loginB;
    private final Button logoutB;

    /**
     * Create a remote service proxy to talk to the server-side picture
     * service.
     */
    private final PictureServiceAsync pictureService = GWT
            .create(PictureService.class);

    public HomeView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-home");

        Image logo = new Image("./images/logo.png");
        logo.addStyleName("m-logo");

        VerticalPanel buttonPanel = new VerticalPanel();
        buttonPanel.addStyleName("m-button-panel");

        Button picturesB = new Button("Pictures");
        Button highscoreB = new Button("Highscore");
        Button missionB = new Button("Missions");
        Button searchB = new Button("Search");
        Button aboutB = new Button("About");
        loginB = new Button("Login");
        logoutB = new Button("Logout");

        picturesB.addStyleName("m-button");
        picturesB.addStyleName("m-picture-button");
        highscoreB.addStyleName("m-button");
        highscoreB.addStyleName("m-highscore-button");
        missionB.addStyleName("m-button");
        missionB.addStyleName("m-mission-button");
        searchB.addStyleName("m-button");
        searchB.addStyleName("m-search-button");
        aboutB.addStyleName("m-button");
        aboutB.addStyleName("m-about-button");
        loginB.addStyleName("m-button");
        loginB.addStyleName("m-login-button");
        logoutB.addStyleName("m-button");
        logoutB.addStyleName("m-logout-button");

        picturesB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("picture=" + pictureId);
            }
        });

        highscoreB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("highscore");
            }
        });

        missionB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("missions");
            }
        });

        searchB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("search");
            }
        });

        aboutB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("about");
            }
        });

        loginB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("login");
            }
        });

        logoutB.addStyleName("superhidden");
        logoutB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                UserTools.setLoggedOff();
                setLoggedIn(false);
            }
        });

        buttonPanel.add(picturesB);
        buttonPanel.add(highscoreB);
        buttonPanel.add(missionB);
        buttonPanel.add(searchB);
        buttonPanel.add(aboutB);
        buttonPanel.add(loginB);
        buttonPanel.add(logoutB);

        main.add(logo);
        main.add(buttonPanel);
    }

    public void setLoggedIn(boolean loggedIn) {
        if(loggedIn) {
            logoutB.removeStyleName("superhidden");
            loginB.addStyleName("superhidden");
        } else {
            loginB.removeStyleName("superhidden");
            logoutB.addStyleName("superhidden");
        }
    }

    @Override
    public Widget asWidget() {
        pictureService.get(0, true, UserTools.isAdult(), true, new AsyncCallback<MetaImage>() {

            @Override
            public void onFailure(Throwable caught) {
                //Do nothing
            }

            @Override
            public void onSuccess(final MetaImage metaImage) {
                pictureId = metaImage.getId();
            }
        });
        onLoad();
        setLoggedIn(UserTools.isLoggedIn());
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
