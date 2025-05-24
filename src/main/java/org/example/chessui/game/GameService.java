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
import java.util.Objects;

public class GameService {
    private static final String BASE_URL = "http://localhost:8081/api/games";
    private final HttpClient httpClient;
    private final Gson gson;
    private final SessionManager authManager = SessionManager.getInstance();

    /**
     * @param authManager provides JWT token for authenticated requests
     */
    private List<GameDTO> history;
    private Long index = (long) -1;

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public List<GameDTO> getHistory() {
        return history;
    }

    private static GameService instance;
    private String token, username;

    public static GameService getInstance() {
        if (instance == null) instance = new GameService();
        return instance;
    }

    private GameService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
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
    public List<GameDTO> getGamesByUser() throws IOException, InterruptedException {
        HttpRequest request = withAuthHeaders(HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/" + SessionManager.getInstance().getUsername())))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            this.history = gson.fromJson(response.body(), new TypeToken<List<GameDTO>>() {
            }.getType());
            return this.history;
        } else {
            throw new RuntimeException("Failed to fetch games: " + response.body());
        }
    }

    public List<String> convertChessMove(List<ChessMove> moves) {
        return moves.stream().map(gson::toJson).toList();
    }


    public List<ChessMove> convertResponseToMoves() {

        GameDTO d = getHistory().stream().filter(e -> Objects.equals(e.id, getIndex())).toList().getFirst();
        return d.moves.stream()
                .map(json -> gson.fromJson(json, ChessMove.class))
                .toList();
    }
}