package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.mindlevel.client.services.OracleService;
import net.mindlevel.shared.Mission;

@SuppressWarnings("serial")
public class OracleServiceImpl extends DBConnector implements OracleService {

	@Override
	public ArrayList<String> getUsers() throws IllegalArgumentException {
		ArrayList<String> users = new ArrayList<String>();
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT username FROM user WHERE admin = 0");
		    ResultSet rs = ps.executeQuery();
		    while(rs.next())
		    	users.add(rs.getString("username"));
		    rs.close();
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	@Override
	public ArrayList<Mission> getMissions() throws IllegalArgumentException {
		ArrayList<Mission> missions = new ArrayList<Mission>();
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM mission");
		    ResultSet rs = ps.executeQuery();
		    while(rs.next()) {
		    	Mission mission = new Mission();
		    	mission.setId(rs.getInt("id"));
		    	mission.setName(rs.getString("name"));
		    	mission.setCategory(rs.getString("category"));
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
}