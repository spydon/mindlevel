package net.mindlevel.client.pages;

import net.mindlevel.client.services.RegistrationService;
import net.mindlevel.client.services.RegistrationServiceAsync;
import net.mindlevel.client.tools.HandyTools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

public class Activate extends Page {

    /**
     * Create a remote service proxy to talk to the server-side Registration service.
     */
    private final RegistrationServiceAsync regService = GWT
            .create(RegistrationService.class);

    public Activate(String uuid) {
        super();
        finalize(uuid);
    }

    private void finalize(String uuid) {
        regService.activateAccount(uuid, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void arg0) {
                History.newItem("login");
                HandyTools.showDialogBox("Success!", new HTML("Welcome, you are now registered!"
                        + "<p>You might want to check out the <a href=\"#tutorial\">#tutorial</a></p>"));
            }

            @Override
            public void onFailure(Throwable arg0) {
                History.newItem("");
                HandyTools.showDialogBox("Error", arg0.getMessage());
            }
        });
    }

    @Override
    protected void init() {}
}
