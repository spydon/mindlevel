package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.mindlevel.client.services.TokenService;
import net.mindlevel.shared.User;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class TokenServiceImpl extends DBConnector implements TokenService {

	@Override
	public String getToken(String username, String password) throws IllegalArgumentException {
		String token = "";
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT token FROM user WHERE username = ? AND password = ?");
		    ps.setString(1, username);
		    ps.setString(2, password);
		    ResultSet rs = ps.executeQuery();
		    if(rs.next())
		    	token = rs.getString("token");
		    else
		    	throw new IllegalArgumentException("Wrong username/password!");
		    rs.close();
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return token;
	}
	
	//Do not open up for remote use. Potentially unsafe.
	public String generateToken(String username) throws IllegalArgumentException {
		String token = "";
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("UPDATE user SET token=UUID() WHERE username = ?");
		    ps.setString(1, username);
		    int result = ps.executeUpdate();
			PreparedStatement ps2 = conn.prepareStatement("SELECT token FROM user WHERE username = ?");
		    ps2.setString(1, username);
		    ResultSet rs = ps2.executeQuery();
		    if(result == 1 && rs.first())
		    	token = rs.getString("token");
		    else
		    	throw new IllegalArgumentException("Something went wrong!");
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return token;
	}
	
	public boolean validateAuth(String user, String token) {
		boolean isValid = false;
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT username FROM user WHERE username=? && token=?");
		    ps.setString(1, user);
		    ps.setString(2, token);
		    ResultSet rs = ps.executeQuery();
		    if(rs.next()) {
		    	isValid = true;
		    }
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return isValid;
	}
	
	public boolean validateToken(String token) throws IllegalArgumentException {
		boolean isValid = false;
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT token FROM user WHERE token=?");
		    ps.setString(1, token);
		    ResultSet rs = ps.executeQuery();
		    if(rs.next())
		    	isValid = true;
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return isValid;
	}
	
	//Do not open up to be called from client
	public boolean validateAdminToken(String token) {
		boolean isValid = false;
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT token FROM user WHERE token=? AND admin=1");
		    ps.setString(1, token);
		    ResultSet rs = ps.executeQuery();
		    if(rs.next())
		    	isValid = true;
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return isValid;
	}
	
	public User getUser(String token) throws IllegalArgumentException {
		User user = new User();
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE token=?");
		    ps.setString(1, token);
		    ResultSet rs = ps.executeQuery();
		    if(rs.next()) {
		    	user.setUsername(rs.getString("username"));
		    	user.setAdmin(rs.getBoolean("admin"));
		    	user.setAdult(rs.getBoolean("adult"));
		    	user.setLocation(rs.getString("location"));
		    	user.setCreated(rs.getString("created"));
		    	user.setToken(rs.getString("token"));
		    	user.setDescription(rs.getString("description"));
		    	user.setLastLogin(rs.getString("last_login"));
		    } else {
		    	throw new IllegalArgumentException("Not logged in.");
		    }
		    rs.close();
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public void invalidateToken(String token) throws IllegalArgumentException {
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("UPDATE user SET token=NULL WHERE token=?");
			ps.setString(1, token);
		    int result = ps.executeUpdate();
		    if(result != 1)
		    	throw new IllegalArgumentException("You are already logged out...");
		    ps.close();
		    conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}