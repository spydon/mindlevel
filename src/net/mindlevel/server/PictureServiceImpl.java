package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import net.mindlevel.client.services.PictureService;
import net.mindlevel.shared.Category;
import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.MetaImage;
import net.mindlevel.shared.Mission;

@SuppressWarnings("serial")
public class PictureServiceImpl extends DBConnector implements PictureService {

    @Override
    public MetaImage get(int id, boolean relative, boolean adult, boolean validated) throws IllegalArgumentException {
        MetaImage image = new MetaImage();
        try {
            int imageCount = getImageCount(validated, adult);
            image = setMetaImageProps(id, relative, imageCount, adult, validated);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public ArrayList<MetaImage> getPictures(int start, int offset, Constraint constraint) throws IllegalArgumentException {
        Connection conn = getConnection();
        ArrayList<MetaImage> pictures = new ArrayList<MetaImage>();

        if(!constraint.isValidated()) {
            if(!new TokenServiceImpl().validateAdminToken(constraint.getToken())) {
                throw new IllegalArgumentException("You are not authorized to do that...");
            }
        }
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT p.id, filename, title, location, mission_id, owner, "
                    + "description, adult, score, thread_id, timestamp FROM picture p "
                    + "INNER JOIN (SELECT distinct picture_id FROM user_picture WHERE username LIKE ?) tag "
                    + "ON p.id = tag.picture_id "
                    + "INNER JOIN (SELECT distinct m.id FROM mission m "
                    + "INNER JOIN mission_category mc ON m.id = mc.mission_id "
                    + "INNER JOIN category c ON mc.category_id = c.id WHERE c.name LIKE ?) mission "
                    + "ON p.mission_id = mission.id "
                    + "WHERE validated = ? AND "
                    + "adult LIKE ? AND "
                    + "title LIKE ? "
                    + "ORDER BY timestamp desc LIMIT ?,?");
            ps.setString(1, "%" + constraint.getUsername() + "%");
            ps.setString(2, "%" + (constraint.getCategory() == Category.ALL ? "" : constraint.getCategory().toString().toLowerCase()) + "%");
            ps.setBoolean(3, constraint.isValidated());
            ps.setString(4, constraint.isAdult() ? "%" : "0");
            ps.setString(5, "%" + constraint.getPictureTitle() + "%");
            ps.setInt(6, start);
            ps.setInt(7, offset);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                MetaImage picture = new MetaImage();
                int id = rs.getInt("id");
                picture = new MetaImage(rs.getString("filename"), rs.getString("title"),
                        rs.getString("location"), getMission(rs.getInt("mission_id")),
                        rs.getString("owner"), rs.getString("description"),
                        getTags(id),
                        rs.getBoolean("adult"));
                picture.setCategories(getCategories(rs.getInt("mission_id")));
                picture.setScore(rs.getInt("score"));
                picture.setThreadId(rs.getInt("thread_id"));
                picture.setId(id);
                String created = rs.getString("timestamp");
                picture.setDate(created.replace(".0", ""));
                pictures.add(picture);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pictures;
    }

    private int getImageCount(boolean validated, boolean adult) throws IllegalArgumentException, SQLException {
        int imageCount = Integer.MAX_VALUE;
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM picture WHERE validated = ? AND (adult = ? OR adult = ?)");
        ps.setBoolean(1, validated);
        ps.setBoolean(2, false);
        ps.setBoolean(3, adult);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            imageCount = rs.getInt(1);
        rs.close();
        ps.close();
        conn.close();
        return imageCount;
    }

    private MetaImage setMetaImageProps(int id, boolean relative, int imageCount, boolean adult, boolean validated)
            throws IllegalArgumentException, SQLException {
        if(imageCount==0) {
            return new MetaImage();
        }
        adult = true; //Flip if adult pictures shouldn't be shown even if you know the full link
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
            ps = conn.prepareStatement("SELECT id, filename, title, location, mission_id, owner, "
                    + "description, adult, ? AS relative_id, score, thread_id, timestamp FROM picture "
                    + "WHERE validated = ? AND (adult = ? OR adult = ?)"
                    + "ORDER BY timestamp LIMIT ?,1");
            ps.setInt(1, id);
            ps.setBoolean(2, validated);
            ps.setBoolean(3, false);
            ps.setBoolean(4, adult);
            ps.setInt(5, id-1);
        } else {
            ps = conn.prepareStatement("SELECT id, filename, title, location, mission_id, owner, "
                    + "description, adult, (SELECT COUNT(id) FROM picture "
                    + "WHERE id <= ? AND validated = ? AND (adult = ? OR adult = ?)) AS relative_id, "
                    + "score, thread_id, timestamp FROM picture "
                    + "WHERE id = ? AND validated = ? AND (adult = ? OR adult = ?)");
            ps.setInt(1, id);
            ps.setBoolean(2, validated);
            ps.setBoolean(3, false);
            ps.setBoolean(4, adult);
            ps.setInt(5, id);
            ps.setBoolean(6, validated);
            ps.setBoolean(7, false);
            ps.setBoolean(8, adult);
        }
        ResultSet rs = ps.executeQuery();
        if(rs.first()) {
            int realId = rs.getInt("id");
            image = new MetaImage(rs.getString("filename"), rs.getString("title"),
                                  rs.getString("location"), getMission(rs.getInt("mission_id")),
                                  rs.getString("owner"), rs.getString("description"),
                                  getTags(realId),
                                  rs.getBoolean("adult"));
            image.setScore(rs.getInt("score"));
            image.setThreadId(rs.getInt("thread_id"));
            image.setId(realId);
            String created = rs.getString("timestamp");
            image.setDate(created.replace(".0", ""));
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

    @Override
    public void validate(int pictureId, String token) throws IllegalArgumentException {
        if(new TokenServiceImpl().validateAdminToken(token)) {
            Connection conn = getConnection();
            PreparedStatement ps;
            try {
                ps = conn.prepareStatement("UPDATE picture SET validated=1 WHERE id=?");
                ps.setInt(1, pictureId);
                ps.executeUpdate();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                throw new IllegalArgumentException("Could not validate picture. " + e.getStackTrace());
            }
        } else {
            throw new IllegalArgumentException("YOU don't seem to be admin. This was logged.");
        }
    }

    private ArrayList<String> getTags(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT username FROM user_picture WHERE picture_id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        ArrayList<String> tags = new ArrayList<String>();
        while(rs.next()) {
            String username = rs.getString("username");
            tags.add(username);
        }
        rs.close();
        ps.close();
        conn.close();
        return tags;
    }

    private Mission getMission(int missionId) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT id, name FROM mission WHERE id = ?");

        ps.setInt(1, missionId);
        ResultSet rs = ps.executeQuery();
        Mission mission = new Mission();
        if(rs.next()) {
            mission.setId(rs.getInt("id"));
            mission.setName(rs.getString("name"));
        }
        rs.close();
        ps.close();
        conn.close();
        return mission;
    }

    private ArrayList<Category> getCategories(int missionId) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT c.name FROM mission m "
                        + "INNER JOIN mission_category mc ON m.id = mc.mission_id "
                        + "INNER JOIN category c ON mc.category_id = c.id WHERE m.id = ?");

        ps.setInt(1, missionId);
        ResultSet rs = ps.executeQuery();
        ArrayList<Category> categories = new ArrayList<Category>();
        while(rs.next()) {
            categories.add(Category.valueOf(rs.getString("name").toUpperCase()));
        }
        rs.close();
        ps.close();
        conn.close();
        return categories;
    }

    @Override
    public void deleteTags(int pictureId, String token) throws IllegalArgumentException {
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
                e.printStackTrace();
                throw new IllegalArgumentException("Could not delete tags.");
            }
        } else {
            throw new IllegalArgumentException("YOU don't seem to be admin. This was logged.");
        }
    }

    @Override
    public void delete(int pictureId, String token) throws IllegalArgumentException {
        if(new TokenServiceImpl().validateAdminToken(token)) {
            Connection conn = getConnection();
            PreparedStatement ps;
            try {
                new RatingServiceImpl().deleteRatings(pictureId, token);
                deleteTags(pictureId, token);
                ps = conn.prepareStatement("DELETE FROM picture WHERE id=?");
                ps.setInt(1, pictureId);
                ps.executeUpdate();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Could not delete picture.");
            }
        } else {
            throw new IllegalArgumentException("YOU don't seem to be admin. This was logged.");
        }
    }
}