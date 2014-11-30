package net.mindlevel.client.pages;

import net.mindlevel.client.widgets.NotFoundElement;

import com.google.gwt.user.client.ui.RootPanel;

public class NotFound extends Page {

    public NotFound() {
        super();
        init();
    }

    @Override
    protected void init() {
        appArea.add(new NotFoundElement());
    }
}
