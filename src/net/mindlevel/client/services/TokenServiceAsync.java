package net.mindlevel.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>TokenService</code>.
 */
public interface TokenServiceAsync {
    void getToken(String login, String password, AsyncCallback<String> callback)
            throws IllegalArgumentException;
    void validateToken(String token, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;
    void invalidateToken(String token, AsyncCallback<Void> callback)
            throws IllegalArgumentException;
}
