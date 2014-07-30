package net.mindlevel.server;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mindlevel.client.services.UserService;
import net.mindlevel.shared.Constraint;
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
            ArrayList<User> users = queryUsers(ps, true);
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
    public List<User> getUsers(int start, int offset) throws IllegalArgumentException {
        return getUsers(start, offset, new Constraint());
    }

    @Override
    public List<User> getUsers(int start, int offset, Constraint constraint) throws IllegalArgumentException {
        ArrayList<User> users = new ArrayList<User>();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username, name, permission_id As permission, "
                    + "adult, location, created, about, picture, score, last_login FROM user WHERE username <> 'system'"
                    + "AND username LIKE ? ORDER BY username LIMIT ?,?");
            ps.setString(1, "%" + constraint.getUsername() + "%");
            ps.setInt(2, start);
            ps.setInt(3, offset);
            users.addAll(queryUsers(ps, false));
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> getHighscore(int start, int end) throws IllegalArgumentException {
        ArrayList<User> users = new ArrayList<User>();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username, name, permission_id As permission, "
                    + "adult, location, created, about, picture, score, last_login FROM user WHERE username <> 'system' ORDER BY score desc LIMIT ?,?");
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
        ArrayList<User> users = new ArrayList<User>();
        try {
            Connection conn = getConnection();
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
                    + "WHERE username <> 'system' ORDER BY last_login desc LIMIT 0,?");
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
            File file = new File(ServerTools.PATH + "pictures/" + filename);
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
    public void changePassword(String username, String oldPassword, String password, String token) throws IllegalArgumentException {
        Connection conn = getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE user SET password = SHA2(CONCAT(SHA2(?, 512),SHA2(?, 512)),512)"
                    + " WHERE username = ? AND token = ? AND password = SHA2(CONCAT(SHA2(?, 512),SHA2(?, 512)),512)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, username);
            ps.setString(4, token);
            ps.setString(5, username);
            ps.setString(6, oldPassword);
            int result = ps.executeUpdate();
            if(result != 1) {
                throw new IllegalArgumentException(
                        "Password could not be changed.");
            }
            ps.close();
            conn.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public Void banUser(String username, String reason, Date expiry, String adminName, String token) throws IllegalArgumentException {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO ban (username, reason, expires) VALUES (?, ?, ?)");
            TokenServiceImpl tokenService = new TokenServiceImpl();
            if(tokenService.validateAuth(adminName, token) && tokenService.validateAdminToken(token)) {
                ps.setString(1, username);
                ps.setString(2, reason);
                ps.setTimestamp(3, new Timestamp(expiry.getTime()));
                ps.executeUpdate();
                tokenService.invalidateUserToken(username);
            } else {
                throw new IllegalArgumentException("Seems like you are trying to do something dodgy. This was logged.");
            }
            ps.close();
            conn.close();
        } catch(SQLException e) {
            throw new IllegalArgumentException("That user does not exist.");
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
                ps.setString(1, name);
                ps.setString(2, location);
                ps.setString(3, about);
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