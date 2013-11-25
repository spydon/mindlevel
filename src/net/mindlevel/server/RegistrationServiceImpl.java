package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.mindlevel.client.services.RegistrationService;
import net.mindlevel.shared.FieldVerifier;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class RegistrationServiceImpl extends DBConnector implements RegistrationService {

    @Override
    public String register(String username, String password, boolean adult) throws IllegalArgumentException {
        int result = 0;
        username.toLowerCase();
        if (!FieldVerifier.isValidName(username)) {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "Name must be at least 4 characters long, but not longer than 20.");
        }
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO user (username, password, adult, token, last_login) "
                    + "values (?, SHA2(CONCAT(SHA2(?, 512),SHA2(?, 512)),512), ?, UUID(), UNIX_TIMESTAMP())");
            ps.setString(1, username);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setBoolean(4, adult);
            result = ps.executeUpdate();
            ps.close();
            conn.close();
        } catch(SQLException e) {
//            e.printStackTrace();
            throw new IllegalArgumentException(
                    "Username already taken.");
        }
        return result + " row changed";
    }
}