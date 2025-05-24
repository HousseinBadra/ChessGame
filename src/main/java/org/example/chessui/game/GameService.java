package org.example.chessui.game;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.chessui.auth.SessionManager;
import org.example.chessui.engine.types.ChessMove;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GameService {
    private static final String BASE_URL = "http://localhost:8081/api/games";
    private final HttpClient httpClient;
    private final Gson gson;
    private final SessionManager authManager;

    /**
     * @param authManager provides JWT token for authenticated requests
     */
    public GameService(SessionManager authManager) {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
        this.authManager = authManager;
    }

    private HttpRequest.Builder withAuthHeaders(HttpRequest.Builder builder) {
        String token = authManager.getToken();
        return builder
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token);
    }

    /**
     * Add a new game to the backend.
     */
    public GameDTO addGame(GameDTO game) throws IOException, InterruptedException {
        String json = gson.toJson(game);
        HttpRequest request = withAuthHeaders(HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/add")))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return gson.fromJson(response.body(), GameDTO.class);
        } else {
            throw new RuntimeException("Failed to add game: " + response.body());
        }
    }

    /**
     * Update an existing game on the backend.
     */
    public GameDTO updateGame(GameDTO game) throws IOException, InterruptedException {
        String json = gson.toJson(game);
        HttpRequest request = withAuthHeaders(HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/update/" + game.id.toString())))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return gson.fromJson(response.body(), GameDTO.class);
        } else {
            throw new RuntimeException("Failed to update game: " + response.body());
        }
    }

    /**
     * Get all games for a specific user.
     */
    public List<GameDTO> getGamesByUser(Long userId) throws IOException, InterruptedException {
        HttpRequest request = withAuthHeaders(HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/" + userId)))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return gson.fromJson(response.body(), new TypeToken<List<GameDTO>>() {}.getType());
        } else {
            throw new RuntimeException("Failed to fetch games: " + response.body());
        }
    }

    public List<String> convertChessMove(List<ChessMove> moves) {
        return moves.stream().map(gson::toJson).toList();
    }
}