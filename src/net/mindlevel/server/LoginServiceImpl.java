package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.mindlevel.client.services.LoginService;
import net.mindlevel.shared.Ban;
import net.mindlevel.shared.FieldVerifier;
import net.mindlevel.shared.User;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class LoginServiceImpl extends DBConnector implements LoginService {

    @Override
    public User login(String username, String password) throws IllegalArgumentException {
        User user = new User();
        username = username.toLowerCase();
        if (!FieldVerifier.isValidName(username)) {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "The username must be at least 4 characters long, but not longer than 20.");
        }
        Ban ban = getBan(username);
        if (ban.isBanned()) {
            // If the user is banned, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "You are banned until " + ServerTools.shortenDate(ban.getExpiry()) + " because of \"" + ban.getReason() + "\"");
        }
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username, adult, created, activated, permission_id as permission, last_login, picture "
                    + "FROM user "
                    + "WHERE username = ? AND password = SHA2(CONCAT(SHA2(?, 512),SHA2(?, 512)),512)");
            ps.setString(1, username);
            ps.setString(2, username);
            ps.setString(3, password);
            ResultSet rs = ps.executeQuery();
            if(rs.first()) {
                if(!rs.getBoolean("activated")) {
                    throw new IllegalArgumentException(
                            "This account haven't been activated yet, check your e-mail");
                }
                String token = new TokenServiceImpl().generateToken(username);
                user.setToken(token);
                user.setUsername(rs.getString("username"));
                user.setAdult(rs.getBoolean("adult"));
                user.setCreated(rs.getTimestamp("created"));
                user.setPermission(rs.getInt("permission"));
                user.setLastLogin(rs.getTimestamp("last_login"));
                user.setPicture(rs.getString("picture"));

                PreparedStatement ps2 = conn.prepareStatement("UPDATE user SET last_login = DEFAULT WHERE username = ?");
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

    private Ban getBan(String username) {
        Ban ban = null;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username, reason, expires "
                    + "FROM ban "
                    + "WHERE username = ? ORDER BY expires desc LIMIT 0,1");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.first()) {
                ban = new Ban(username, rs.getString("reason"), rs.getTimestamp("expires"));
            } else {
                ban = new Ban(username, "Not ever banned", new Date(0));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return ban;
    }
}