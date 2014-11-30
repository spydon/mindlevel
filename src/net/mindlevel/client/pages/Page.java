package net.mindlevel.client.pages;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class Page {

    protected RootPanel appArea;

    public Page() {
        appArea = RootPanel.get("apparea");
        Window.scrollTo(0, 0);
    }
    protected abstract void init();
}
