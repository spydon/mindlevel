package net.mindlevel.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("reg")
public interface RegistrationService extends RemoteService {
	String register(String login, String password, boolean adult) throws IllegalArgumentException;
}
