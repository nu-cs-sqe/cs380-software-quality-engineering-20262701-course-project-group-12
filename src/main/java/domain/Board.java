package domain;

import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;

import java.util.List;

public class Board {
    private static final int FILES = 8;
    private static final int RANKS = 8;
    private static final int FIRST_RANK = 1;
    private static final int WHITE_BACK_RANK = 1;
    private static final int WHITE_PAWN_RANK = 2;
    private static final int BLACK_PAWN_RANK = 7;
    private static final int BLACK_BACK_RANK = 8;
    private static final List<PieceType> BACK_RANK_ORDER = List.of(
            PieceType.ROOK,
            PieceType.KNIGHT,
            PieceType.BISHOP,
            PieceType.QUEEN,
            PieceType.KING,
            PieceType.BISHOP,
            PieceType.KNIGHT,
            PieceType.ROOK);

    private final Piece[][] squares = new Piece[RANKS][FILES];

    public Board() {
        for (int file = 0; file < FILES; file++) {
            placeStartingPiecesOnFile(file);
        }
    }

    private void placeStartingPiecesOnFile(int file) {
        PieceType backRankType = BACK_RANK_ORDER.get(file);
        place(new Piece(backRankType, PieceColor.WHITE), new Location(file, WHITE_BACK_RANK));
        place(new Piece(PieceType.PAWN, PieceColor.WHITE), new Location(file, WHITE_PAWN_RANK));
        place(new Piece(PieceType.PAWN, PieceColor.BLACK), new Location(file, BLACK_PAWN_RANK));
        place(new Piece(backRankType, PieceColor.BLACK), new Location(file, BLACK_BACK_RANK));
    }

    private void place(Piece piece, Location location) {
        squares[rowOf(location)][location.getFile()] = piece;
    }

    public Piece pieceAt(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Cannot read a piece from a null square");
        }
        if (!isOccupied(location)) {
            throw new IllegalArgumentException("Cannot read a piece from the empty square at file "
                    + location.getFile() + ", rank " + location.getRank());
        }
        return squares[rowOf(location)][location.getFile()];
    }

    public boolean isOccupied(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Cannot check whether a null square is occupied");
        }
        return squares[rowOf(location)][location.getFile()] != null;
    }

    public void move(Location from, Location to) {
        if (to == null) {
            throw new IllegalArgumentException("Cannot move a piece to a null square");
        }
        Piece movingPiece = pieceAt(from);
        clear(from);
        place(movingPiece, to);
    }

    private void clear(Location location) {
        squares[rowOf(location)][location.getFile()] = null;
    }

    private int rowOf(Location location) {
        return location.getRank() - FIRST_RANK;
    }
}
