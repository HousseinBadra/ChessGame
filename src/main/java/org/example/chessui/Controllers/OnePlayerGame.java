package org.example.chessui.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.example.chessui.auth.SessionManager;
import org.example.chessui.engine.ChessGame;
import org.example.chessui.engine.types.ChessMove;
import org.example.chessui.engine.types.ChessPiece;
import org.example.chessui.engine.types.ChessPlayer;
import org.example.chessui.engine.types.Position;
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
import org.example.chessui.game.GameDTO;
import org.example.chessui.game.GameService;

import java.util.ArrayList;

public class OnePlayerGame {

    @FXML public Button resetButton;
    public Button backButton;
    public Button saveButton;
    @FXML private GridPane chessBoard;
    @FXML private Label turnLabel;
    @FXML private Label gameStatusLabel;

    private final GameService gameService = GameService.getInstance();
    private final ArrayList<ArrayList<StackPane>> board = new ArrayList<>();
    private ChessGame game = new ChessGame(); // Make it non-final so we can reinitialize
    private ArrayList<ChessMove> moves = new ArrayList<>();
    private Position currentPosition = null;
    private boolean saved = false;
    private long gameId = -1;
    private boolean isAiMooving = false;

    private final ChessPlayer PLAYER_COLOR = ChessPlayer.White; // Human player is always White
    private final ChessPlayer AI_COLOR = ChessPlayer.Black;     // AI is always Black

