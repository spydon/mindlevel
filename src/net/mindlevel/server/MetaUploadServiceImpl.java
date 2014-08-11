package net.mindlevel.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import net.mindlevel.client.services.MetaUploadService;
import net.mindlevel.shared.FieldVerifier;
import net.mindlevel.shared.MetaImage;

import com.mysql.jdbc.Statement;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * This is an example of how to use UploadAction class.
 */
public class MetaUploadServiceImpl extends DBConnector implements
        MetaUploadService {

    private static final long serialVersionUID = 1L;

    @Override
    public String upload(MetaImage metaImage, boolean validated) throws IllegalArgumentException {
        if (!FieldVerifier.isValidMetaImage(metaImage).equals(""))
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "I'm afraid the fluff you entered was not correct.");
        if(!new TokenServiceImpl().validateAdminToken(metaImage.getToken()) && !new TokenServiceImpl().validateAuth(metaImage.getOwner(), metaImage.getToken())) {
            throw new IllegalArgumentException(
                    "Something went wrong with the authentication.");
        }
        try {
            int threadId = getThreadId();
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO picture "
                    + "(filename, title, location, description, adult, owner, mission_id, thread_id) "
                    + "values(?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, metaImage.getFilename());
            ps.setString(2, metaImage.getTitle());
            ps.setString(3, metaImage.getLocation());
            ps.setString(4, metaImage.getDescription());
            ps.setBoolean(5, metaImage.isAdult());
            ps.setString(6, metaImage.getOwner());
            ps.setInt(7, metaImage.getMission().getId());
            ps.setInt(8, threadId);

            ps.executeUpdate();
            uploadTags(metaImage.getOwner(), metaImage.getTags(),
                    getPictureID(metaImage.getFilename(), false), validated);
            ps.close();
            conn.close();
        } catch(MySQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("You already uploaded this picture");
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return "Success";
    }

    private int getThreadId() {
        int threadId = 0;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO comment_thread () values ()", Statement.RETURN_GENERATED_KEYS);

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.first();
            threadId = keys.getInt(1);

            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return threadId;
    }

    private void uploadTags(String owner, HashSet<String> tags, int pictureId, boolean validated)
            throws SQLException {
        UserServiceImpl userService = new UserServiceImpl();
        for (String username : tags) {
            if(userService.userExists(username.toLowerCase())) {
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO user_picture "
                        + "(picture_id, username) VALUES (?, ?)");
                ps.setInt(1, pictureId);
                ps.setString(2, username);
                ps.executeUpdate();
                ps.close();
                conn.close();
            }
        }
    }

    private int getPictureID(String filename, boolean validated) throws SQLException {
        int id = 0;
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM picture WHERE filename=?");
        ps.setString(1, filename);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            id = rs.getInt("id");
        }
        rs.close();
        ps.close();
        conn.close();
        return id;
    }
}