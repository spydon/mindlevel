package net.mindlevel.shared;

import java.io.Serializable;

public class Captcha implements Serializable{
    private static final long serialVersionUID = 1L;
    private String question, token;

    public Captcha(String question, String token) {
        this.setQuestion(question);
        this.setToken(token);
    }

    public Captcha() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}