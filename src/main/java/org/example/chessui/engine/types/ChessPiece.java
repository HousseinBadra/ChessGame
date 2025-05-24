package org.example.chessui.engine.types;

import org.example.chessui.engine.MoveStrategy.ChessStrategyRepo;
import org.example.chessui.engine.MoveStrategy.MoveStrategy;

import java.util.ArrayList;

public class ChessPiece {
    public final int weight;
    public final ChessPlayer player;
    public final PieceType type;
    public int numberOfMoves = 0;

    public ChessPiece(int weight, ChessPlayer player, PieceType type) {
        this.weight = weight;
        this.player = player;
        this.type = type;
    }

    public ChessPiece(int weight, ChessPlayer player, PieceType type, int numberOfMoves) {
        this.weight = weight;
        this.player = player;
        this.type = type;
        this.numberOfMoves = numberOfMoves;
    }

    public void moveSideEffect() {
        numberOfMoves++;
    }

    public void undoMoveSideEffect() {
        numberOfMoves--;
    }

    public boolean isHasMoved() {
        return this.numberOfMoves != 0;
    }

    public ChessPiece getClone() {
        return new ChessPiece(this.weight, this.player, this.type, this.numberOfMoves);
    }

    public ArrayList<MoveStrategy> getStrategy() {
        return ChessStrategyRepo.getInstance().getStrategies(this.type);
    }

    public ArrayList<ChessMove> generateMoves(ArrayList<ArrayList<ChessPiece>> board, ChessMove lastMove, Position position){
        ArrayList<ChessMove> result = new ArrayList<>();
        for(MoveStrategy stg : this.getStrategy()) {
            result.addAll(stg.generatePossibleMoves(board, this, lastMove, position));
        }
        return result;
    }

    public boolean canAttack(ArrayList<ArrayList<ChessPiece>> board, ChessPiece attackedPiece, ChessMove lastMove, Position attackPosition, Position defensePosition) {
        for (MoveStrategy strategy : this.getStrategy()) {
            if (this.player == attackedPiece.player) return false;
            if (strategy.canAttack(board, this, defensePosition, lastMove, attackPosition)) {
                return true;
            }
        }
        return false;
    }

    public String render() {
        return ChessSymbols.getSymbol(type, player);
    }
}

