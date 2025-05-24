package org.example.chessui.engine.MoveStrategy;

import org.example.chessui.engine.types.ChessMove;
import org.example.chessui.engine.types.ChessPiece;
import org.example.chessui.engine.types.ChessPlayer;
import org.example.chessui.engine.types.Position;

import java.util.ArrayList;

public class Castle extends MoveStrategy {
    Castle() {
        super();
    }

    static boolean isPositionSafe(ArrayList<ArrayList<ChessPiece>> board, Position target, ChessPlayer player) {
        if(board.get(target.y).get(target.x) != null) return false;
        for (int i = 0; i < board.size(); i++) {
            ArrayList<ChessPiece> row = board.get(i);
            for (int j = 0; j < row.size(); j++) {
                ChessPiece piece = row.get(j);
                Position checkingPosition = new Position(j, i);
                if (piece != null && piece.player != player)
                    for (MoveStrategy stg : piece.getStrategy())
                        if (stg.canAttack(board, piece, target, null, checkingPosition)) {
                            return false;
                        }
            }
        }
        return true;
    }

    public ArrayList<ChessMove> generatePossibleMoves(ArrayList<ArrayList<ChessPiece>> board, ChessPiece piece, ChessMove lastMove, Position position) {
        ArrayList<ChessMove> result = new ArrayList<>();
        if (piece.player == ChessPlayer.White) {
            ChessPiece expectedKing = board.getFirst().get(4);
            ChessPiece exceptedRightRook = board.getFirst().getLast();
            ChessPiece exceptedLeftRook = board.getFirst().getFirst();
            if (expectedKing != null && !expectedKing.isHasMoved()) {
                if (exceptedRightRook != null && !exceptedRightRook.isHasMoved()) {
                    if (isPositionSafe(board, new Position(5, 0), piece.player) &&
                            isPositionSafe(board, new Position(6, 0), piece.player)) {
                        ChessMove sideEffect = new ChessMove(new Position(7, 0), new Position(5, 0), exceptedRightRook, null, null, Strategies.Castle, null, null);
                        result.add(new ChessMove(new Position(4, 0), new Position(6, 0), piece, null, sideEffect, Strategies.Castle, null, null));
                    }
                }
                if (exceptedLeftRook != null && !exceptedLeftRook.isHasMoved()) {
                    if (isPositionSafe(board, new Position(3, 0), piece.player) &&
                            isPositionSafe(board, new Position(2, 0), piece.player) &&
                            isPositionSafe(board, new Position(1, 0), piece.player)) {
                        ChessMove sideEffect = new ChessMove(new Position(0, 0), new Position(3, 0), exceptedLeftRook, null, null, Strategies.Castle, null, null);
                        result.add(new ChessMove(new Position(4, 0), new Position(2, 0), piece, null, sideEffect, Strategies.Castle, null, null));
                    }
                }
            }

        }
        if (piece.player == ChessPlayer.Black) {
            ChessPiece expectedKing = board.getLast().get(4);
            ChessPiece exceptedRightRook = board.getLast().getLast();
            ChessPiece exceptedLeftRook = board.getLast().getFirst();
            if (expectedKing != null && !expectedKing.isHasMoved()) {
                if (exceptedRightRook != null && !exceptedRightRook.isHasMoved()) {
                    if (isPositionSafe(board, new Position(5, 7), piece.player) &&
                            isPositionSafe(board, new Position(6, 7), piece.player)) {
                        ChessMove sideEffect = new ChessMove(new Position(7, 7), new Position(5, 7), exceptedRightRook, null, null, Strategies.Castle, null, null);
                        result.add(new ChessMove(new Position(4, 7), new Position(6, 7), piece, null, sideEffect, Strategies.Castle, null, null));
                    }
                }
                if (exceptedLeftRook != null && !exceptedLeftRook.isHasMoved()) {
                    if (isPositionSafe(board, new Position(3, 7), piece.player) &&
                            isPositionSafe(board, new Position(2, 7), piece.player) &&
                            isPositionSafe(board, new Position(1, 7), piece.player)) {
                        ChessMove sideEffect = new ChessMove(new Position(0, 7), new Position(3, 7), exceptedLeftRook, null, null, Strategies.Castle, null, null);
                        result.add(new ChessMove(new Position(4, 7), new Position(2, 7), piece, null, sideEffect, Strategies.Castle, null, null));
                    }
                }
            }

        }
        return result;
    }

    public boolean canAttack(ArrayList<ArrayList<ChessPiece>> board, ChessPiece attacker, Position target, ChessMove lastMove, Position position) {
        return false;
    }
}