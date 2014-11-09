package net.mindlevel.client.pages;

import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.widgets.LoginsSection;
import net.mindlevel.client.widgets.NewsSection;
import net.mindlevel.client.widgets.PictureSection;
import net.mindlevel.client.widgets.UserTagElement;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Home extends Page{
    private final RootPanel appArea;
    private final FlowPanel backPanel;

    public Home(RootPanel appArea) {
        History.newItem("", false);
        this.appArea = appArea;
        this.backPanel = new FlowPanel();
        init();
    }

    @Override
    protected void init() {
        backPanel.addStyleName("home-panel");
        FlowPanel userPanel = new FlowPanel();
        NewsSection news = new NewsSection(3);
        PictureSection ps = new PictureSection(3, true);
        LoginsSection lls = new LoginsSection(5);
//        NewestUsersSection nsu = new NewestUsersSection(1);
        userPanel.addStyleName("home-user-panel");
        userPanel.add(lls);
//        userPanel.add(nsu);
        if(UserTools.isLoggedIn()) {
            backPanel.add(new UserTagElement(Mindlevel.user, true, true));
        }
        backPanel.add(news);
        backPanel.add(ps);
        backPanel.add(userPanel);
        appArea.add(backPanel);
    }
}
