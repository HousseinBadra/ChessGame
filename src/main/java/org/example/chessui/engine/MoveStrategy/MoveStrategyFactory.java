package org.example.chessui.engine.MoveStrategy;

import org.example.chessui.engine.types.PieceType;

import java.util.ArrayList;

public class MoveStrategyFactory {
    private static MoveStrategyFactory factory = null;
    L LStrategy = new L();
    X XStrategy = new X();
    EnPassant EnPassantStrategy = new EnPassant();
    Plus PlusStrategy = new Plus();
    PawnMove PawnMoveStrategy = new PawnMove();
    DoublePawnMove DoublePawnMoveStrategy = new DoublePawnMove();
    PawnTake PawnTakeStrategy = new PawnTake();
    KingMove KingMoveStrategy = new KingMove();
    Castle CastleStrategy = new Castle();

    private MoveStrategyFactory() {
    }

    public static MoveStrategyFactory getFactory() {
        if (factory == null) factory = new MoveStrategyFactory();
        return factory;
    }

    public ArrayList<MoveStrategy> getStrategy(PieceType request) {
        ArrayList<MoveStrategy> result = new ArrayList<>();
        if (request == PieceType.Bishop) {
            result.add(XStrategy);
        }
        if (request == PieceType.Knight) {
            result.add(LStrategy);
        }
        if (request == PieceType.Rook) {
            result.add(PlusStrategy);
        }
        if (request == PieceType.Queen) {
            result.add(PlusStrategy);
            result.add(XStrategy);
        }
        if (request == PieceType.Pawn) {
            result.add(EnPassantStrategy);
            result.add(PawnMoveStrategy);
            result.add(DoublePawnMoveStrategy);
            result.add(PawnTakeStrategy);
        }
        if (request == PieceType.King) {
            result.add(KingMoveStrategy);
            result.add(CastleStrategy);
        }

        return result;
    }
}
