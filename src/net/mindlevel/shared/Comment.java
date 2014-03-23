package net.mindlevel.shared;

import java.io.Serializable;

public class Comment implements Serializable{
    private static final long serialVersionUID = 1L;
    private String username, comment;
    private int id;
    private int timestamp;
    private int parentId = 0; //If parentId is 0 the comment is a direct comment and not a reply to somebody else.
    private int threadId;

    protected Comment() {}

    public Comment(int threadId) { //To add non-existing parent comment
        this.id = 0;
        this.parentId = 0;
        this.setThreadId(threadId);
    }

    public Comment(int id,
                   int threadId,
                   String username,
                   String comment,
                   int parentId,
                   int timestamp) {
        this.setId(id);
        this.setThreadId(threadId);
        this.setUsername(username);
        this.setComment(comment);
        this.setParentId(parentId);
        this.timestamp = timestamp;
    }

    public Comment(int threadId,
                   String username,
                   String comment,
                   int parentId) {
        this.setThreadId(threadId);
        this.setUsername(username);
        this.setComment(comment);
        this.setParentId(parentId);
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
}