    @FXML
    public void initialize() {
        setupBoard();
        render();
        // If AI starts (e.g., if you later add an option for AI to play white), call makeAiMove() here
        // Currently, human (White) always starts.
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
                int finalRow = 7 - row; // Adjust for chessboard coordinate system (0,0 is bottom-left for White)
                int finalCol = col;
                tile.setOnMouseClicked(event -> handleClick(finalRow, finalCol));
                if(board.size() == row) {
                    board.add(new ArrayList<>());
                }
                board.get(row).add(tile);
                chessBoard.add(tile, col, row);
            }
        }
    }

    void handleClick(int row, int col) {
        // Only allow human clicks if it's the human player's turn and the game is not over
        if (game.getCurrentPlayer() != PLAYER_COLOR || game.isCheckmate(PLAYER_COLOR) || isAiMooving) {
            return;
        }

        ChessPiece piece = game.getBoard().get(row).get(col);

        // If a piece was already selected, and we click on the same piece again, deselect it
        if (currentPosition != null && currentPosition.x == col && currentPosition.y == row) {
            currentPosition = null;
            moves.clear();
            render();
            return;
        }

        // click on one of my pieces (human player's pieces)
        if(piece != null && piece.player == PLAYER_COLOR) { // Check against PLAYER_COLOR
            currentPosition = new Position(col, row);
            moves = game.getLegalMoves(currentPosition);
            render();
            return;
        }
        // check if clicked position is a valid move for the currently selected piece
        else if(currentPosition != null){
            boolean moveMade = false;
            for(ChessMove move : moves) {
                if(move.getTo().x == col && move.getTo().y == row) {
                    game.applyMove(move);
                    game.togglePlayer(); // Switch to AI's turn
                    moveMade = true;
                    break; // Exit loop after finding and processing the move
                }
            }
            // Clear selection & highlights after attempt to move
            currentPosition = null;
            moves.clear();
            render(); // Re-render to update the board state

            // If a human move was made, and it's now AI's turn, and the game is not over, make AI move
            if (moveMade && game.getCurrentPlayer() == AI_COLOR && !game.isCheckmate(ChessPlayer.Black)) {
                var timeline =
                        new Timeline(
                                new KeyFrame(Duration.millis(100), e -> makeAiMove()));
                timeline.playFromStart();
            }
        }
        // If clicked on an empty square or opponent's piece without a piece selected, just re-render
        // This ensures highlights are removed if an invalid move is clicked
        else {
            render();
        }
    }

    void render() {
        ArrayList<ArrayList<ChessPiece>> r = game.getBoard();
        for (int i =0; i< 8; i++) {
            for (int j =0; j< 8; j++) {
                StackPane tile = board.get(i).get(j);
                tile.getStyleClass().remove("current-square");
                tile.getStyleClass().remove("highlighted-square");
                // Remove existing piece labels
                tile.getChildren().removeIf(node -> node instanceof Label);
                // No board flipping for AI game: tile.setScaleY(game.getCurrentPlayer() == ChessPlayer.White ? 1 : -1);
            }
        }

        for(int i = 0; i< 8; i++ ){
            for(int j = 0; j< 8;j++ ){
                StackPane tile = board.get(7 - i).get(j); // Access board using inverted row for display (0,0 is bottom-left)
                ChessPiece piece = r.get(i).get(j);
                if(piece != null) {
                    Label pieceLabel = new Label(piece.render());
                    // Set piece color for better visibility
                    pieceLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: " + (piece.player == ChessPlayer.White ? "white" : "black") + ";");
                    // No piece flipping for AI game: pieceLabel.setScaleY(game.getCurrentPlayer() == ChessPlayer.White ? 1 : -1);
                    tile.getChildren().add(pieceLabel);
                }
            }
        }

        // Apply highlights for current selection
        if(currentPosition != null) {
            board.get(7 - currentPosition.y).get(currentPosition.x).getStyleClass().add("current-square");
        }
        for(ChessMove m : moves) {
            board.get(7 - m.getTo().y).get(m.getTo().x).getStyleClass().add("highlighted-square");
        }

        // Update turn label
        turnLabel.setText(game.getCurrentPlayer() == PLAYER_COLOR ? "Your Turn (White)" : "AI's Turn (Black)");

        // Update game status label
        if (game.isCheckmate(game.getCurrentPlayer())) {
            gameStatusLabel.setText("Checkmate! " + (game.getCurrentPlayer() == PLAYER_COLOR ? "AI" : "You") + " wins!");
            turnLabel.setText("Game Over"); // Clear turn label on game over
        } else if (game.isStalemate(game.getCurrentPlayer())) {
            gameStatusLabel.setText("Stalemate! It's a draw.");
            turnLabel.setText("Game Over"); // Clear turn label on game over
        } else if (game.isKingInCheck(game.getCurrentPlayer())) {
            gameStatusLabel.setText(game.getCurrentPlayer() == PLAYER_COLOR ? "You are in Check!" : "AI is in Check!");
        } else {
            gameStatusLabel.setText(""); // Clear status if no special state
        }
    }

    private void makeAiMove() {
        if (game.isCheckmate(ChessPlayer.Black)) {
            return; // Don't make AI move if game is already over
        }
        isAiMooving = true;
        ChessMove aiMove = game.getBestMove();
        isAiMooving = false;

        if (aiMove != null) {
            game.applyMove(aiMove);
            game.togglePlayer(); // Switch back to human player
            render();
        }
    }

    @FXML
    private void handleNewGame() {
        game = new ChessGame(); // Reinitialize the game
        currentPosition = null;
        moves.clear();
        // No board flipping in AI mode, so no chessBoard.setScaleY(1); needed
        setupBoard(); // Re-setup board for new game
        render(); // Render the new game state
        // If AI starts (e.g., if you later add an option for AI to play white), call makeAiMove() here
        // Currently, human (White) always starts.
    }
    @FXML
    private void handleGoBack(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/chessui/main_menu.fxml"));
            ;
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

    @FXML
    private void saveGame() {
        SessionManager s = SessionManager.getInstance();
        try {
            GameDTO g = new GameDTO();
            g.setWhiteUsername(s.getUsername());
            g.setBlackUsername("AI");
            g.setMoves(gameService.convertChessMove(game.getMoveHistory().subList(1,game.getMoveHistory().size())));
            if(!saved) {
                this.gameId = gameService.addGame(g).id;
                this.saved = true;
            }
            else {
                g.setId(gameId);
                gameService.updateGame(g);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}