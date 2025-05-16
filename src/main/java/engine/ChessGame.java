package engine;

import engine.MoveStrategy.Strategies;
import engine.types.*;

import java.util.ArrayList;
import java.util.Random;

public class ChessGame {

    private final ArrayList<ArrayList<ChessPiece>> board;
    private final ArrayList<ChessMove> moveHistory;
    private ChessPlayer currentPlayer;
    private int totalMoves;

    public ChessGame() {
        this.board = ChessBoardFactory.createStartingBoard();
        this.moveHistory = new ArrayList<>();
        moveHistory.add(null);
        this.currentPlayer = ChessPlayer.White;
        this.totalMoves = 0;
    }

    public void applyMove(ChessMove move) {
        moveHistory.add(move);
        apply(move);
        totalMoves++;
    }

    public void apply(ChessMove move) {
        Position from = move.getFrom();
        Position to = move.getTo();

//         handle destruction
        if (move.getDestroy() != null && move.getDestroyPosition() != null) {
            board.get(move.getDestroyPosition().y).set(move.getDestroyPosition().x, null);
        }

        //handle move
        if(from != null) {
            ChessPiece piece = board.get(from.y).get(from.x);
            if(piece != null) {
                piece.moveSideEffect();
            }


            board.get(to.y).set(to.x, piece);
            board.get(from.y).set(from.x, null);
        }
        // handle creation
        if (move.getCreate() != null && move.getCreatePosition() != null) {
            board.get(move.getCreatePosition().y).set(move.getCreatePosition().x, move.getCreate());
        }

        if (move.getSideEffect() != null) {
            apply(move.getSideEffect()); // e.g., en passant or castling rook move
        }
    }

    public void undo() {
        if (moveHistory.isEmpty()) return;
        ChessMove move = moveHistory.getLast();
        moveHistory.removeLast();
        totalMoves--;
        undoMove(move);
   }

    public void undoMove(ChessMove move) {

        Position from = move.getFrom();
        Position to = move.getTo();

        if (move.getSideEffect() != null) {
            undoMove(move.getSideEffect()); // e.g., en passant or castling rook move
        }

        // handle creation
        if (move.getCreate() != null && move.getCreatePosition() != null) {
            board.get(move.getCreatePosition().y).set(move.getCreatePosition().x, null);
        }

        //handle move
        if( from != null) {
            ChessPiece piece = board.get(to.y).get(to.x);
            if(piece != null) {
                piece.undoMoveSideEffect();
            }

            board.get(to.y).set(to.x, null);
            board.get(from.y).set(from.x, piece);
        }
        // handle destruction
        if (move.getDestroy() != null && move.getDestroyPosition() != null) {
            board.get(move.getDestroyPosition().y).set(move.getDestroyPosition().x, move.getDestroy());
        }

    }

    public void togglePlayer() {
        currentPlayer = (currentPlayer == ChessPlayer.White) ? ChessPlayer.Black : ChessPlayer.White;
    }

    public ChessPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public ArrayList<ArrayList<ChessPiece>> getBoard() {
        return board;
    }

