package engine.types;

import engine.MoveStrategy.MoveStrategy;

import java.util.ArrayList;

public class ChessPiece {
    public final int weight;
    public final ChessPlayer player;
    public final PieceType type;
    public final ArrayList<MoveStrategy> strategy;
    private int numberOfMoves = 0;

    public ChessPiece(int weight, ChessPlayer player, ArrayList<MoveStrategy> strategy, PieceType type) {
        this.weight = weight;
        this.player = player;
        this.strategy = strategy;
        this.type = type;
    }

    public ChessPiece(int weight, ChessPlayer player, ArrayList<MoveStrategy> strategy, PieceType type, int numberOfMoves) {
        this.weight = weight;
        this.player = player;
        this.strategy = strategy;
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
        return new ChessPiece(this.weight, this.player, this.strategy, this.type, this.numberOfMoves);
    }

    public ArrayList<ChessMove> generateMoves(ArrayList<ArrayList<ChessPiece>> board, ChessMove lastMove, Position position){
        ArrayList<ChessMove> result = new ArrayList<>();
        for(MoveStrategy stg : strategy) {
            result.addAll(stg.generatePossibleMoves(board, this, lastMove, position));
        }
        return result;
    }

    public boolean canAttack(ArrayList<ArrayList<ChessPiece>> board, ChessPiece attackedPiece, ChessMove lastMove, Position attackPosition, Position defensePosition) {
        for (MoveStrategy strategy : this.strategy) {
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

