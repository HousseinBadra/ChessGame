package org.example.chessui.Controllers;

import engine.ChessGame;
import engine.types.ChessMove;
import engine.types.ChessPiece;
import engine.types.ChessPlayer;
import engine.types.Position;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class TwoPlayersLocalGame {

    @FXML
    public Button undoButton;
    @FXML
    public Button resetButton; // New button for reset
    public Button backButton;
    @FXML
    private GridPane chessBoard;
    @FXML
    private Label turnLabel; // Label to show whose turn it is
    @FXML
    private Label gameStatusLabel; // Label to show game status (check, checkmate, stalemate)

    private final ArrayList<ArrayList<StackPane>> board = new ArrayList<>();
    private ChessGame game = new ChessGame(); // Make it non-final so we can reinitialize
    private ArrayList<ChessMove> moves = new ArrayList<>();
    private Position currentPosition = null;

    @FXML
    public void initialize() {
        setupBoard();
        render();
    }

    private void setupBoard() {
        chessBoard.getChildren().clear(); // Clear existing children if re-initializing
        board.clear(); // Clear existing board
        int tileSize = 70; // Slightly larger tiles

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle square = new Rectangle(tileSize, tileSize);
                boolean light = (row + col) % 2 == 0;
                square.setFill(light ? Color.web("#F0D9B5") : Color.web("#B58863")); // Nicer chess colors
                square.getStyleClass().add("square");
                StackPane tile = new StackPane(square);


                // Add click listener
                int finalRow = 7 - row; // Adjust for chessboard coordinate system
                int finalCol = col;
                tile.setOnMouseClicked(event -> handleClick(finalRow, finalCol));
                if (board.size() == row) {
                    board.add(new ArrayList<>());
                }
                board.get(row).add(tile);
                chessBoard.add(tile, col, row);
            }
        }
    }

    void handleClick(int row, int col) {
        ChessPiece piece = game.getBoard().get(row).get(col);

        // If a piece was already selected, and we click on the same piece again, deselect it
        if (currentPosition != null && currentPosition.x == col && currentPosition.y == row) {
            currentPosition = null;
            moves.clear();
            render();
            return;
        }

        // click on one of my pieces
        if (piece != null && piece.player == game.getCurrentPlayer()) {
            currentPosition = new Position(col, row);
            moves = game.getLegalMoves(currentPosition);
        }
        // check if clicked position is a move
        else if (currentPosition != null) {
            boolean moveMade = false;
            for (ChessMove move : moves) {
                if (move.getTo().x == col && move.getTo().y == row) {
                    StackPane sourceTile = board.get(7 - move.getFrom().y).get(move.getFrom().x);
                    StackPane targetTile = board.get(7 - move.getTo().y).get(move.getTo().x);

                    // Find the pieceLabel to animate
                    Label pieceLabel = null;
                    if (sourceTile.getChildren().size() > 1 && sourceTile.getChildren().get(1) instanceof Label) {
                        pieceLabel = (Label) sourceTile.getChildren().get(1);
                    }

                    if (pieceLabel != null) {

                        finalizeMove(move);

                    } else {
                        // If no label found for animation, just finalize the move immediately
                        finalizeMove(move);
                    }
                    moveMade = true;
                    break; // Exit loop after finding and processing the move
                }
            }
            if (!moveMade) { // If no valid move was made, deselect the current piece
                currentPosition = null;
                moves.clear();
            }
        }
        render();
    }

    private void finalizeMove(ChessMove move) {
        // apply move in game model
        game.applyMove(move);
        game.togglePlayer(); // This will be handled by the game logic after a successful move

        // clear selection & highlights
        currentPosition = null;
        moves.clear();
        chessBoard.setScaleY(game.getCurrentPlayer() == ChessPlayer.White ? 1 : -1);

        // re-render the board so the piece actually lives in the new tile
        render();
    }

    void render() {
        ArrayList<ArrayList<ChessPiece>> r = game.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                StackPane tile = board.get(i).get(j);
                tile.getStyleClass().remove("current-square");
                tile.getStyleClass().remove("highlighted-square");
                // Remove existing piece labels
                tile.getChildren().removeIf(node -> node instanceof Label);
                tile.setScaleY(game.getCurrentPlayer() == ChessPlayer.White ? 1 : -1); // Flip pieces for current player
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                StackPane tile = board.get(7 - i).get(j); // Access board using inverted row for display
                ChessPiece piece = r.get(i).get(j);
                if (piece != null) {
                    Label pieceLabel = new Label(piece.render());
                    pieceLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: " + (piece.player == ChessPlayer.White ? "white" : "black") + ";");
                    pieceLabel.setScaleY(game.getCurrentPlayer() == ChessPlayer.White ? 1 : -1); // Flip pieces
                    tile.getChildren().add(pieceLabel);
                }
            }
        }

        if (currentPosition != null) {
            board.get(7 - currentPosition.y).get(currentPosition.x).getStyleClass().add("current-square");
        }
        for (ChessMove m : moves) {
            board.get(7 - m.getTo().y).get(m.getTo().x).getStyleClass().add("highlighted-square");
        }

        // Update turn label
        turnLabel.setText(game.getCurrentPlayer() == ChessPlayer.White ? "White's Turn" : "Black's Turn");

        // Update game status label
        if (game.isCheckmate(game.getCurrentPlayer())) {
            gameStatusLabel.setText("Checkmate! " + (game.getCurrentPlayer() == ChessPlayer.White ? "Black" : "White") + " wins!");
            turnLabel.setText(""); // Clear turn label on game over
        } else if (game.isStalemate(game.getCurrentPlayer())) {
            gameStatusLabel.setText("Stalemate! It's a draw.");
            turnLabel.setText(""); // Clear turn label on game over
        } else if (game.isKingInCheck(game.getCurrentPlayer())) {
            gameStatusLabel.setText(game.getCurrentPlayer() == ChessPlayer.White ? "White is in Check!" : "Black is in Check!");
        } else {
            gameStatusLabel.setText(""); // Clear status if no special state
        }
    }

    @FXML
    private void handleUndo() {
        if (game.getMoveHistory().size() <= 1) return;
        game.undo();
        game.togglePlayer(); // This will be handled by the game logic after a successful undo
        currentPosition = null;
        moves.clear();
        chessBoard.setScaleY(game.getCurrentPlayer() == ChessPlayer.White ? 1 : -1);
        render();

    }

    @FXML
    private void handleNewGame() {
        game = new ChessGame(); // Reinitialize the game
        currentPosition = null;
        moves.clear();
        chessBoard.setScaleY(1); // Reset board orientation to White
        setupBoard(); // Re-setup board for new game
        render(); // Render the new game state
    }

    @FXML
    private void handleGoBack(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/chessui/main_menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene c = new Scene(root);
            stage.setScene(c);
            stage.show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}