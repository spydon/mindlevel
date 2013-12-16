package net.mindlevel.client.services;

import net.mindlevel.shared.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface LoginServiceAsync {
	void login(String login, String password, AsyncCallback<User> callback)
			throws IllegalArgumentException;
}
