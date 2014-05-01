package net.mindlevel.shared;

import java.io.Serializable;
import java.util.Date;

public class Report implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username, type, url, content;
    private Date timestamp;

    protected Report() {}

    public Report(String username, String type, String url, String content) {
        this.setUsername(username);
        this.setType(type);
        this.setUrl(url);
        this.setContent(content);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String report) {
        this.content = report;
    }
}