package domain;

import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;

public class Board {
    private final Piece[][] squares;

    public Board() {
        squares = new Piece[8][8];
        squares[0][0] = new Piece(PieceType.ROOK, PieceColor.WHITE);
        squares[0][7] = new Piece(PieceType.ROOK, PieceColor.WHITE);
        squares[0][1] = new Piece(PieceType.KNIGHT, PieceColor.WHITE);
    }

    public Piece pieceAt(Location location) {
        return squares[location.getRank() - 1][location.getFile()];
    }
}
