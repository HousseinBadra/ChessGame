package org.example.chessui.auth;

public class SessionManager {
    private static SessionManager instance;
    private String token, username;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    public void setSession(String t, String u) {
        token = t;
        username = u;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void clear() {
        token = null;
        username = null;
    }
}