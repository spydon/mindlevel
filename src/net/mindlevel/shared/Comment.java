package net.mindlevel.shared;

import java.io.Serializable;

public class Comment implements Serializable{
    private static final long serialVersionUID = 1L;
    private String username, comment, repliedTo;
    private int id, timestamp;
    private int parentId; //If parentId is 0 the comment is a direct comment and not a reply to somebody else.

    public Comment(int id,
                   String username,
                   String comment,
                   int parentId,
                   int timestamp) {
        this.setId(id);
        this.setUsername(username);
        this.setComment(comment);
        this.setParentId(parentId);
        this.timestamp = timestamp;
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

    public String getRepliedTo() {
        return repliedTo;
    }

    public void setRepliedTo(String repliedTo) {
        this.repliedTo = repliedTo;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getId() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setId(int id) {
        this.id = id;
    }
}