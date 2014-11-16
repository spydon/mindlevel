package net.mindlevel.client.pages;

import com.google.gwt.user.client.Window;

public abstract class Page {

    public Page() {
        Window.scrollTo(0, 0);
    }
    protected abstract void init();
}
