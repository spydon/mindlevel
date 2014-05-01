package net.mindlevel.shared;

import java.io.Serializable;
import java.util.HashSet;

public class MetaImage implements Serializable{
    private static final long serialVersionUID = 1L;
    private String title, description, location, filename, owner, date, token;
    private HashSet<String> tags;
    private HashSet<Category> categories;
    private boolean adult = false;
    private int imageCount = Integer.MAX_VALUE;
    private int id;
    private int relativeId;
    private int score;
    private int threadId;
    private Mission mission;

    public MetaImage() {
        this.filename = "notfound.jpg";
        this.imageCount = 0;
        this.relativeId = 0;
        this.id = 0;
    }

    public MetaImage(String filename, String title, String location,
                     Mission mission, String owner, String description, HashSet<String> tags,
                     boolean adult) {
        this.filename = filename;
        this.setId(id);
        this.title = title;
        this.location = location;
        this.mission = mission;
        this.owner = owner;
        this.description = description;
        this.tags = tags;
        this.adult = adult;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getThumbnail() {
        return filename.replace("scaled", "thumb");
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public HashSet<String> getTags() {
        return tags;
    }

    public void setTags(HashSet<String> tags) {
        this.tags = tags;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public HashSet<Category> getCategories() {
        return categories;
    }

    public void setCategories(HashSet<Category> categories) {
        this.categories = categories;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Mission getMission() {
        return mission;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getRelativeId() {
        return relativeId;
    }

    public void setRelativeId(int relativeId) {
        this.relativeId = relativeId;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
}