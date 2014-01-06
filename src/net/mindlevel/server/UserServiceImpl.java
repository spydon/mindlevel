package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.mindlevel.client.services.UserService;
import net.mindlevel.shared.User;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class UserServiceImpl extends DBConnector implements UserService {
    @Override
    public User getUser(String username) throws IllegalArgumentException {
        User user = new User();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username, permission_id As permission, "
                    + "adult, location, created, token, description, last_login FROM user WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                user.setUsername(rs.getString("username"));
                user.setPermission(rs.getInt("permission"));
                user.setAdult(rs.getBoolean("adult"));
                user.setLocation(rs.getString("location"));
                user.setCreated(rs.getString("created"));
                user.setToken(rs.getString("token"));
                user.setDescription(rs.getString("description"));
                user.setLastLogin(rs.getString("last_login"));
            } else {
                throw new IllegalArgumentException("Not logged in.");
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
            PreparedStatement ps = conn.prepareStatement("SELECT username, permission_id As permission, "
                    + "adult, location, created, token, description, last_login FROM user WHERE token = ?");
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                user.setUsername(rs.getString("username"));
                user.setPermission(rs.getInt("permission"));
                user.setAdult(rs.getBoolean("adult"));
                user.setLocation(rs.getString("location"));
                user.setCreated(rs.getString("created"));
                user.setToken(rs.getString("token"));
                user.setDescription(rs.getString("description"));
                user.setLastLogin(rs.getString("last_login"));
            } else {
                throw new IllegalArgumentException("Not logged in.");
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
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user ORDER BY username LIMIT ?,?");
            ps.setInt(1, start);
            ps.setInt(2, end);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPermission(rs.getInt("permission_id"));
                user.setAdult(rs.getBoolean("adult"));
                user.setLocation(rs.getString("location"));
                user.setCreated(rs.getString("created"));
                user.setDescription(rs.getString("description"));
                user.setLastLogin(rs.getString("last_login")+"000");
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
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE username = ?");
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
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user ORDER BY username");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPermission(rs.getInt("permission_id"));
                user.setAdult(rs.getBoolean("adult"));
                user.setLocation(rs.getString("location"));
                user.setCreated(rs.getString("created"));
                user.setDescription(rs.getString("description"));
                user.setLastLogin(rs.getString("last_login")+"000");
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
}