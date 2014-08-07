package net.mindlevel.client.pages;

import net.mindlevel.client.widgets.NotFoundElement;

import com.google.gwt.user.client.ui.RootPanel;

public class NotFound {
    private final RootPanel appArea;

    public NotFound(RootPanel appArea) {
        this.appArea = appArea;
        init();
    }

    private void init() {
        appArea.add(new NotFoundElement());
    }
}
