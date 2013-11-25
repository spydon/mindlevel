package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import net.mindlevel.client.services.PictureService;
import net.mindlevel.shared.MetaImage;

@SuppressWarnings("serial")
public class PictureServiceImpl extends DBConnector implements PictureService {

    @Override
    public MetaImage get(int id, boolean relative, boolean validated) throws IllegalArgumentException {
        MetaImage image = new MetaImage();
        try {
            int imageCount = getImageCount(validated);
            image = setMetaImageProps(id, relative, imageCount, validated);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return image;
    }

    private int getImageCount(boolean validated) throws IllegalArgumentException, SQLException {
        int imageCount = Integer.MAX_VALUE;
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM picture WHERE validated = ?");
        ps.setBoolean(1, validated);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            imageCount = rs.getInt(1);
        rs.close();
        ps.close();
        conn.close();
        return imageCount;
    }

    private MetaImage setMetaImageProps(int id, boolean relative, int imageCount, boolean validated) throws IllegalArgumentException, SQLException {
        if(imageCount==0)
            return new MetaImage();
        Connection conn = getConnection();
        MetaImage image = new MetaImage();
        PreparedStatement ps;
        if(id==-1) {
            id = new Random().nextInt(imageCount)+1;
        } else if(id==0) {
            id = imageCount;
            relative = true;
        }
        if(relative) {
            ps = conn.prepareStatement("SELECT filename, title, picture.location, mission_id, owner_id, "
                    + "picture.description user.username As owner, adult, ? AS relative_id FROM picture "
                    + "INNER JOIN user ON owner_id = user.id ORDER BY created LIMIT ?,1");
            ps.setInt(1, id);
            ps.setInt(2, id-1);
        } else {
            ps = conn.prepareStatement("SELECT *, (SELECT COUNT(*) FROM picture "
                    + "WHERE ID <= ?) AS relative_id FROM picture WHERE id=?");
            ps.setInt(1, id);
            ps.setInt(2, id);
        }
        ResultSet rs = ps.executeQuery();
        if(rs.first()) {
            int realId = rs.getInt("id");
            image = new MetaImage(rs.getString("filename"), rs.getString("title"),
                                  rs.getString("location"), rs.getInt("mission_id"),
                                  rs.getInt("owner_id"), rs.getString("description"),
                                  getTags(realId, validated),
                                  rs.getString("owner"), rs.getBoolean("adult"));
            image.setScore(rs.getInt("score"));
            image.setId(realId);
            String created = rs.getString("created");
            image.setDate(created.replace(".0", ""));
            //image.setDate(created.replaceAll("\\.[0-9]", ""));
            image.setRelativeId(rs.getInt("relative_id"));
        } else {
            throw new IllegalArgumentException("No such picture...");
        }
        image.setImageCount(imageCount);
        rs.close();
        ps.close();
        conn.close();
        return image;
    }

    private ArrayList<String> getTags(int id, boolean validated) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_picture WHERE picture_id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        ArrayList<String> tags = new ArrayList<String>();
        while(rs.next()) {
            String UserID = rs.getString("UserID");
            tags.add(UserID);
        }
        rs.close();
        ps.close();
        conn.close();
        return tags;
    }

    @Override
    public void deleteTags(int pictureId, boolean validated, String token) throws IllegalArgumentException{
        if(new TokenServiceImpl().validateAdminToken(token)) {
            Connection conn = getConnection();
            PreparedStatement ps;
            try {
                ps = conn.prepareStatement("DELETE FROM user_picture WHERE picture_id=?");
                ps.setInt(1, pictureId);
                ps.executeUpdate();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                throw new IllegalArgumentException("Could not delete tags. " + e.getStackTrace());
            }
        } else {
            throw new IllegalArgumentException("YOU don't seem to be admin. This was logged.");
        }
    }

    @Override
    public void deletePicture(int pictureId, boolean validated, String token) throws IllegalArgumentException{
        if(new TokenServiceImpl().validateAdminToken(token)) {
            Connection conn = getConnection();
            PreparedStatement ps;
            try {
                ps = conn.prepareStatement("DELETE FROM picture WHERE id=?");
                ps.setInt(1, pictureId);
                ps.executeUpdate();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                throw new IllegalArgumentException("Could not delete picture. " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("YOU don't seem to be admin. This was logged.");
        }
    }
}