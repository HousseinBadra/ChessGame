package org.example.chessui.engine.MoveStrategy;

import org.example.chessui.engine.types.ChessMove;
import org.example.chessui.engine.types.ChessPiece;
import org.example.chessui.engine.types.Position;

import java.util.ArrayList;

class Plus extends MoveStrategy {

    Plus() {
        super();
    }

    public ArrayList<ChessMove> generatePossibleMoves(ArrayList<ArrayList<ChessPiece>> board, ChessPiece piece, ChessMove lastMove, Position position) {
        ArrayList<ChessMove> result = new ArrayList<>();
        // check forward
        int counter;
        Position checkingPosition;
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        for (int[] direction : directions) {
            counter = 1;
            while (counter < 8) {
                checkingPosition = new Position(position.x + counter * direction[0], position.y + counter * direction[1]);
                if (!checkingPosition.isValid()) counter = 9;
                else if (board.get(checkingPosition.y).get(checkingPosition.x) == null) {
                    result.add(new ChessMove(position, checkingPosition, piece.getClone(), null, null, Strategies.Plus, null, null));
                } else if (board.get(checkingPosition.y).get(checkingPosition.x).player != piece.player) {
                    result.add(new ChessMove(position, checkingPosition, piece.getClone(), board.get(checkingPosition.y).get(checkingPosition.x).getClone(), null, Strategies.Plus, checkingPosition, null));
                    counter = 9;
                } else {
                    counter = 9;
                }
                counter++;
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
