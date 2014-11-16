package net.mindlevel.mobile.client.view;

import net.mindlevel.client.pages.Missions;
import net.mindlevel.client.tools.UserTools;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MissionsView extends MPage {
    protected VerticalPanel appArea;
    protected SimplePanel main;

    private Missions missions;
    private boolean initialized = false;

    public MissionsView() {
        appArea = new VerticalPanel();
        main = new SimplePanel();
        main.add(appArea);
        main.addStyleName("desktop");
        appArea.addStyleName("m-missions");
    }

    public void init() {
        initialized = true;
        missions = new Missions(appArea);
    }

    @Override
    public Widget asWidget() {
        if(!initialized) {
            init();
        }
        if(UserTools.isLoggedIn()) {
            missions.addSuggestionButton(); //Adds a suggestion button if it doesn't already exist
        } else {
            missions.removeSuggestionButton();
        }
        onLoad();
        return main;
    }
}