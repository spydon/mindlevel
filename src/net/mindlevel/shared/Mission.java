package net.mindlevel.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class Mission implements Serializable{
    private static final long serialVersionUID = 1L;
    private String name, description, creator, timestamp;
    private int id;
    private boolean adult;
    private ArrayList<String> categories;

    public Mission(String name,
                   ArrayList<String> categories,
                   String description,
                   String creator,
                   boolean adult) {
        this.name = name;
        this.categories = categories;
        this.description = description;
        this.creator = creator;
        this.adult = adult;
    }

    public Mission() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}