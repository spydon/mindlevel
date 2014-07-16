package net.mindlevel.mobile.client.view;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AboutView extends MPage {
    protected VerticalPanel main;

    public AboutView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");
        init();
    }

    public void init() {
        Image logo = new Image("./images/logo.png");
        logo.addStyleName("m-logo");

        HTML about = new HTML("<h1>About</h1><p>I think that most people probably wouldn't even appreciate the repetitive parts of their lives if they "
                            + "had an easy option of doing something more exciting, something that will be worth remembering.</p>"
                            + "<p>This is my attempt of giving you that option. /<a href=\"#user=spydon\">spydon</a></p>"
                            + "</br>"
                            + "<p>Note that this is the mobile version of the site.</p>");
        about.addStyleName("m-about");

        main.add(logo);
        main.add(about);
//        main.add(new HTML("(Click on the table to get more information about a user)"));
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