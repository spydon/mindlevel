package net.mindlevel.client.pages;

import net.mindlevel.client.Mindlevel;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

public class Chat {
    private final RootPanel appArea;

    public Chat(RootPanel appArea) {
        this.appArea = appArea;
        init();
    }

    public void init() {
        int height = Window.getClientHeight()-RootPanel.get("topheader").getOffsetHeight();
        final Frame iFrame = new Frame("http://webchat.freenode.net?nick="
                + Mindlevel.user.getUsername()
                + "&channels=mindlevel&uio=d4");
        iFrame.setHeight(height + "px");
        iFrame.setWidth("100%");
        Window.enableScrolling(false);
        appArea.add(iFrame);
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                int height = Window.getClientHeight()-RootPanel.get("topheader").getOffsetHeight();
                iFrame.setHeight(height+"px");
            }
        });
    }
}
