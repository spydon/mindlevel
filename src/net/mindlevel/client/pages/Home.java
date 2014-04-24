package net.mindlevel.client.pages;

import net.mindlevel.client.widgets.LastLoginsSection;
import net.mindlevel.client.widgets.NewestUsersSection;
import net.mindlevel.client.widgets.NewsSection;
import net.mindlevel.client.widgets.PictureSection;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
        backPanel.addStyleName("home-panel");
        VerticalPanel userPanel = new VerticalPanel();
        PictureSection ps = new PictureSection(3, true);
        NewsSection news = new NewsSection(3);
        LastLoginsSection lls = new LastLoginsSection(4);
        NewestUsersSection nsu = new NewestUsersSection(4);
        userPanel.add(lls);
        userPanel.add(nsu);
        backPanel.add(ps);
        backPanel.add(news);
        backPanel.add(userPanel);
        appArea.add(backPanel);
    }
}
