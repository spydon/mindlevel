package net.mindlevel.client.pages.dialog;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.services.TokenService;
import net.mindlevel.client.services.TokenServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

public class Logout {

    /**
     * Create a remote service proxy to talk to the server-side Greeting
     * service.
     */
    private final TokenServiceAsync tokenService = GWT
            .create(TokenService.class);

    private final String session;

    public Logout(String session) {
        this.session = session;
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
                        UserTools.setLoggedOff();
//                        HandyTools.showDialogBox("Logged out", new HTML(
//                                "You successfully logged out"));
                        History.newItem(session);
                    }
                }
        );
    }
}
