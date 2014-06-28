package net.mindlevel.mobile.client.view;

import net.mindlevel.client.widgets.QuoteElement;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.panel.flex.RootFlexPanel;

public class HomeView implements MPage {
    protected RootFlexPanel main;

    public HomeView() {
        main = new RootFlexPanel();

        Image logo = new Image("./images/logo.png");
        logo.addStyleName("logo");

        QuoteElement quote = new QuoteElement();

        Button picturesB = new Button("Pictures");
        Button highscoreB = new Button("Highscore");
        Button missionB = new Button("Missions");
        Button aboutB = new Button("About");
        Button loginB = new Button("Login");
        picturesB.addStyleName("mbutton"); //Already has styling name
        highscoreB.addStyleName("mbutton");
        missionB.addStyleName("mbutton");
        aboutB.addStyleName("mbutton");
        loginB.addStyleName("mbutton");

        picturesB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("picture");
            }
        });

        main.add(logo);
        main.add(quote);
        main.add(picturesB);
        main.add(highscoreB);
        main.add(missionB);
        main.add(aboutB);
        main.add(loginB);
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
