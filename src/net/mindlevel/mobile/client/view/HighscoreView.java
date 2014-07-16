package net.mindlevel.mobile.client.view;

import net.mindlevel.client.widgets.HighscoreSection;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HighscoreView extends MPage {
    protected VerticalPanel main;

    public HighscoreView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");
        init();
    }

    public void init() {
        HighscoreSection highscore = new HighscoreSection(5);
        main.add(highscore);
//        main.add(new HTML("(Click on the table to get more information about a user)"));
    }

    @Override
    public Widget asWidget() {
        return main;
    }

    @Override
    public void setId(int id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
    }
}