package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.mindlevel.client.services.TokenService;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class TokenServiceImpl extends DBConnector implements TokenService {

    @Override
    public String getToken(String username, String password) throws IllegalArgumentException {
        String token = "";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT token FROM user WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                token = rs.getString("token");
            else
                throw new IllegalArgumentException("Wrong username/password!");
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return token;
    }

    //Do not open up for remote use. Potentially unsafe.
    public String generateToken(String username) throws IllegalArgumentException {
        String token = "";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE user SET token=UUID() WHERE username = ?");
            ps.setString(1, username);
            int result = ps.executeUpdate();
            PreparedStatement ps2 = conn.prepareStatement("SELECT token FROM user WHERE username = ?");
            ps2.setString(1, username);
            ResultSet rs = ps2.executeQuery();
            if(result == 1 && rs.first())
                token = rs.getString("token");
            else
                throw new IllegalArgumentException("Something went wrong!");
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return token;
    }

    public boolean validateAuth(String username, String token) {
        boolean isValid = false;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username FROM user WHERE username=? && token=?");
            ps.setString(1, username);
            ps.setString(2, token);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                isValid = true;
            }
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    @Deprecated //Use validateAuth instead
    @Override
    public boolean validateToken(String token) throws IllegalArgumentException {
        boolean isValid = false;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT token FROM user WHERE token=?");
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                isValid = true;
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    //Do not open up to be called from client
    public boolean validateAdminToken(String token) {
        boolean isValid = false;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT token FROM user WHERE token=? AND permission_id>0");
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                isValid = true;
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    @Override
    public void invalidateToken(String token) throws IllegalArgumentException {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE user SET token=NULL WHERE token=?");
            ps.setString(1, token);
            int result = ps.executeUpdate();
            if(result != 1)
                throw new IllegalArgumentException("You are already logged out...");
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}