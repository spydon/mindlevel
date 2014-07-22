package net.mindlevel.mobile.client.view;

import net.mindlevel.client.pages.Missions;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MissionsView extends MPage {
    protected VerticalPanel appArea;
    protected SimplePanel main;

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
        new Missions(appArea);
    }

    @Override
    public Widget asWidget() {
        if(!initialized) {
            init();
        }
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