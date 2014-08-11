package net.mindlevel.mobile.client.view;

import net.mindlevel.client.widgets.NotFoundElement;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NotFoundView extends MPage {
    protected VerticalPanel main;
    private boolean initialized = false;

    public NotFoundView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");
    }

    protected void init() {
        initialized = true;
        main.add(new NotFoundElement());
    }

    @Override
    public Widget asWidget() {
        onLoad();
        if(!initialized) {
            init();
        }
        return main;
    }
}