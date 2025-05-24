package org.example.chessui.engine.MoveStrategy;

import org.example.chessui.engine.types.ChessMove;
import org.example.chessui.engine.types.ChessPiece;
import org.example.chessui.engine.types.Position;

import java.util.ArrayList;

public abstract class MoveStrategy {

    MoveStrategy() {
    }

    abstract public ArrayList<ChessMove> generatePossibleMoves(ArrayList<ArrayList<ChessPiece>> board, ChessPiece piece, ChessMove lastMove, Position position);
    abstract public boolean canAttack(ArrayList<ArrayList<ChessPiece>> board, ChessPiece attacker, Position target, ChessMove lastMove, Position position);
}
