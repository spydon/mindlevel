package net.mindlevel.client.pages;

import net.mindlevel.client.widgets.LoginsSection;
import net.mindlevel.client.widgets.NewestUsersSection;
import net.mindlevel.client.widgets.NewsSection;
import net.mindlevel.client.widgets.PictureSection;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Home extends Page{
    private final RootPanel appArea;
    private final HorizontalPanel backPanel;

    public Home(RootPanel appArea) {
        History.newItem("", false);
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
        LoginsSection lls = new LoginsSection(4);
        NewestUsersSection nsu = new NewestUsersSection(4);
        userPanel.addStyleName("home-user-panel");
        userPanel.add(lls);
        userPanel.add(nsu);
        backPanel.add(ps);
        backPanel.add(news);
        backPanel.add(userPanel);
        appArea.add(backPanel);
    }
}
