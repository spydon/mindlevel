package net.mindlevel.server;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.mindlevel.client.services.RegistrationService;
import net.mindlevel.shared.FieldVerifier;
import net.mindlevel.shared.Normalizer;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class RegistrationServiceImpl extends DBConnector implements RegistrationService {

    @Override
    public void register(String username, String email, String password, boolean adult) throws IllegalArgumentException {
        username = username.toLowerCase();
        if (!FieldVerifier.isValidUsername(username)) {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "Name must be at least 4 characters long, but not longer than 20.");
        } else if(!FieldVerifier.isValidEmail(email)) {
            throw new IllegalArgumentException(
                    "That email was not formated correctly, try again.");
        }
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO user (username, email, password, adult) "
                    + "values (?, ?, SHA2(CONCAT(SHA2(?, 512),SHA2(?, 512)),512), ?)");
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setBoolean(5, adult);
            int result = ps.executeUpdate();
            if(result != 1) {
                throw new IllegalArgumentException(
                        "Username already taken.");
            } else {
                PreparedStatement ps2 = conn.prepareStatement("INSERT INTO user_activation (username, uuid) "
                        + "values (?, UUID_SHORT())");
                ps2.setString(1, username);
                ps2.executeUpdate();

                PreparedStatement ps3 = conn.prepareStatement("SELECT LAST_INSERT_ID()");
                ResultSet rs = ps3.executeQuery();
                rs.first();
                String uuid = rs.getString(1);

                sendEmail(username, email, uuid);
                ps2.close();
            }
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "Username already taken.");
        }
    }

    private void sendEmail(String username, String email, String uuid) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "Hi there " + Normalizer.capitalizeName(username) + "! Activate your account by clicking this link: "
                + "<a href='https://mindlevel.net#activate=" + uuid + "' />";

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("spydon@mindlevel.com", "Mindlevel.net"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress(email, Normalizer.capitalizeName(username)));
            msg.setSubject("Mindlevel.net activation");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        } catch (UnsupportedEncodingException e) {
            // ...
        }
    }
}