    public Position findKing(ChessPlayer kingColor) {
        Position position = null;
        for (int i = 0; i < board.size(); i++) {
            ArrayList<ChessPiece> row = board.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) != null && row.get(j).player == kingColor && row.get(j).type == PieceType.King) {
                    position = new Position(j, i);
                }
            }
        }
        return position;
    }

    public boolean isKingInCheck(ChessPlayer kingColor) {
        Position kingPosition = findKing(kingColor);
        ChessPiece king = board.get(kingPosition.y).get(kingPosition.x);
        for (int i = 0; i < board.size(); i++) {
            ArrayList<ChessPiece> row = board.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) != null) {
                    if (row.get(j).canAttack(board, king, moveHistory.getLast(), new Position(j, i), kingPosition)) {
                        return true;
                    }

                }
            }
        }
        return false;

    }

    private boolean isMoveLegal(ChessMove move, ChessPlayer player) {
        applyMove(move);
        boolean result = !isKingInCheck(player);
        undo();
        return result;
    }

    public ArrayList<ChessMove> getLegalMoves(Position position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ArrayList<ChessMove> result = new ArrayList<>();
        ChessPiece piece = board.get(position.y).get(position.x);
        if (piece == null) return moves;
        moves.addAll(piece.generateMoves(board, moveHistory.getLast(), position));
        for (ChessMove move : moves) {
            if (isMoveLegal(move, piece.player)) {
                result.add(move);
            }
        }
        return result;

    }

    public boolean isCheckmate(ChessPlayer kingColor) {
        // Stub: Add your move generator and king attack checker here
        if (!isKingInCheck(kingColor)) return false;
        for (int i = 0; i < board.size(); i++) {
            ArrayList<ChessPiece> row = board.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) != null && row.get(j).player == kingColor) {
                    if (!getLegalMoves(new Position(j, i)).isEmpty()) {
                        return false;
                    }

                }
            }
        }
        return true;
    }

    public boolean isStalemate(ChessPlayer kingColor) {
        if (isKingInCheck(kingColor)) return false;
        for (int i = 0; i < board.size(); i++) {
            ArrayList<ChessPiece> row = board.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) != null && row.get(j).player == kingColor) {
                    if (!getLegalMoves(new Position(j, i)).isEmpty()) {
                        return false;
                    }

                }
            }
        }
        return true;
    }

    public ArrayList<ChessMove> getMoveHistory() {
        return moveHistory;
    }

    ArrayList<ChessMove> generateAllMoves() {
        ArrayList<ChessMove> result = new ArrayList<>();
        for(int i=0; i< 8; i++) {
            for(int j=0; j< 8; j++){
                ChessPiece piece = board.get(i).get(j);
                if(piece != null && piece.player == getCurrentPlayer()) {
                    result.addAll(getLegalMoves(new Position(j, i)));
                }
            }
        }
        return result;
    }

    ChessMove getRandomMove() {
        ArrayList<ChessMove> possibleMoves = generateAllMoves();
        return possibleMoves.get(new Random().nextInt(possibleMoves.size()));
    }

    int miniMax(int depth, boolean maximizing) {
        if(isCheckmate(getCurrentPlayer())) return maximizing ? -999 : +999;
        if(depth == 4) return evalBoard(getCurrentPlayer());
        ArrayList<ChessMove> r = generateAllMoves();
        if(maximizing) {
            int bestScore = -9999;
            for (ChessMove chessMove : r) {
                applyMove(chessMove);
                togglePlayer();
                int score = miniMax(depth +1, false);
                if (score > bestScore) bestScore = score;
                undo();
                togglePlayer();
            }
            return bestScore;
        }
        else {
            int bestScore = 9999;
            for (ChessMove chessMove : r) {
                applyMove(chessMove);
                togglePlayer();
                int score = miniMax(depth + 1, true);
                if (score < bestScore) bestScore = score;
                undo();
                togglePlayer();
            }
            return bestScore;
        }

    }

    int evalBoard(ChessPlayer player) {
        int white = 0;
        int black = 0;
        for(int i=0; i< 8; i++) {
            for(int j=0; j< 8; j++) {
                ChessPiece piece = board.get(i).get(j);
                if(piece != null) {
                    if(piece.player == ChessPlayer.White) white+= piece.weight;
                    if(piece.player == ChessPlayer.Black) black+= piece.weight;
                }
            }
        }
        return player == ChessPlayer.White ? white - black : black - white;
    }
    public ChessMove getBestMove() {
        ArrayList<ChessMove> r = generateAllMoves();
            int bestScore = -9999;
            int bestIndex = -1;
            for (int i=0; i< r.size(); i++) {
                applyMove(r.get(i));
                togglePlayer();
                int score = miniMax(1, false);
                if (score > bestScore) {
                    bestScore = score;
                    bestIndex = i;
                }
                undo();
                togglePlayer();

        }
            if(bestIndex == -1) return null;
            return r.get(bestIndex);
    }

    public void renderBoard() {
        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + " ");
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.get(row).get(col);
                if (piece != null) {
                    System.out.print(piece.render() + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println((' '));
        }
    }



}
