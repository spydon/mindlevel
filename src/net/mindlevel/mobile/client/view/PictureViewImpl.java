package net.mindlevel.mobile.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.panel.flex.RootFlexPanel;


public class PictureViewImpl implements PictureView {
    protected Presenter p;
    protected RootFlexPanel main;

    public PictureViewImpl() {
        main = new RootFlexPanel();

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
