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
    public User getUser(String userId) throws IllegalArgumentException {
        User user = new User();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE username = ?");
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                user.setUsername(rs.getString("username"));
                user.setPermission(rs.getInt("permission"));
                user.setAdult(rs.getBoolean("adult"));
                user.setLocation(rs.getString("location"));
                user.setCreated(rs.getString("created"));
                user.setDescription(rs.getString("description"));
                user.setLastLogin(rs.getString("last_login"));
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
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user ORDER BY username LIMIT ?,?");
            ps.setInt(1, start);
            ps.setInt(2, end);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPermission(rs.getInt("permission"));
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
}