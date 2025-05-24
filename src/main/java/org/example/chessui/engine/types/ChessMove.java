package org.example.chessui.engine.types;

import org.example.chessui.engine.MoveStrategy.Strategies;

public class ChessMove {
    final public Position from;
    final public Position to;
    final public Position destroyPosition;
    final public Position createPosition;
    final public ChessPiece piece;
    final public ChessPiece destroy;
    final public ChessMove sideEffect;
    final public ChessPiece create;
    final public Strategies stragie;

    public Position getTo() {
        return to;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public ChessPiece getDestroy() {
        return destroy;
    }

    public Position getFrom() {
        return from;
    }

    public ChessPiece getCreate() {
        return create;
    }

    public Position getDestroyPosition() {
        return destroyPosition;
    }

    public ChessMove getSideEffect() {
        return sideEffect;
    }

    public Position getCreatePosition() {
        return createPosition;
    }

    public Strategies getStragie() {
        return stragie;
    }

    public ChessMove(Position from, Position to, ChessPiece piece, ChessPiece destroy, ChessMove sideEffect, Strategies stragie, Position destroyPosition, Position createPosition) {
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.destroy = destroy;
        this.sideEffect = sideEffect;
        this.create = null;
        this.stragie = stragie;
        this.createPosition = createPosition;
        this.destroyPosition = destroyPosition;
    }

    public ChessMove(Position from, Position to, ChessPiece piece, ChessPiece destroy, ChessPiece create, ChessMove sideEffect, Strategies stragie, Position destroyPosition, Position createPosition) {
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.destroy = destroy;
        this.create = create;
        this.sideEffect = sideEffect;
        this.stragie = stragie;
        this.createPosition = createPosition;
        this.destroyPosition = destroyPosition;
    }

    private String safeToString(ChessPiece piece) {
        return (piece != null) ? piece.render() : "null";
    }

    @Override
    public String toString() {
        return "ChessMove {\n" +
                "  from = " + from + ",\n" +
                "  to = " + to + ",\n" +
                "  piece = " + safeToString(piece) + ",\n" +
                "  destroy = " + safeToString(destroy) + ",\n" +
                "  create = " + safeToString(create) + ",\n" +
                "  destroyPosition = " + destroyPosition + ",\n" +
                "  createPosition = " + createPosition + ",\n" +
                "  strategy = " + (stragie != null ? stragie.name() : "null") + ",\n" +
                "  sideEffect = " + (sideEffect != null ? sideEffect.toString() : "null") + "\n" +
                "}";
    }

}
