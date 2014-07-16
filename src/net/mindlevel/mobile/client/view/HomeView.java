package net.mindlevel.mobile.client.view;

import net.mindlevel.client.widgets.QuoteElement;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.Button;

public class HomeView extends MPage {
    protected VerticalPanel main;

    public HomeView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-home");

        Image logo = new Image("./images/logo.png");
        logo.addStyleName("m-logo");

        QuoteElement quote = new QuoteElement();
        quote.addStyleName("m-quote");

        VerticalPanel buttonPanel = new VerticalPanel();
        buttonPanel.addStyleName("m-button-panel");

        Button picturesB = new Button("Pictures");
        Button highscoreB = new Button("Highscore");
        Button missionB = new Button("Missions");
        Button aboutB = new Button("About");
        Button loginB = new Button("Login");
        picturesB.addStyleName("m-button");
        highscoreB.addStyleName("m-button");
        missionB.addStyleName("m-button");
        aboutB.addStyleName("m-button");
        loginB.addStyleName("m-button");

        picturesB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("picture");
            }
        });

        loginB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("login");
            }
        });

        highscoreB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("highscore");
            }
        });

        aboutB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("about");
            }
        });

        missionB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("missions");
            }
        });


        buttonPanel.add(picturesB);
        buttonPanel.add(highscoreB);
        buttonPanel.add(missionB);
        buttonPanel.add(aboutB);
        buttonPanel.add(loginB);

        main.add(logo);
        main.add(quote);
        main.add(buttonPanel);
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
