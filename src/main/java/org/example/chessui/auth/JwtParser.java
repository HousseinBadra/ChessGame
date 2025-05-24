package org.example.chessui.auth;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

public class JwtParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> parse(String jwt) throws Exception {
        String[] parts = jwt.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        return mapper.readValue(payload, Map.class);
    }
}
