package org.example.chessui.engine.MoveStrategy;

import org.example.chessui.engine.types.ChessMove;
import org.example.chessui.engine.types.ChessPiece;
import org.example.chessui.engine.types.ChessPlayer;
import org.example.chessui.engine.types.Position;

import java.util.ArrayList;

public class KingMove extends MoveStrategy {
    KingMove() {
        super();
    }

    static boolean isPositionAttacked(ArrayList<ArrayList<ChessPiece>> board, Position target, ChessPlayer player) {
        ChessPiece p = board.get(target.y).get(target.x);
        for (int i = 0; i< board.size(); i++) {
            ArrayList<ChessPiece> row = board.get(i);
            for (int j = 0; j < row.size(); j++) {
                ChessPiece piece = row.get(j);
                Position checkingPosition = new Position(j, i);
                if (piece != null && piece.player != player){
                    board.get(target.y).set(target.x, null);
                    for (MoveStrategy stg : piece.strategy){
                        if (stg.canAttack(board, piece, target, null, checkingPosition)) {
                            board.get(target.y).set(target.x, p);
                            return true;
                        }}
                    board.get(target.y).set(target.x, p);
            }}
        }
        return false;
    }

    public ArrayList<ChessMove> generatePossibleMoves(ArrayList<ArrayList<ChessPiece>> board, ChessPiece piece, ChessMove lastMove, Position position) {
        ArrayList<ChessMove> result = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int[] direction : directions) {
            Position newPosition = new Position(position.x + direction[0], position.y + direction[1]);
            if(newPosition.isValid()){
            if (!isPositionAttacked(board, newPosition, piece.player)) {
                if (board.get(newPosition.y).get(newPosition.x) == null) {
                    result.add(new ChessMove(position, newPosition, piece.getClone(), null, null, Strategies.KingMove, null, null));
                } else if (board.get(newPosition.y).get(newPosition.x).player != piece.player) {
                    result.add(new ChessMove(position, newPosition, piece.getClone(), board.get(newPosition.y).get(newPosition.x).getClone(), null, Strategies.KingMove, newPosition, null));
                }
            }
        }}
        return result;
    }

    public boolean canAttack(ArrayList<ArrayList<ChessPiece>> board, ChessPiece attacker, Position target, ChessMove lastMove, Position position) {

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int[] direction : directions) {
            Position newPosition = new Position(position.x + direction[0], position.y + direction[1]);
            if(newPosition.isValid()) {
                ChessPiece targetPiece = board.get(newPosition.y).get(newPosition.x);
                if (newPosition.x == target.x && newPosition.y == target.y) {
                    return targetPiece == null || targetPiece.player != attacker.player;
                }
            }
        }
        return false;
    }
}
