package net.mindlevel.shared;

import java.io.Serializable;
import java.util.Date;

public class Ban implements Serializable{
    private static final long serialVersionUID = 1L;
    private final String username;
    private final Date expiryDate;
    private final String reason;

    public Ban(String username, String reason, Date expiryDate) {
        this.username = username;
        this.reason = reason;
        this.expiryDate = expiryDate;
    }

    public String getUsername() {
        return username;
    }

    public String getReason() {
        return reason;
    }

    public Date getExpiry() {
        return expiryDate;
    }

    public boolean isBanned() {
        return new Date().before(expiryDate);
    }

    @Override
    public String toString() {
        return username + " " + reason + " " + expiryDate + " Banned: " + isBanned();
    }
}