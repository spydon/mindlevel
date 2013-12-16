package net.mindlevel.client.pages;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.TokenService;
import net.mindlevel.client.services.TokenServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Logout {
    private final RootPanel appArea;

    /**
     * Create a remote service proxy to talk to the server-side Greeting
     * service.
     */
    private final TokenServiceAsync tokenService = GWT
            .create(TokenService.class);

    public Logout(RootPanel appArea) {
        this.appArea = appArea;
        init();
    }

    private void init() {
        tokenService.invalidateToken(Mindlevel.user.getToken(),
                new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        // Show the RPC error message to the user
                        HandyTools.showDialogBox("Error",
                                new HTML(caught.getMessage()));
                    }

                    @Override
                    public void onSuccess(Void result) {
                        HandyTools.setLoggedOff();
                        new Home(appArea);
                        HandyTools.showDialogBox("Logged out", new HTML(
                                "You successfully logged out"));
                    }
                }
        );
    }
}
