package de.glowman554.bot.platform.web;

import java.util.Random;

public class WebInstance {
    private final String authenticationString;
    private String userId;
    private boolean authenticated = false;

    public WebInstance() {
        this.authenticationString = String.valueOf(new Random().nextInt(9999999));
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthenticationString() {
        return authenticationString;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
