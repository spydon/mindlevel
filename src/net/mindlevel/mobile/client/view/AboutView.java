package net.mindlevel.mobile.client.view;

import net.mindlevel.client.widgets.QuoteElement;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.image.ComputerImageButton;

public class AboutView extends MPage {
    protected VerticalPanel main;

    public AboutView() {
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

        HTML about = new HTML("<h1>About</h1><p>I think that most people probably wouldn't even appreciate the repetitive parts of their lives if they "
                            + "had an easy option of doing something more exciting, something that will be worth remembering.</p>"
                            + "<p>This is my attempt of giving you that option. /<a href=\"#user=spydon\">spydon</a></p>"
                            + "</br>"
                            + "<p>Tutorial at <a href=\"#tutorial\">#tutorial</a></p> <br />"
                            + "<p>Note that this is the mobile version of the site.</p>");
        about.addStyleName("m-about");

//        ImageButton swap = new ImageButton("Swap to desktop version");
        ComputerImageButton swap = new ComputerImageButton();
        swap.addStyleName("m-button");
        swap.addStyleName("5px-bottom-margin");
        swap.setWidth("100%");
        swap.setText("Swap to desktop version");
//        Button swap = new Button("Swap to desktop version");
        swap.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                Cookies.setCookie("platform", "desktop");
                Window.Location.reload();
            }
        });

        main.add(logo);
        main.add(quote);
        main.add(about);
        main.add(swap);
    }

    @Override
    public Widget asWidget() {
        onLoad();
        return main;
    }
}