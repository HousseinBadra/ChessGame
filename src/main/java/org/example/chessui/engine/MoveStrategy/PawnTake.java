package org.example.chessui.engine.MoveStrategy;

import org.example.chessui.engine.types.*;

import java.util.ArrayList;

public class PawnTake extends MoveStrategy {
    PawnTake() {
        super();
    }

    public ArrayList<ChessMove> generatePossibleMoves(ArrayList<ArrayList<ChessPiece>> board, ChessPiece piece, ChessMove lastMove, Position position) {
        ArrayList<ChessMove> result = new ArrayList<>();
        int[] directions = {-1, 1};
        ChessMove sideEffect = null;
        if (piece.player == ChessPlayer.White) {
            for (int direction : directions) {
                Position newPosition = new Position(position.x + direction, position.y + 1);
                if (newPosition.isValid()) {
                    ChessPiece target = board.get(newPosition.y).get(newPosition.x);
                    if (target != null && target.player != piece.player) {
                        if (position.y == 6) {
                            ChessPiece newQueen = new ChessPiece(8, piece.player, PieceType.Queen);
                            sideEffect = new ChessMove(null, null, null, piece, newQueen, null, Strategies.Promotion, newPosition, newPosition);
                        }
                        result.add(new ChessMove(position, newPosition, piece.getClone(), target, sideEffect, Strategies.PawnTake, newPosition, null));
                    }
                }
            }
        }
        if (piece.player == ChessPlayer.Black) {
            for (int direction : directions) {
                Position newPosition = new Position(position.x + direction, position.y - 1);
                if (newPosition.isValid()) {
                    ChessPiece target = board.get(newPosition.y).get(newPosition.x);
                    if (target != null && target.player != piece.player) {
                        if (position.y == 1) {
                            ChessPiece newQueen = new ChessPiece(8, piece.player,  PieceType.Queen);
                            sideEffect = new ChessMove(null, null, null, piece, newQueen, null, Strategies.Promotion, newPosition, newPosition);
                        }
                        result.add(new ChessMove(position, newPosition, piece.getClone(), target, sideEffect, Strategies.PawnTake, newPosition, null));
                    }
                }
            }
        }

        return result;
    }

    @Override
    public boolean canAttack(ArrayList<ArrayList<ChessPiece>> board, ChessPiece attacker, Position target, ChessMove lastMove, Position position) {
        ArrayList<ChessMove> moves = generatePossibleMoves(board, attacker, lastMove, position);
        for (ChessMove move : moves) {
            if (move.getTo().equals(target)) {
                return true;
            }

        }
        return false;
    }
}
