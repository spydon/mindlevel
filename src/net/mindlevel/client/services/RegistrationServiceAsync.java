package net.mindlevel.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>RegistrationService</code>.
 */
public interface RegistrationServiceAsync {
    void register(String login, String email, String password, boolean adult, AsyncCallback<Void> callback)
            throws IllegalArgumentException;
    void activateAccount(String uuid, AsyncCallback<Void> callback) throws IllegalArgumentException;
}
