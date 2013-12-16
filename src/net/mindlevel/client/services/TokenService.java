package net.mindlevel.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("token")
public interface TokenService extends RemoteService {
    String getToken(String login, String password) throws IllegalArgumentException;
    boolean validateToken(String token) throws IllegalArgumentException;
    void invalidateToken(String token) throws IllegalArgumentException;
}
