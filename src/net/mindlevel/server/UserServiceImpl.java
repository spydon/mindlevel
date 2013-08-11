package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.mindlevel.client.services.UserService;
import net.mindlevel.shared.User;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class UserServiceImpl extends DBConnector implements UserService {

	@Override
	public User getUser(String userId) throws IllegalArgumentException {
		User user = new User();
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE username = ?");
		    ps.setString(1, userId);
		    ResultSet rs = ps.executeQuery();
		    if(rs.next()) {
		    	user.setUsername(rs.getString("username"));
		    	user.setAdmin(rs.getBoolean("admin"));
		    	user.setAdult(rs.getBoolean("adult"));
		    	user.setLocation(rs.getString("location"));
		    	user.setCreated(rs.getString("created"));
		    	user.setDescription(rs.getString("description"));
		    	user.setLastLogin(rs.getString("last_login"));
		    } else {
		    	throw new IllegalArgumentException("No such user.");
		    }
		    rs.close();
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public boolean userExists(String username) {
		boolean exists = false;
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE username = ?");
		    ps.setString(1, username);
		    ResultSet rs = ps.executeQuery();
		    if(rs.first()) {
		    	exists = true;
		    }
		    rs.close();
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return exists;		
	}
}