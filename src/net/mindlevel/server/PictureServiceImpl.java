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
		String table = validated ? "picture" : "picture_suggestion";
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM " + table);
		ResultSet rs = ps.executeQuery();
		if(rs.next())
			imageCount = rs.getInt(1);
		rs.close();
		ps.close();
		conn.close();
		return imageCount;
	}
	
	private MetaImage setMetaImageProps(int id, boolean relative, int imageCount, boolean validated) throws IllegalArgumentException, SQLException {
		Connection conn = getConnection();
		MetaImage image = new MetaImage();
		String table = validated ? "picture" : "picture_suggestion";
		PreparedStatement ps;
		if(id==-1) {
			id = new Random().nextInt(imageCount)+1;
		} else if(id==0) {
			id = imageCount;
			relative = true;
		}
		if(relative) {
			ps = conn.prepareStatement("SELECT *, ? AS relative_id FROM " + table + " ORDER BY created LIMIT ?,1");
			ps.setInt(1, id);
			ps.setInt(2, id-1);
		} else {
			ps = conn.prepareStatement("SELECT *, (SELECT COUNT(*) FROM " + table + " WHERE ID <= ?) AS relative_id FROM  " + table + " WHERE id=?");
			ps.setInt(1, id);
			ps.setInt(2, id);
		}
	    ResultSet rs = ps.executeQuery();
	    if(rs.first()) {
	    	int realId = rs.getInt("id");
	    	image = new MetaImage(rs.getString("filename"), rs.getString("title"),
	    						  rs.getString("location"), rs.getInt("mission_id"),
	    						  rs.getString("description"), getTags(realId, validated),
	    						  rs.getString("owner"), rs.getBoolean("adult"));
	    	image.setScore(rs.getInt("score"));
	    	image.setId(realId);
	    	image.setDate(rs.getString("created"));
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
		String table = validated ? "user_picture" : "user_picture_suggestion";
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE picture_id=?");
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
	
	public void deleteTags(int pictureId, boolean validated, String token) throws IllegalArgumentException{
		if(new TokenServiceImpl().validateAdminToken(token)) {
			Connection conn = getConnection();
			String table = validated ? "user_picture" : "user_picture_suggestion";
			PreparedStatement ps;
			try {
				ps = conn.prepareStatement("DELETE FROM " + table + " WHERE picture_id=?");
				ps.setInt(1, pictureId);
				ps.executeUpdate();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				throw new IllegalArgumentException("Could not delete tags. " + e.getMessage());
			}
		} else {
			throw new IllegalArgumentException("YOU don't seem to be admin. This was logged.");
		}
	}
	
	public void deletePicture(int pictureId, boolean validated, String token) throws IllegalArgumentException{
		if(new TokenServiceImpl().validateAdminToken(token)) {
			Connection conn = getConnection();
			String table = validated ? "picture" : "picture_suggestion";
			PreparedStatement ps;
			try {
				ps = conn.prepareStatement("DELETE FROM " + table + " WHERE picture_id=?");
				ps.setInt(1, pictureId);
				ps.executeUpdate();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				throw new IllegalArgumentException("Could not delete tags.");
			}
		} else {
			throw new IllegalArgumentException("YOU don't seem to be admin. This was logged.");
		}
	}
}