package net.mindlevel.client.services;

import net.mindlevel.shared.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>UserService</code>.
 */
public interface UserServiceAsync {
	void getUser(String userId, AsyncCallback<User> callback)
			throws IllegalArgumentException;
}
