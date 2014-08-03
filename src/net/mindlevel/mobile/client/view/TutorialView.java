package net.mindlevel.mobile.client.view;

import net.mindlevel.client.widgets.QuoteElement;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TutorialView extends MPage {
    protected VerticalPanel main;

    public TutorialView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");
        init();
    }

    protected void init() {
        Image logo = new Image("./images/logo.png");
        logo.addStyleName("m-logo");

        QuoteElement quote = new QuoteElement();
        quote.addStyleName("m-quote");

        HTML tutorial = new HTML(
                "<h2>Home</h2>"
              + "<p>Home contains six buttons: Pictures, Highscore, Missions, Search, About and Login/Logout.</p> "
              + "<h2>Pictures</h2>"
              + "<p>Here you can see all the finished missions by the users of the site. You can navigate by swiping between the pictures "
              + "or navigate with the bottom bar. "
              + "<p>To vote you press the up or down button, it costs 1 point to give an upvote and that gives the tagged users "
              + "of the picture 10 points each. It costs 10 points to downvote and that gives all the tagged users 10 minus point, "
              + "so only give out minus points if you really dislike the picture.</p>"
              + "<h2>Missions</h2>"
              + "<p>Here you can see the available missions, if you click on a mission you see more information about it. "
              + "You can also suggest missions here, if your mission gets accepted by an admin/moderator it will become visible "
              + "on the site.</p>"
              + "<p>If you click on a mission you can upload a finished mission and get points for it. In the future you will get "
              + "badges and special rights the more points you have. </p>"
              + "<h2>Highscore</h2>"
              + "<p>Here you can see the honorable users with the most score. You can press them to get more information about them.</p>"
              + "<h2>Search</h2>"
              + "<p>Search for whatever you want to, it should be quite self explanatory.</p>"
              + "<h2>About</h2>"
              + "<p>Read about mindlevel. You also find contact information here.</p>"
              + "<h2>Login/Logout</h2>"
              + "<p>This is where you login and logout, remember that you stay logged in until you log out. You can also register a new "
              + "account here.</p>"
              + "<h2>Misc</h2> "
              + "<p>If you have any further questions or concerns, or if you encounter difficulties while "
              + "using the site, please contact info@mindlevel.net or file a bug, abuse or "
              + "feature request at <a href=\"#report\">#report</a></p>."
              + "<p>Hope you have a great time here at Mindlevel! /<a href=\"#user=spydon\">spydon</a> </p>");
        tutorial.addStyleName("m-about");

        main.add(logo);
        main.add(quote);
        main.add(tutorial);
    }

    @Override
    public Widget asWidget() {
        onLoad();
        return main;
    }
}