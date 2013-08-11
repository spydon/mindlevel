package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.mindlevel.client.services.MissionService;
import net.mindlevel.shared.Mission;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class MissionServiceImpl extends DBConnector implements MissionService {

	private ArrayList<Mission> missions = new ArrayList<Mission>();
	@Override
	public List<Mission> getMissions(int start, int end, boolean validated) throws IllegalArgumentException {
		try {
			Connection conn = getConnection();
			missions.clear();
			String table = validated ? "mission" : "mission_suggestion";
			PreparedStatement ps = 
					conn.prepareStatement("SELECT id, "
										+ "name, "
										+ "category, "
										+ "adult, "
										+ "creator, "
										+ "timestamp "
										+ "FROM " + table + " "
										+ "ORDER BY name "
										+ "LIMIT ?,?");
		    ps.setInt(1, start);
		    ps.setInt(2, end);
		    ResultSet rs = ps.executeQuery();
		    while(rs.next()) {
		    	Mission mission = new Mission();
		    	mission.setName(rs.getString("name"));
		    	mission.setCategory(rs.getString("category"));
		    	mission.setId(rs.getInt("id"));
		    	mission.setAdult(rs.getBoolean("adult"));
		    	mission.setCreator(rs.getString("creator"));
		    	mission.setTimestamp(rs.getString("timestamp"));
		    	missions.add(mission);
		    }
		    rs.close();
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return missions;
	}
	
	public int getMissionCount(boolean validated) throws IllegalArgumentException {
		int count = 0;
		try {
			Connection conn = getConnection();
			String table = validated ? "mission" : "mission_suggestion";
			PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS count FROM " + table);
		    ResultSet rs = ps.executeQuery();
		    if(rs.first()) {
		    	count = rs.getInt("count");
		    }
		    rs.close();
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public Mission getMission(int id, boolean validated) throws IllegalArgumentException {
		Mission mission = new Mission();
		try {
			Connection conn = getConnection();
			String table = validated ? "mission" : "mission_suggestion";
			PreparedStatement ps = 
					conn.prepareStatement("SELECT Id, "
										+ "Name, "
										+ "Category, "
										+ "Description, "
										+ "Adult, "
										+ "Creator, "
										+ "Timestamp "
										+ "FROM " + table + " "
										+ "WHERE Id = ?");
			ps.setInt(1, id);
		    ResultSet rs = ps.executeQuery();
		    if(rs.first()) {
		    	mission.setName(rs.getString("name"));
		    	mission.setCategory(rs.getString("category"));
		    	mission.setId(rs.getInt("id"));
		    	mission.setDescription(rs.getString("description"));
		    	mission.setAdult(rs.getBoolean("adult"));
		    	mission.setCreator(rs.getString("creator"));
		    	mission.setTimestamp(rs.getString("timestamp"));
		    }
		    rs.close();
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return mission;
	}

	@Override
	public void suggestMission(Mission mission) throws IllegalArgumentException {
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO mission_suggestion " +
														 "(name, category, description, adult, creator) " +
														 "values " +
														 "(?, ?, ?, ?, ?)");
		    ps.setString(1, mission.getName());
		    ps.setString(2, mission.getCategory());
		    ps.setString(3, mission.getDescription());
		    ps.setBoolean(4, mission.getAdult());
		    ps.setString(5, mission.getCreator());
		    
		    int result = ps.executeUpdate();
		    ps.close();
		    if (result != 1)
		    	throw new IllegalArgumentException("Unknown error.");
		    conn.close();
		} catch(SQLException | IllegalArgumentException e) {
//			e.printStackTrace();
			throw new IllegalArgumentException(
					"Something went wrong.");
		}
	}
	
	@Override
	public void uploadMission(Mission mission, String token) throws IllegalArgumentException {
		try {
			if(new TokenServiceImpl().validateAdminToken(token)) {
				Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM mission WHERE " +
						 "name=? AND category=? AND description=? AND creator=?");
			    ps.setString(1, mission.getName());
			    ps.setString(2, mission.getCategory());
			    ps.setString(3, mission.getDescription());
			    ps.setString(5, mission.getCreator());
			    ResultSet rs = ps.executeQuery();
			    if(!rs.first()) {
					PreparedStatement ps2 = conn.prepareStatement("INSERT INTO mission " +
																 "(name, category, description, adult, creator) " +
																 "values " +
																 "(?, ?, ?, ?, ?)");
				    ps2.setString(1, mission.getName());
				    ps2.setString(2, mission.getCategory());
				    ps2.setString(3, mission.getDescription());
				    ps2.setBoolean(4, mission.getAdult());
				    ps2.setString(5, mission.getCreator());
				    
				    int result = ps.executeUpdate();
				    ps.close();
				    PreparedStatement ps3 = conn.prepareStatement("DELETE FROM mission_suggestion WHERE id = ?");
				    ps3.setInt(1, mission.getId());
				    int result2 = ps3.executeUpdate();
				    if (result != 1 && result2 != 1)
				    	throw new IllegalArgumentException("Unknown error.");
			    } else {
			    	throw new IllegalArgumentException("The mission already exists.");
			    }
			    conn.close();
			} else {
				throw new IllegalArgumentException(
						"You are not admin, you sneaky bastard! ;)");
			}
		} catch(SQLException e) {
			throw new IllegalArgumentException(
					"Something went wrong.");
		}
	}

	@Override
	public ArrayList<String> getCategories() {
		ArrayList<String> categories = new ArrayList<String>();
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT name FROM category");
		    ResultSet rs = ps.executeQuery();
		    while(rs.next()) {
		    	categories.add(rs.getString("name"));
		    }
		    rs.close();
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return categories;
	}
}