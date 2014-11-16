package net.mindlevel.client.pages;

import net.mindlevel.client.tools.UserTools;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class Chat extends Page {

    public Chat() {
        super();
        init();
    }

    @Override
    protected void init() {
        int height = Window.getClientHeight()-RootPanel.get("topheader").getOffsetHeight()-6;
        RootPanel.get("chat-frame").setStyleName("superhidden", false);
        RootPanel.get("chat-frame").setHeight(height+"px");
        if(RootPanel.get("chat-frame").getElement().getAttribute("src").equals("")) {
            RootPanel.get("chat-frame").getElement().setAttribute("src", "https://webchat.freenode.net?nick=" + UserTools.getUsername() + "&channels=mindlevel&uio=d4");
            Window.addResizeHandler(new ResizeHandler() {
                @Override
                public void onResize(ResizeEvent event) {
                    int height = Window.getClientHeight()-RootPanel.get("topheader").getOffsetHeight()-7;
                    RootPanel.get("chat-frame").setHeight(height+"px");
                }
            });
        }
    }
}
