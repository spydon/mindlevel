package net.mindlevel.client.services;

import java.util.List;

import net.mindlevel.shared.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>UserService</code>.
 */
public interface HighscoreServiceAsync {
	void getUsers(int start, int end, AsyncCallback<List<User>> callback)
			throws IllegalArgumentException;
	void getUserCount(AsyncCallback<Integer> callback)
			throws IllegalArgumentException;
}
