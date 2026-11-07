package domain;

import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;

import java.util.List;

public class Board {
    private static final int FILES = Location.MAX_FILE - Location.MIN_FILE + 1;
    private static final int RANKS = Location.MAX_RANK - Location.MIN_RANK + 1;
    private static final int WHITE_BACK_RANK = Location.MIN_RANK;
    private static final int WHITE_PAWN_RANK = 2;
    private static final int BLACK_PAWN_RANK = 7;
    private static final int BLACK_BACK_RANK = Location.MAX_RANK;
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
        for (int file = Location.MIN_FILE; file <= Location.MAX_FILE; file++) {
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
        if (!isOccupied(location)) {
            throw new IllegalArgumentException("Cannot read a piece from the empty square at file "
                    + location.getFile() + ", rank " + location.getRank());
        }
        return squares[rowOf(location)][location.getFile()];
    }

    public boolean isOccupied(Location location) {
        return squares[rowOf(location)][location.getFile()] != null;
    }

    public void move(Location from, Location to) {
        Piece movingPiece = pieceAt(from);
        clear(from);
        place(movingPiece, to);
    }

    private void clear(Location location) {
        squares[rowOf(location)][location.getFile()] = null;
    }

    private int rowOf(Location location) {
        return location.getRank() - Location.MIN_RANK;
    }
}
