package domain;

import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;

public class Board {
    private static final int FILES = 8;
    private static final int RANKS = 8;
    private final Piece[][] squares;

    public Board() {
        squares = new Piece[RANKS][FILES];
        PieceType[] backRank = {
            PieceType.ROOK,
            PieceType.KNIGHT,
            PieceType.BISHOP,
            PieceType.QUEEN,
            PieceType.KING,
            PieceType.BISHOP,
            PieceType.KNIGHT,
            PieceType.ROOK
        };
        for (int file = 0; file < FILES; file++) {
            squares[0][file] = new Piece(backRank[file], PieceColor.WHITE);
            squares[1][file] = new Piece(PieceType.PAWN, PieceColor.WHITE);
            squares[6][file] = new Piece(PieceType.PAWN, PieceColor.BLACK);
            squares[7][file] = new Piece(backRank[file], PieceColor.BLACK);
        }
    }

    public Piece pieceAt(Location location) {
        if (location == null) {
            throw new IllegalArgumentException();
        }
        return squares[location.getRank() - 1][location.getFile()];
    }
}
