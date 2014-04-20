package net.mindlevel.client.pages;

import net.mindlevel.client.widgets.LastLoginsSection;
import net.mindlevel.client.widgets.NewsSection;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Home extends Page{
    private final RootPanel appArea;
    private final HorizontalPanel backPanel;

    public Home(RootPanel appArea) {
        this.appArea = appArea;
        this.backPanel = new HorizontalPanel();
        init();
    }

    @Override
    protected void init() {
        NewsSection news = new NewsSection(3);
        LastLoginsSection lgs = new LastLoginsSection(5);
        backPanel.add(news);
        backPanel.add(lgs);
        appArea.add(backPanel);
    }
}
