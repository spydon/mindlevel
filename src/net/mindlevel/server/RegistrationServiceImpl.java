package net.mindlevel.server;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.mindlevel.client.services.RegistrationService;
import net.mindlevel.shared.FieldVerifier;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class RegistrationServiceImpl extends DBConnector implements RegistrationService {

    @Override
    public void register(String username, String email, String password, boolean adult) throws IllegalArgumentException {
        username.toLowerCase();
        if (!FieldVerifier.isValidName(username) || !FieldVerifier.isValidEmail(email)) {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "Name must be at least 4 characters long, but not longer than 20.");
        }
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO user (username, email, password, adult, token, last_login) "
                    + "values (?, ?, SHA2(CONCAT(SHA2(?, 512),SHA2(?, 512)),512), ?, UUID(), UNIX_TIMESTAMP())");
            ps.setString(1, escapeHtml4(username));
            ps.setString(2, escapeHtml4(email));
            ps.setString(3, escapeHtml4(username));
            ps.setString(4, password);
            ps.setBoolean(5, adult);
            int result = ps.executeUpdate();
            if(result != 1) {
                throw new IllegalArgumentException(
                        "Username already taken.");
            };
            ps.close();
            conn.close();
        } catch(SQLException e) {
            throw new IllegalArgumentException(
                    "Username already taken.");
        }
    }
}