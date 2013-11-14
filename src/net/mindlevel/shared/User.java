package net.mindlevel.shared;

import java.io.Serializable;

public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    private String username;
    private String token;
    private boolean adult;
    private int permission;
    private String location, description, created;
    private String lastLogin;
//    private final HashMap<String, Integer> scores = new HashMap<String, Integer>();
    //TODO: Change this datastructure

    public User() {
//        scores.put("crazy", 0);
//        scores.put("nice", 0);
//        scores.put("inventive", 0);
//        scores.put("artistic", 0);
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
//        scores.put(scoreName, score);
    }

    public int getScore(String scoreName) {
//        return scores.get(scoreName);
        return 0;
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
        return 1==permission;
    }

    public boolean isModerator() {
        return 2==permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
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

    @Override
    public String toString() {
        return username + " " + location + " " + adult + " " + permission + " " + lastLogin + " "
                + created + " " + description;
    }
}
