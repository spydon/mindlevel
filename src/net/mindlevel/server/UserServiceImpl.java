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
                    + "adult, location, created, about, picture, score, last_login FROM user WHERE username = ?");
            ps.setString(1, username);
            ArrayList<User> users = queryUsers(ps, false);
            if(users.size() > 0) {
                user = users.get(0);
            } else {
                throw new IllegalArgumentException("No such user.");
            }
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
                    + "token, adult, location, created, about, picture, score, last_login FROM user WHERE token = ?");
            ps.setString(1, token);
            ArrayList<User> users = queryUsers(ps, false);
            if(users.size() > 0) {
                user = users.get(0);
            } else {
                throw new IllegalArgumentException("No such user.");
            }
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
                    + "adult, location, created, about, picture, score, last_login FROM user WHERE username <> 'system' ORDER BY username LIMIT ?,?");
            ps.setInt(1, start);
            ps.setInt(2, end);
            users.addAll(queryUsers(ps, false));
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
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS count FROM user WHERE username <> 'system'");
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
                    + "adult, location, created, about, picture, score, last_login FROM user "
                    + "WHERE username <> 'system' ORDER BY username");
            users.addAll(queryUsers(ps, false));
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> getLastLogins(int number) throws IllegalArgumentException {
        ArrayList<User> users = new ArrayList<User>();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username, name, location, picture, score, last_login FROM user "
                    + "WHERE username <> 'system' ORDER BY created desc LIMIT 0,?");
            ps.setInt(1, number);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setLocation(rs.getString("location"));
                user.setPicture(rs.getString("picture"));
                user.setScore(rs.getInt("score"));
                user.setLastLogin(rs.getTimestamp("last_login"));
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
    public List<User> getNewestUsers(int number) throws IllegalArgumentException {
        ArrayList<User> users = new ArrayList<User>();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username, name, location, picture, score, created FROM user "
                    + "WHERE username <> 'system' ORDER BY created desc LIMIT 0,?");
            ps.setInt(1, number);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setLocation(rs.getString("location"));
                user.setPicture(rs.getString("picture"));
                user.setScore(rs.getInt("score"));
                user.setCreated(rs.getTimestamp("created"));
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

    private ArrayList<User> queryUsers(PreparedStatement psOuter, boolean withToken) {
        ArrayList<User> users = new ArrayList<User>();
        try {
            Connection conn = getConnection();
            String stmt = psOuter.toString();
            stmt = stmt.substring(stmt.indexOf(' ')+1);
            psOuter.close();
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setPermission(rs.getInt("permission"));
                if(withToken) {
                    user.setToken(rs.getString("token"));
                }
                user.setAdult(rs.getBoolean("adult"));
                user.setLocation(rs.getString("location"));
                user.setCreated(rs.getTimestamp("created"));
                user.setAbout(rs.getString("about"));
                user.setPicture(rs.getString("picture"));
                user.setScore(rs.getInt("score"));
                user.setLastLogin(rs.getTimestamp("last_login"));
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