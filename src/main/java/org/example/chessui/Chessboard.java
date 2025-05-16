package org.example.chessui;
import engine.ChessGame;
import engine.types.ChessMove;
import engine.types.ChessPiece;
import engine.types.ChessPlayer;
import engine.types.Position;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

public class Chessboard {

    public Button undoButton;
    @FXML private GridPane chessBoard;

    //
    private final ArrayList<ArrayList<StackPane>> board = new ArrayList<>();
    private final ChessGame game = new ChessGame();
    private ArrayList<ChessMove> moves = new ArrayList<>();
    private Position currentPosition = null;

    @FXML
    public void initialize() {
        int tileSize = 60;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle square = new Rectangle(tileSize, tileSize);
                boolean light = (row + col) % 2 == 0;
                square.setFill(light ? Color.BEIGE : Color.GRAY);
                square.getStyleClass().add("square");
                StackPane tile = new StackPane(square);


                // Add click listener
                int finalRow = 7 - row;
                int finalCol = col;
                tile.setOnMouseClicked(event -> handleClick(finalRow, finalCol));
                if(board.size() == row) {
                    board.add(new ArrayList<>());
                }
                board.get(row).add(tile);
                chessBoard.add(tile, col, row);
            }
        }
        render();
    }

    void handleClick(int row, int col) {
        ChessPiece piece = game.getBoard().get(row).get(col);
        // click on one of my pieces
        if(piece != null && piece.player == game.getCurrentPlayer()) {
            currentPosition = new Position(col, row);
            moves = game.getLegalMoves(currentPosition);
        }

        // check if clicked position is a move
        else if(currentPosition != null){
            if(row != currentPosition.y || col != currentPosition.x) {
                for(int i=0; i< moves.size(); i++) {
                    ChessMove move = moves.get(i);
                    if(move.getTo().x == col && move.getTo().y == row) {
                        StackPane sourceTile = board.get(7 - move.getFrom().y).get(move.getFrom().x);
                        StackPane targetTile = board.get(7 - move.getTo().y).get(move.getTo().x);

                        // assuming render() just put a Label at index 1
                        Label pieceLabel = (Label) sourceTile.getChildren().get(1);

                        // 2. Play the animation, then finalize the move
                        animatePieceMove(sourceTile, targetTile, pieceLabel, () -> {
                            // apply move in game model
                            game.applyMove(move);
                            game.togglePlayer();

                            // clear selection & highlights
                            currentPosition = null;
                            moves.clear();
                            chessBoard.setScaleY(game.getCurrentPlayer() == ChessPlayer.White ? 1 : -1);

                            // re-render the board so the piece actually lives in the new tile
                            render();
                        });
                    }
                }
            }
        }

        render();
    }

    void render() {
        ArrayList<ArrayList<ChessPiece>> r = game.getBoard();
        for (int i =0; i< 8; i++) {
            for (int j =0; j< 8; j++) {
                board.get(i).get(j).getStyleClass().remove("current-square");
                board.get(i).get(j).getStyleClass().remove("highlighted-square");
                if(board.get(i).get(j).getChildren().size() == 2) {
                    board.get(i).get(j).getChildren().removeLast();
                }
                board.get(i).get(j).setScaleY(game.getCurrentPlayer() == ChessPlayer.White ? 1 : -1);
            }
        }


        for(int i = 0; i< 8; i++ ){
            for(int j = 0; j< 8;j++ ){
                StackPane tile = board.get(7 - i).get(j);
                ChessPiece piece = r.get(i).get(j);
                tile.getChildren().removeAll();
                if(piece != null) {
                    Label pieceLabel = new Label(piece.render());
                    pieceLabel.setStyle("-fx-font-size: 32px;");  // Adjust font size
                    tile.getChildren().add(pieceLabel);
                }
            }
        }

        if(currentPosition != null) {
            board.get(7 - currentPosition.y).get(currentPosition.x).getStyleClass().add("current-square");
        }
        for(ChessMove m : moves) {
            board.get(7 - m.getTo().y).get(m.getTo().x).getStyleClass().add("highlighted-square");
        }
    }

    private void animatePieceMove(
            StackPane sourceTile,
            StackPane targetTile,
            Label pieceLabel,
            Runnable onFinished
    ) {
        // 1. Remove piece from source tile
        sourceTile.getChildren().remove(pieceLabel);

        // 2. Create overlay pane for animation (avoids scaling issues)
        StackPane animationLayer = new StackPane(pieceLabel);
        animationLayer.setMouseTransparent(true);

        // Add to the same parent as chessBoard (bypass chessboard scaling)
        ((Pane) chessBoard.getParent()).getChildren().add(animationLayer);

        // 3. Get positions in scene coordinates
        Bounds sourceScene = sourceTile.localToScene(sourceTile.getBoundsInLocal());
        Bounds targetScene = targetTile.localToScene(targetTile.getBoundsInLocal());
        Bounds chessBoardScene = chessBoard.localToScene(chessBoard.getBoundsInLocal());

        // 4. Convert to animationLayer coordinates
        double startX = sourceScene.getMinX() - chessBoardScene.getMinX();
        double startY = sourceScene.getMinY() - chessBoardScene.getMinY();
        double endX = targetScene.getMinX() - chessBoardScene.getMinX();
        double endY = targetScene.getMinY() - chessBoardScene.getMinY();

        // 5. Position overlay initially
        animationLayer.setLayoutX(chessBoardScene.getMinX());
        animationLayer.setLayoutY(chessBoardScene.getMinY());
        pieceLabel.relocate(startX + 16, startY + 8);
        // 6. Animate to target position
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), pieceLabel);
        tt.setFromX(startX + 16);
        tt.setFromY(startY + 8);
        tt.setToX(endX + 16);
        tt.setToY(endY + 8);

        tt.setOnFinished(e -> {
            // 7. Cleanup and finalize
            ((Pane) chessBoard.getParent()).getChildren().remove(animationLayer);
            onFinished.run();
        });

        tt.play();
    }

    @FXML
    private void handleUndo() {
        // Implement undo logic here
        game.undo();
        game.togglePlayer();

        // clear selection & highlights
        currentPosition = null;
        moves.clear();
        chessBoard.setScaleY(game.getCurrentPlayer() == ChessPlayer.White ? 1 : -1);

        // re-render the board so the piece actually lives in the new tile
        render();
    }
}
