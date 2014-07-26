package net.mindlevel.mobile.client.view;

import net.mindlevel.mobile.client.MindlevelMobile;

import com.google.gwt.user.client.ui.IsWidget;

public abstract class MPage implements IsWidget {
    protected String session, parameter;

    protected void onLoad() {
        MindlevelMobile.showBar();
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
