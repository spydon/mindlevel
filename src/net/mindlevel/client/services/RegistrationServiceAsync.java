package net.mindlevel.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface RegistrationServiceAsync {
	void register(String login, String password, boolean adult, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
