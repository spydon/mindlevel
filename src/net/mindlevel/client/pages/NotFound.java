package net.mindlevel.client.pages;

import net.mindlevel.client.widgets.NotFoundElement;

import com.google.gwt.user.client.ui.RootPanel;

public class NotFound extends Page {
    private final RootPanel appArea;

    public NotFound(RootPanel appArea) {
        super();
        this.appArea = appArea;
        init();
    }

    @Override
    protected void init() {
        appArea.add(new NotFoundElement());
    }
}
