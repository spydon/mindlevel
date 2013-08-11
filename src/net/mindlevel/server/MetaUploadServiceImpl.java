package net.mindlevel.server;

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

	public String upload(MetaImage metaImage, boolean validated) throws IllegalArgumentException{
		String table = validated ? "picture" : "picture_suggestion";
		if (!FieldVerifier.isValidMetaImage(metaImage))
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"I'm afraid the fluff you entered was not correct.");
		if(!new TokenServiceImpl().validateAdminToken(metaImage.getToken()) || !new TokenServiceImpl().validateAuth(metaImage.getOwner(), metaImage.getToken())) {
			throw new IllegalArgumentException(
					"Something went wrong with the authentication.");
		}
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + table + " (filename, title, location, owner, description, mission_id, adult) values(?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, metaImage.getFilename());
			ps.setString(2, metaImage.getTitle());
			ps.setString(3, metaImage.getLocation());
			ps.setString(4, metaImage.getOwner());
			ps.setString(5, metaImage.getDescription());
			ps.setInt(6, metaImage.getMissionId());
			ps.setBoolean(7, metaImage.isAdult());
			ps.executeUpdate();
			uploadTags(metaImage.getOwner(), metaImage.getTags(),
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
		String table = validated ? "user_picture" : "user_picture_suggestion";
		UserServiceImpl userService = new UserServiceImpl();
		for (String tag : tags) {
			if(userService.userExists(tag.toLowerCase())) {
				Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO " + table + " (picture_id, user_id) values(?, ?)");
				ps.setInt(1, pictureId);
				ps.setString(2, tag);
				ps.executeUpdate();
				ps.close();
				conn.close();
			}
		}
	}

	private int getPictureID(String filename, boolean validated) throws SQLException {
		int id = 0;
		String table = validated ? "picture" : "picture_suggestion";
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT id FROM " + table + " WHERE filename=?");
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