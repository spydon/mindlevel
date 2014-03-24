package net.mindlevel.server;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.mindlevel.client.services.UserService;
import net.mindlevel.shared.User;

@SuppressWarnings("serial")
public class UserServiceImpl extends DBConnector implements UserService {
    @Override
    public User getUser(String username) throws IllegalArgumentException {
        User user = new User();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username, name, permission_id As permission, "
                    + "adult, location, created, about, picture, last_login FROM user WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setPermission(rs.getInt("permission"));
                user.setAdult(rs.getBoolean("adult"));
                user.setLocation(rs.getString("location"));
                user.setCreated(rs.getString("created"));
                user.setAbout(rs.getString("about"));
                user.setPicture(rs.getString("picture"));
                user.setLastLogin(rs.getLong("last_login"));
            } else {
                throw new IllegalArgumentException("No such user.");
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User getUserFromToken(String token) throws IllegalArgumentException {
        User user = new User();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username, name, permission_id As permission, "
                    + "token, adult, location, created, about, picture, last_login FROM user WHERE token = ?");
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setPermission(rs.getInt("permission"));
                user.setToken(rs.getString("token"));
                user.setAdult(rs.getBoolean("adult"));
                user.setLocation(rs.getString("location"));
                user.setCreated(rs.getString("created"));
                user.setAbout(rs.getString("about"));
                user.setPicture(rs.getString("picture"));
                user.setLastLogin(rs.getLong("last_login"));
            } else {
                throw new IllegalArgumentException("No such user.");
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private final ArrayList<User> users = new ArrayList<User>();
    @Override
    public List<User> getUsers(int start, int end) throws IllegalArgumentException {
        try {
            Connection conn = getConnection();
            users.clear();
            PreparedStatement ps = conn.prepareStatement("SELECT username, name, permission_id As permission, "
                    + "adult, location, created, about, picture, last_login FROM user ORDER BY username LIMIT ?,?");
            ps.setInt(1, start);
            ps.setInt(2, end);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setPermission(rs.getInt("permission"));
                user.setAdult(rs.getBoolean("adult"));
                user.setLocation(rs.getString("location"));
                user.setCreated(rs.getString("created"));
                user.setAbout(rs.getString("about"));
                user.setPicture(rs.getString("picture"));
                user.setLastLogin(rs.getLong("last_login"));
                users.add(user);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public int getUserCount() throws IllegalArgumentException {
        int count = 0;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS count FROM user");
            ResultSet rs = ps.executeQuery();
            if(rs.first()) {
                count = rs.getInt("count");
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean userExists(String username) {
        boolean exists = false;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username FROM user WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.first()) {
                exists = true;
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    @Override
    public List<User> getUsers() throws IllegalArgumentException {
        try {
            Connection conn = getConnection();
            users.clear();
            PreparedStatement ps = conn.prepareStatement("SELECT username, name, permission_id As permission, "
                    + "adult, location, created, about, picture, last_login FROM user ORDER BY username");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setPermission(rs.getInt("permission"));
                user.setAdult(rs.getBoolean("adult"));
                user.setLocation(rs.getString("location"));
                user.setCreated(rs.getString("created"));
                user.setAbout(rs.getString("about"));
                user.setPicture(rs.getString("picture"));
                user.setLastLogin(rs.getLong("last_login"));
                users.add(user);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Void setProfilePicture(String filename, boolean adult, String username, String token) throws IllegalArgumentException {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE user SET picture = ?, picture_adult = ? WHERE username = ?");
            int result = 0;
            File file = new File("./pictures/" + filename);
            if(new TokenServiceImpl().validateAuth(username, token) && file.exists()) {
                ps.setString(1, filename);
                ps.setBoolean(2, adult);
                ps.setString(3, username);
                result = ps.executeUpdate();
            } else {
                throw new IllegalArgumentException("Seems like you are trying to do something dodgy. This was logged.");
            }
            if(result != 1) {
                throw new UnknownError("Something went wrong");
            }
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Void updateProfile(String name,
                             String location,
                             String about,
                             boolean adult,
                             String username,
                             String token) throws IllegalArgumentException {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE user SET name = ?, location = ?, about = ?, adult = ? WHERE username = ?");
            int result = 0;
            if(new TokenServiceImpl().validateAuth(username, token)) {
                ps.setString(1, escapeHtml4(name));
                ps.setString(2, escapeHtml4(location));
                ps.setString(3, escapeHtml4(about));
                ps.setBoolean(4, adult);
                ps.setString(5, username);
                result = ps.executeUpdate();
            } else {
                throw new IllegalArgumentException("You are trying to modify somebody elses profile. This was logged.");
            }
            if(result != 1) {
                throw new UnknownError("Something went wrong");
            }
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}