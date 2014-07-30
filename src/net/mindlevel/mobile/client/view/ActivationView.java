package net.mindlevel.mobile.client.view;

import net.mindlevel.client.pages.Activate;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ActivationView extends MPage {
    protected VerticalPanel main;

    public ActivationView() {
    }

    private void activate() {
        new Activate(parameter);
    }

    @Override
    public Widget asWidget() {
        onLoad();
        activate();
        return main;
    }
}