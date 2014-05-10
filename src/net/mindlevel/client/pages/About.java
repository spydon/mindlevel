package net.mindlevel.client.pages;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class About {
    private final RootPanel appArea;

    public About(RootPanel appArea) {
        this.appArea = appArea;
        init();
    }

    private void init() {
        Image logo = new Image("./images/logo.png");
        HTML about = new HTML(
                "<h1>About MindLevel</h1>"
                + "<p>After a few months of backpacking I came to realize that my life at home was quite "
                + "repetitive and boring. (Even though I was living in a student town full of life)</p>"
                + "<p>In most peoples' day-to-day life they perform repetitive and seemingly pointless tasks, I'm sure "
                + "you know what I'm talking about. Repetitive tasks that we've grown to think that we like, "
                + "things that take up a lot of our time but don't add anything to our lives. </p>"
                + "<p>The things that we wont remember when we grow old.</p>"
                + "<p>I think that most people probably wouldn't even appreciate these parts of their lives if they "
                + "had an easy option of doing something more exciting, something that will be worth remembering.</p>"
                + "<p>This is my attempt of giving you that option.</p>"
                + "<p>/<a href=\"#user=spydon\">spydon</a>.<br />"
                + "<p>Tutorial at <a href=\"#tutorial\">#tutorial</a> <br />"
                + "File bugs, abuse and feature requests at <a href=\"#report\">#report</a></p>");
        about.addStyleName("about");
        appArea.add(logo);
        appArea.add(about);
    }
}
