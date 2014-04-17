package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.mindlevel.client.services.CaptchaService;
import net.mindlevel.shared.Captcha;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class CaptchaServiceImpl extends DBConnector implements CaptchaService {

    @Override
    public Captcha get() {
        Captcha captcha = new Captcha();
        int id = 0;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT id, question FROM captcha ORDER BY RAND()");
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                id = rs.getInt("id");
                captcha.setQuestion(rs.getString("question"));
            }
            ps.close();
            conn.close();
            captcha.setToken(generateToken(id));
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return captcha;
    }

    protected String generateToken(int id) throws IllegalArgumentException {
        String token = "";
        try {
            token = generateUUID();
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO delivered_captcha (id, token) VALUES (?,?)");
            ps.setInt(1, id);
            ps.setString(2, token);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return token;
    }

    private String generateUUID() {
        String uuid = "";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT UUID() as token");
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                uuid = rs.getString("token");
            }
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return uuid;
    }

    @Override
    public boolean verify(String answer, String token) {
        Boolean correct = true;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE d.* FROM delivered_captcha d "
                    + "INNER JOIN captcha c on c.id = d.id "
                    + "WHERE c.answer = ? AND d.token = ?");
            ps.setString(1, answer);
            ps.setString(2, token);
            int result = ps.executeUpdate();
            if(result == 0) {
                correct = false;
                return correct;
            }
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return correct;
    }
}