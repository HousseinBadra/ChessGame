package org.example.chessui.auth;

public class RegisterRequest {
    public String username;
    public String email;
    public String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String u, String e, String p) {
        username = u;
        email = e;
        password = p;
    }
}