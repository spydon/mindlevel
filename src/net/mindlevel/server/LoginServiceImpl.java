package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.mindlevel.client.services.LoginService;
import net.mindlevel.shared.FieldVerifier;
import net.mindlevel.shared.User;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class LoginServiceImpl extends DBConnector implements LoginService {

    @Override
    public User login(String username, String password) throws IllegalArgumentException {
        User user = new User();
        if (!FieldVerifier.isValidName(username)) {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "The username must be at least 4 characters long, but not longer than 20.");
        }
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * "
                    + "FROM user "
                    + "WHERE username = ? AND password = SHA2(CONCAT(SHA2(?, 512),SHA2(?, 512)),512)");
            ps.setString(1, username);
            ps.setString(2, username);
            ps.setString(3, password);
            ResultSet rs = ps.executeQuery();
            if(rs.first()) {
                String token = new TokenServiceImpl().generateToken(username);
                user.setToken(token);
                user.setUserId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setAdult(rs.getBoolean("adult"));
                user.setCreated(rs.getString("created"));
                user.setPermission(rs.getInt("permission"));
                //How much does it actually have to be filled out in the user object at this stage...
                PreparedStatement ps2 = conn.prepareStatement("UPDATE user SET last_login=UNIX_TIMESTAMP() WHERE username = ?");
                ps2.setString(1, username);
                ps2.executeUpdate();
                ps2.close();
            } else {
                throw new IllegalArgumentException("Wrong username/password!");
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}