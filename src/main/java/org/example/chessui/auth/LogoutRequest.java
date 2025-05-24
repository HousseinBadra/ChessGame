package org.example.chessui.auth;

public class LogoutRequest {
    public String token;

    public LogoutRequest() {
    }

    public LogoutRequest(String t) {
        token = t;
    }
}
