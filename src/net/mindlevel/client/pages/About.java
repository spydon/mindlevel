package net.mindlevel.client.pages;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class About {
    private final RootPanel appArea;

    public About(RootPanel appArea) {
        this.appArea = appArea;
        init();
    }

    private void init() {
        HTML about = new HTML(
                "<h1>About MindLevel</h1>"
                        + "<p>It all started after a few months of backpacking in Australia "
                        + "when it felt like my brain were going numb. I wanted a programming "
                        + "project which I could exercise my brain muscles a bit on, but I also "
                        + "wanted it to be something that wouldn't just die once I were done with it. "
                        + "I thought it would be really cool to build something that had a bit of "
                        + "a community around it.</p>"
                        + "<p>After some deep thinking sessions(in Byron Bay) I came up with a question: "
                        + "\"What do I like the most?\" The answer to that question is "
                        + "not very hard to answer for people that have known me for a while. I "
                        + "really like doing crazy and wild things. They can be completely random, "
                        + "like climbing up a flagpole or something more planned like skydiving or highlining.</p> "
                        + "<p>So I thought, there must be quite a few more crazy people out there in the "
                        + "world, let's see if we can inspire them to be a little bit crazier and "
                        + "live a little bit more on the edge. The idea of MindLevel was born.</p>"
                        + "<p>So if you like to test your boundaries and meet other crazy people, MindLevel "
                        + "is the place for you!</p>"
                        + "<br />"
                        + "For questions contact <a href=\"#user=spydon\">spydon</a>.<br />"
                        + "<a href=\"#report\">Report</a> bugs, abuse and feature requests via the report form.");
        about.addStyleName("about");
        appArea.add(about);
    }
}
