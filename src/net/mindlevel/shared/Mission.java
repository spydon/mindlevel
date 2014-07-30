package net.mindlevel.shared;

import java.util.Date;
import java.util.HashSet;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Mission implements IsSerializable {
    private String name, description, creator;
    private Date created;
    private int id;
    private boolean adult, validated;
    private HashSet<Category> categories;

    public Mission(String name,
                   HashSet<Category> categories,
                   String description,
                   String creator,
                   boolean adult,
                   boolean validated) {
        this.name = name;
        this.categories = categories;
        this.description = description;
        this.creator = creator;
        this.adult = adult;
        this.validated = validated;
    }

    public Mission(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Mission(int id) {
        this.id = id;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public HashSet<Category> getCategories() {
        return categories;
    }

    public void setCategories(HashSet<Category> categories) {
        this.categories = categories;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }
}