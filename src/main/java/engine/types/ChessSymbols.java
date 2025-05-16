package engine.types;

public class ChessSymbols {

    public static String getSymbol(PieceType type, ChessPlayer player) {
        return switch (type) {
            case King -> (player == ChessPlayer.White) ? "♔" : "♚";
            case Queen -> (player == ChessPlayer.White) ? "♕" : "♛";
            case Rook -> (player == ChessPlayer.White) ? "♖" : "♜";
            case Bishop -> (player == ChessPlayer.White) ? "♗" : "♝";
            case Knight -> (player == ChessPlayer.White) ? "♘" : "♞";
            case Pawn -> (player == ChessPlayer.White) ? "♙" : "♟";
            default -> " ";
        };
    }
}
