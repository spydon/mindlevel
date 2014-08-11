package net.mindlevel.client.pages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class About {
    private final RootPanel appArea;
    private final VerticalPanel aboutPanel;

    public About(RootPanel appArea) {
        this.appArea = appArea;
        this.aboutPanel = new VerticalPanel();
        aboutPanel.addStyleName("about-panel");
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
                + "<p>This is my attempt of giving you such an option.</p>"
                + "<p>/<a href=\"#user=spydon\">spydon</a>.<br />"
                + "<p>Tutorial at <a href=\"#tutorial\">#tutorial</a> <br />"
                + "File bugs, abuse and feature requests at <a href=\"#report\">#report</a><br />"
                + "Other questions etc can be sent to info@mindlevel.net</p>");
        about.addStyleName("about");

        Button swap = new Button("Swap to mobile version");
        swap.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Cookies.setCookie("platform", "mobile");
                Window.Location.reload();
            }
        });

        aboutPanel.add(logo);
        aboutPanel.add(about);
        aboutPanel.add(swap);
        appArea.add(aboutPanel);
    }
}
