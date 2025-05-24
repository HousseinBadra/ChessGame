package org.example.chessui.game;

import org.example.chessui.engine.types.ChessMove;

import java.util.List;

public class GameDTO {
    public Long id;
    public String whiteUsername;
    public String blackUsername;
    public List<String> moves;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWhiteUsername() { return whiteUsername; }
    public void setWhiteUsername(String whiteUsername) { this.whiteUsername = whiteUsername; }
    public String getBlackUsername() { return blackUsername; }
    public void setBlackUsername(String blackUsername) { this.blackUsername = blackUsername; }
    public List<String> getMoves() { return moves; }
    public void setMoves(List<String> moves) { this.moves = moves; }


}
