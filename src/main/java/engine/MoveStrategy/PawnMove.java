package engine.MoveStrategy;

import engine.types.*;

import java.util.ArrayList;

class PawnMove extends MoveStrategy {

    PawnMove() {
        super();
    }

    public ArrayList<ChessMove> generatePossibleMoves(ArrayList<ArrayList<ChessPiece>> board, ChessPiece piece, ChessMove lastMove, Position position) {
        ArrayList<ChessMove> result = new ArrayList<>();
        // check white
        ChessMove sideEffect = null;
        if (piece.player == ChessPlayer.White) {
            if (board.get(position.y + 1).get(position.x) == null) {
                Position newPosition = new Position(position.x, position.y + 1);
                if (position.y == 6) {
                    ChessPiece newQueen = new ChessPiece(8, piece.player, MoveStrategyFactory.getFactory().getStrategy(PieceType.Queen), PieceType.Queen);
                    sideEffect = new ChessMove(null, null, null, piece, newQueen, null, Strategies.Promotion, newPosition, newPosition);
                }
                result.add(new ChessMove(position, newPosition, piece.getClone(), null, sideEffect, Strategies.PawnMove, null, null));
            }
        }
        if (piece.player == ChessPlayer.Black) {
            if (board.get(position.y - 1).get(position.x) == null) {
                Position newPosition = new Position(position.x, position.y - 1);
                if (position.y == 1) {
                    ChessPiece newQueen = new ChessPiece(8, piece.player, MoveStrategyFactory.getFactory().getStrategy(PieceType.Queen), PieceType.Queen);
                    sideEffect = new ChessMove(null, null, null, piece, newQueen, null, Strategies.Promotion, newPosition, newPosition);
                }
                result.add(new ChessMove(position, newPosition, piece.getClone(), null, sideEffect, Strategies.PawnMove, null, null));
            }
        }
        return result;
    }

    @Override
    public boolean canAttack(ArrayList<ArrayList<ChessPiece>> board, ChessPiece attacker, Position target, ChessMove lastMove, Position position) {
        return false;
    }
}
