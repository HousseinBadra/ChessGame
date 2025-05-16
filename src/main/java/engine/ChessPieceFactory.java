package engine;

import engine.MoveStrategy.MoveStrategyFactory;
import engine.types.ChessPiece;
import engine.types.ChessPlayer;
import engine.types.PieceType;

public class ChessPieceFactory {

    private static ChessPieceFactory factory = null;
    private final MoveStrategyFactory pieceStrategyFactory = MoveStrategyFactory.getFactory();

    private ChessPieceFactory() {
    }

    public static ChessPieceFactory getFactory() {
        if (factory == null) factory = new ChessPieceFactory();
        return factory;
    }

    public ChessPiece getPiece(PieceType request, ChessPlayer player) {

        if (request == PieceType.Bishop) {
            return new ChessPiece(3, player, pieceStrategyFactory.getStrategy(request), request);
        }
        if (request == PieceType.Knight) {
            return new ChessPiece(3, player, pieceStrategyFactory.getStrategy(request), request);
        }
        if (request == PieceType.Rook) {
            return new ChessPiece(5, player, pieceStrategyFactory.getStrategy(request), request);
        }
        if (request == PieceType.Queen) {
            return new ChessPiece(8, player, pieceStrategyFactory.getStrategy(request), request);
        }
        if (request == PieceType.Pawn) {
            return new ChessPiece(1, player, pieceStrategyFactory.getStrategy(request), request);
        }
        return new ChessPiece(1000, player, pieceStrategyFactory.getStrategy(request), request);

    }
}