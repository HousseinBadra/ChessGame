package org.example.chessui.auth;

public class LoginRequest {
    public String username;
    public String password;

    public LoginRequest() {
    }

    public LoginRequest(String u, String p) {
        username = u;
        password = p;
    }
}
