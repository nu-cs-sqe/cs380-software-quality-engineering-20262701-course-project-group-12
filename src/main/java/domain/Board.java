package domain;

import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;

public class Board {
    private final Piece[][] squares;

    public Board() {
        squares = new Piece[8][8];
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
        for (int file = 0; file < 8; file++) {
            squares[0][file] = new Piece(backRank[file], PieceColor.WHITE);
        }
        for (int file = 0; file < 8; file++) {
            squares[1][file] = new Piece(PieceType.PAWN, PieceColor.WHITE);
        }
        squares[6][0] = new Piece(PieceType.PAWN, PieceColor.BLACK);
    }

    public Piece pieceAt(Location location) {
        return squares[location.getRank() - 1][location.getFile()];
    }
}
