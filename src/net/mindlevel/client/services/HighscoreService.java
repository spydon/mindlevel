package net.mindlevel.client.services;

import java.util.List;

import net.mindlevel.shared.User;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("highscore")
public interface HighscoreService extends RemoteService {
	List<User> getUsers(int start, int end) throws IllegalArgumentException;
	int getUserCount() throws IllegalArgumentException;
}
