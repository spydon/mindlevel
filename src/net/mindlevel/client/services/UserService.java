package net.mindlevel.client.services;

import java.util.Date;
import java.util.List;

import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.User;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {
    User getUser(String userId) throws IllegalArgumentException;
    List<User> getUsers(int start, int end) throws IllegalArgumentException;
    List<User> getUsers(int start, int end, Constraint constraint) throws IllegalArgumentException;
    List<User> getHighscore(int start, int end) throws IllegalArgumentException;
    List<User> getUsers() throws IllegalArgumentException;
    int getUserCount() throws IllegalArgumentException;
    User getUserFromToken(String token) throws IllegalArgumentException;
    Void setProfilePicture(String filename, boolean adult, String username, String token);
    Void updateProfile(String realName, String location, String about, boolean adult, String username, String token);
    Void banUser(String username, String reason, Date expiry, String adminName, String token) throws IllegalArgumentException;
    List<User> getNewestUsers(int number) throws IllegalArgumentException;
    List<User> getLastLogins(int number) throws IllegalArgumentException;
}
