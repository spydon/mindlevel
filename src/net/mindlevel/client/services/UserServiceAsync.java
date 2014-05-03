package net.mindlevel.client.services;

import java.util.Date;
import java.util.List;

import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>UserService</code>.
 */
public interface UserServiceAsync {
    void getUser(String userId, AsyncCallback<User> callback)
            throws IllegalArgumentException;
    void getUsers(int start, int end, AsyncCallback<List<User>> callback)
            throws IllegalArgumentException;
    void getUsers(int start, int end, Constraint constraint, AsyncCallback<List<User>> callback)
            throws IllegalArgumentException;
    void getHighscore(int start, int end, AsyncCallback<List<User>> callback)
            throws IllegalArgumentException;
    void getUsers(AsyncCallback<List<User>> asyncCallback);
    void getUserCount(AsyncCallback<Integer> callback)
            throws IllegalArgumentException;
    void getUserFromToken(String token, AsyncCallback<User> asyncCallback);
    void setProfilePicture(String filename, boolean adult, String username, String token, AsyncCallback<Void> asyncCallback);
    void updateProfile(String realName, String location, String about, boolean adult, String username, String token, AsyncCallback<Void> asyncCallback);
    void banUser(String username, String reason, Date expiry, String adminName, String token, AsyncCallback<Void> asyncCallback)
            throws IllegalArgumentException;
    void getLastLogins(int number, AsyncCallback<List<User>> callback)
            throws IllegalArgumentException;
    void getNewestUsers(int number, AsyncCallback<List<User>> callback)
            throws IllegalArgumentException;
}
