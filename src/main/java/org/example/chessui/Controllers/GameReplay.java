package org.example.chessui.Controllers;

import javafx.fxml.FXML;
import org.example.chessui.engine.ChessGame;
import org.example.chessui.engine.types.ChessMove;
import org.example.chessui.engine.types.ChessPiece;
import org.example.chessui.engine.types.ChessPlayer;
import javafx.event.ActionEvent;
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
import org.example.chessui.game.GameService;

import java.util.ArrayList;
import java.util.List;

public class GameReplay {

    @FXML
    public Button undoButton;
    @FXML
    public Button resetButton; // New button for reset
    public Button backButton;
    public Button saveButton;
    public Button moveForward;
    @FXML
    private GridPane chessBoard;
    @FXML
    private Label turnLabel; // Label to show whose turn it is
    @FXML
    private Label gameStatusLabel; // Label to show game status (check, checkmate, stalemate)

    private final ArrayList<ArrayList<StackPane>> board = new ArrayList<>();
    private final ChessGame game = new ChessGame();
    List<ChessMove> moveHistory;// Make it non-final so we can reinitialize

    @FXML
    public void initialize() {
        moveHistory = GameService.getInstance().convertResponseToMoves();
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
                if (board.size() == row) {
                    board.add(new ArrayList<>());
                }
                board.get(row).add(tile);
                chessBoard.add(tile, col, row);
            }
        }
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
                tile.setScaleY(1); // Flip pieces for current player
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                StackPane tile = board.get(7 - i).get(j); // Access board using inverted row for display
                ChessPiece piece = r.get(i).get(j);
                if (piece != null) {
                    Label pieceLabel = new Label(piece.render());
                    pieceLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: " + (piece.player == ChessPlayer.White ? "white" : "black") + ";");
                    pieceLabel.setScaleY(1); // Flip pieces
                    tile.getChildren().add(pieceLabel);
                }
            }
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
        if (game.getMoveHistory().size() <= 0) return;
        game.undo();
        game.togglePlayer(); // This will be handled by the game logic after a successful undo
        render();

    }

    @FXML
    private void handleGoBack(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/chessui/gameList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene c = new Scene(root);
            c.getStylesheets().add(getClass().getResource("/org/example/chessui/styles.css").toExternalForm());
            stage.setScene(c);
            stage.show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void moveForward(ActionEvent actionEvent) {
        int index = game.getMoveHistory().size();
        if(index >= moveHistory.size()) return;
        game.applyMove(moveHistory.get(index - 1));
        game.togglePlayer(); // This will be handled by the game logic after a successful undo
        render();
    }
}