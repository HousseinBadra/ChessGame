package org.example.chessui.engine.MoveStrategy;

import org.example.chessui.engine.types.PieceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Singleton repository that holds precomputed strategies for each PieceType
 * using eager initialization.
 */
public class ChessStrategyRepo {
    // 1) Eagerly create the singleton instance
    private static final ChessStrategyRepo INSTANCE = new ChessStrategyRepo();

    // 2) Map of piece type → its move strategies
    private final Map<PieceType, ArrayList<MoveStrategy>> strategyMap = new HashMap<>();

    // 3) Private constructor: fill the map once
    private ChessStrategyRepo() {
        MoveStrategyFactory factory = MoveStrategyFactory.getFactory();
        for (PieceType type : PieceType.values()) {
            strategyMap.put(type, factory.getStrategy(type));
        }
    }

    /**
     * Returns the one and only instance of this repository.
     */
    public static ChessStrategyRepo getInstance() {
        return INSTANCE;
    }

    /**
     * Retrieves the move strategies for the given piece type.
     * @param pieceType the type of chess piece
     * @return a list of strategies associated with the piece
     */
    public ArrayList<MoveStrategy> getStrategies(PieceType pieceType) {
        return strategyMap.getOrDefault(pieceType, new ArrayList<>());
    }
}
