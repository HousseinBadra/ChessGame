package org.example.chessui.auth;

import java.net.http.HttpResponse;

public class AuthService {
    private static final String BASE = "http://localhost:8081";

    public String login(LoginRequest req) throws Exception {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}",
                req.username, req.password);
        HttpResponse<String> r = HttpClientUtil.post(BASE + "/login", json);
        // backend returns raw token string
        return r.body().replaceAll("\"", "");
    }

    public String register(RegisterRequest req) throws Exception {
        String json = String.format(
                "{\"username\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                req.username, req.email, req.password);
        HttpResponse<String> r = HttpClientUtil.post(BASE + "/register", json);
        return r.body().replaceAll("\"", "");
    }

    public void logout() throws Exception {
        String t = SessionManager.getInstance().getToken();
        String json = String.format("{\"token\":\"%s\"}", t);
        HttpClientUtil.post(BASE + "/logout", json);
        SessionManager.getInstance().clear();
    }
}
