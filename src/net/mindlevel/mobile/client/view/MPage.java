package net.mindlevel.mobile.client.view;

import net.mindlevel.mobile.client.MindlevelMobile;

import com.google.gwt.user.client.ui.IsWidget;

public abstract class MPage implements IsWidget {
    protected String session;

    public abstract void setId(int id);
    public abstract void setId(String id);

    protected void onLoad() {
        MindlevelMobile.showBar();
    }

    public void setSession(String session) {
        this.session = session;
    }
}
