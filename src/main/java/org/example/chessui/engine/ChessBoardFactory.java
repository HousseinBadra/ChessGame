package org.example.chessui.engine;

import java.util.ArrayList;
import org.example.chessui.engine.types.ChessPiece;
import org.example.chessui.engine.types.ChessPlayer;
import org.example.chessui.engine.types.PieceType;

public class ChessBoardFactory {

    public static ArrayList<ArrayList<ChessPiece>> createStartingBoard() {
        ChessPieceFactory pieceFactory = ChessPieceFactory.getFactory();
        ArrayList<ArrayList<ChessPiece>> board = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            board.add(new ArrayList<>());
        }

        // Row 0: White major pieces
        board.getFirst().add(pieceFactory.getPiece(PieceType.Rook, ChessPlayer.White));
        board.getFirst().add(pieceFactory.getPiece(PieceType.Knight, ChessPlayer.White));
        board.getFirst().add(pieceFactory.getPiece(PieceType.Bishop, ChessPlayer.White));
        board.getFirst().add(pieceFactory.getPiece(PieceType.Queen, ChessPlayer.White));
        board.getFirst().add(pieceFactory.getPiece(PieceType.King, ChessPlayer.White));
        board.getFirst().add(pieceFactory.getPiece(PieceType.Bishop, ChessPlayer.White));
        board.getFirst().add(pieceFactory.getPiece(PieceType.Knight, ChessPlayer.White));
        board.getFirst().add(pieceFactory.getPiece(PieceType.Rook, ChessPlayer.White));

        // Row 1: White pawns
        for (int i = 0; i < 8; i++) {
            board.get(1).add(pieceFactory.getPiece(PieceType.Pawn, ChessPlayer.White));
        }

        // Rows 2 to 5: Empty squares
        for (int i = 2; i <= 5; i++) {
            for (int j = 0; j < 8; j++) {
                board.get(i).add(null);
            }
        }

        // Row 6: Black pawns
        for (int i = 0; i < 8; i++) {
            board.get(6).add(pieceFactory.getPiece(PieceType.Pawn, ChessPlayer.Black));
        }

        // Row 7: Black major pieces
        board.getLast().add(pieceFactory.getPiece(PieceType.Rook, ChessPlayer.Black));
        board.getLast().add(pieceFactory.getPiece(PieceType.Knight, ChessPlayer.Black));
        board.getLast().add(pieceFactory.getPiece(PieceType.Bishop, ChessPlayer.Black));
        board.getLast().add(pieceFactory.getPiece(PieceType.Queen, ChessPlayer.Black));
        board.getLast().add(pieceFactory.getPiece(PieceType.King, ChessPlayer.Black));
        board.getLast().add(pieceFactory.getPiece(PieceType.Bishop, ChessPlayer.Black));
        board.getLast().add(pieceFactory.getPiece(PieceType.Knight, ChessPlayer.Black));
        board.getLast().add(pieceFactory.getPiece(PieceType.Rook, ChessPlayer.Black));

        return board;
    }
}
