package net.mindlevel.shared;

import java.io.Serializable;
import java.util.Date;

public class News implements Serializable{
    private static final long serialVersionUID = 1L;
    private String username, content;
    private int id;
    private Date timestamp;

    protected News() {}

    public News(int id,
                String username,
                String content,
                Date timestamp) {
        this.setId(id);
        this.setUsername(username);
        this.setContent(content);
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}