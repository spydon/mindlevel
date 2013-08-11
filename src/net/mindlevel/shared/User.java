package net.mindlevel.shared;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String username = "TEST";
	private String token = "fett-random-token";
	private boolean adult, admin;
	private String location, description, created; 
	private String lastLogin = "1372124910";
	private HashMap<String, Integer> scores = new HashMap<String, Integer>();
	
	public User() {
		scores.put("crazy", 0);
		scores.put("nice", 0);
		scores.put("inventive", 0);
		scores.put("artistic", 0);
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String Username) {
		this.username = Username;
	}

	public String getToken() {
		return token;
	}
	
	public void setScore(String scoreName, int score) {
		scores.put(scoreName, score);
	}
	
	public int getScore(String scoreName) {
		return scores.get(scoreName);
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isAdult() {
		return adult;
	}

	public void setAdult(boolean adult) {
		this.adult = adult;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public String toString() {
		return username + " " + location + " " + adult + " " + admin + " " + lastLogin + " "
				+ created + " " + description;
	}
}
