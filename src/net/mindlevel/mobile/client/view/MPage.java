package net.mindlevel.mobile.client.view;

import com.google.gwt.user.client.ui.IsWidget;

public abstract class MPage implements IsWidget {
    protected String session;

    public abstract void setId(int id);
    public abstract void setId(String id);

    public void setSession(String session) {
        System.out.println("Session: " + session);
        this.session = session;
    }
}
