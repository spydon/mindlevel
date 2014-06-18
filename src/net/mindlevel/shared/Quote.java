package net.mindlevel.shared;

import java.io.Serializable;


public class Quote implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String quote;

    protected Quote() {}

    public Quote(String username, String quote) {
        this.username = username;
        this.quote = quote;
    }

    public String getUsername() {
        return username;
    }

    public String getQuote() {
        return quote;
    }

    @Override
    public String toString() {
        return getQuote() + " /" + getUsername();
    }
}