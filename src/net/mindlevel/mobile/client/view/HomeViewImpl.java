package net.mindlevel.mobile.client.view;

import net.mindlevel.client.widgets.NewsSection;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.panel.flex.RootFlexPanel;


public class HomeViewImpl implements HomeView {
    protected Presenter p;
    protected RootFlexPanel main;

    public HomeViewImpl() {
        main = new RootFlexPanel();

        Image logo = new Image("./images/logo.png");
        logo.addStyleName("logo");

        NewsSection news = new NewsSection(1);
        main.add(logo);
        main.add(news);
    }

    @Override
    public Widget asWidget() {
        return main;
    }

    @Override
    public void setPresenter(Presenter p) {
        this.p = p;
    }

}
