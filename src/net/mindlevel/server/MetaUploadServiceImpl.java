package net.mindlevel.server;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.mindlevel.client.services.MetaUploadService;
import net.mindlevel.shared.FieldVerifier;
import net.mindlevel.shared.MetaImage;

/**
 * This is an example of how to use UploadAction class.
 */
public class MetaUploadServiceImpl extends DBConnector implements
        MetaUploadService {

    private static final long serialVersionUID = 1L;

    @Override
    public String upload(MetaImage metaImage, boolean validated) throws IllegalArgumentException{
        if (!FieldVerifier.isValidMetaImage(metaImage))
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "I'm afraid the fluff you entered was not correct.");
        if(!new TokenServiceImpl().validateAdminToken(metaImage.getToken()) && !new TokenServiceImpl().validateAuth(metaImage.getOwner(), metaImage.getToken())) {
            throw new IllegalArgumentException(
                    "Something went wrong with the authentication.");
        }
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO picture "
                    + "(filename, title, location, description, adult, owner, mission_id) "
                    + "values(?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, escapeHtml4(metaImage.getFilename()));
            ps.setString(2, escapeHtml4(metaImage.getTitle()));
            ps.setString(3, escapeHtml4(metaImage.getLocation()));
            ps.setString(4, escapeHtml4(metaImage.getDescription()));
            ps.setBoolean(5, metaImage.isAdult());
            ps.setString(6, escapeHtml4(metaImage.getOwner()));
            ps.setInt(7, metaImage.getMissionId());

            ps.executeUpdate();
            uploadTags(escapeHtml4(metaImage.getOwner()), metaImage.getTags(),
                    getPictureID(metaImage.getFilename(), false), validated);
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return "Success";
    }

    private void uploadTags(String owner, ArrayList<String> tags, int pictureId, boolean validated)
            throws SQLException {
        if (!tags.contains(owner))
            tags.add(owner);
        UserServiceImpl userService = new UserServiceImpl();
        for (String username : tags) {
            if(userService.userExists(username.toLowerCase())) {
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO user_picture "
                        + "(picture_id, username) VALUES (?, ?)");
                ps.setInt(1, pictureId);
                ps.setString(2, escapeHtml4(username));
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