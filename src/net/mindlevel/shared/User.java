package net.mindlevel.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class User implements IsSerializable {
    private String username;
    private String token;
    private boolean adult;
    private int permission;
    private int score;
    private String name, location, about;
    private Date created, lastLogin;
    private String pictureUrl;

    public User() {
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

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String description) {
        this.about = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isAdmin() {
        return permission==1;
    }

    public boolean isModerator() {
        return permission>=1 && permission<=2;
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

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return username + " " + location + " " + adult + " " + permission + " " + lastLogin + " "
                + created + " " + about;
    }

    public String getPicture() {
        return pictureUrl;
    }

    public void setPicture(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getThumbnail() {
        return pictureUrl.replace("scaled", "thumb");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
