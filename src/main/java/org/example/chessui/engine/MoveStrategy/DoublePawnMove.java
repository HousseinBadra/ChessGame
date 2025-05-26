package org.example.chessui.engine.MoveStrategy;

import org.example.chessui.engine.types.ChessMove;
import org.example.chessui.engine.types.ChessPiece;
import org.example.chessui.engine.types.ChessPlayer;
import org.example.chessui.engine.types.Position;

import java.util.ArrayList;

public class DoublePawnMove extends MoveStrategy {

    DoublePawnMove() {
        super();
    }

    public ArrayList<ChessMove> generatePossibleMoves(ArrayList<ArrayList<ChessPiece>> board, ChessPiece piece, ChessMove lastMove, Position position) {
        ArrayList<ChessMove> result = new ArrayList<>();
        if(piece.isHasMoved()) return result;
        // check white
        if (piece.player == ChessPlayer.White && position.y == 1) {
            if (board.get(position.y + 1).get(position.x) == null && board.get(position.y + 2).get(position.x) == null) {
                Position newPosition = new Position(position.x, position.y + 2);
                result.add(new ChessMove(position, newPosition, piece.getClone(), null, null, Strategies.DoublePawnMove, null, null));
            }
        }
        if (piece.player == ChessPlayer.Black && position.y == 6) {
            if (board.get(position.y - 1).get(position.x) == null && board.get(position.y - 2).get(position.x) == null) {
                Position newPosition = new Position(position.x, position.y - 2);
                result.add(new ChessMove(position, newPosition, piece.getClone(), null, null, Strategies.DoublePawnMove, null, null));
            }
        }
        return result;
    }

    @Override
    public boolean canAttack(ArrayList<ArrayList<ChessPiece>> board, ChessPiece attacker, Position target, ChessMove lastMove, Position position) {
        return false;
    }
}
