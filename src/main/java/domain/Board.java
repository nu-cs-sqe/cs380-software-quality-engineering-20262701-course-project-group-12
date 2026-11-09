package domain;

import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;
import domain.piece.Rook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    private static final int FILES = Location.MAX_FILE - Location.MIN_FILE + 1;
    private static final int RANKS = Location.MAX_RANK - Location.MIN_RANK + 1;
    private static final int WHITE_BACK_RANK = Location.MIN_RANK;
    private static final int WHITE_PAWN_RANK = 2;
    private static final int BLACK_PAWN_RANK = 7;
    private static final int BLACK_BACK_RANK = Location.MAX_RANK;
    private static final List<Function<PieceColor, Piece>> BACK_RANK_PIECE_MAKERS = List.of(
            Rook::new,
            color -> new Piece(PieceType.KNIGHT, color),
            color -> new Piece(PieceType.BISHOP, color),
            color -> new Piece(PieceType.QUEEN, color),
            color -> new Piece(PieceType.KING, color),
            color -> new Piece(PieceType.BISHOP, color),
            color -> new Piece(PieceType.KNIGHT, color),
            Rook::new);

    private final List<List<Optional<Piece>>> squares = emptySquares();

    public Board() {
        for (int file = Location.MIN_FILE; file <= Location.MAX_FILE; file++) {
            placeStartingPiecesOnFile(file);
        }
    }

    private static List<List<Optional<Piece>>> emptySquares() {
        return Stream.generate(Board::emptyRank).limit(RANKS).collect(Collectors.toList());
    }

    private static List<Optional<Piece>> emptyRank() {
        return new ArrayList<>(Collections.nCopies(FILES, Optional.empty()));
    }

    private void placeStartingPiecesOnFile(int file) {
        Function<PieceColor, Piece> backRankPiece = BACK_RANK_PIECE_MAKERS.get(file);
        place(backRankPiece.apply(PieceColor.WHITE), new Location(file, WHITE_BACK_RANK));
        place(new Piece(PieceType.PAWN, PieceColor.WHITE), new Location(file, WHITE_PAWN_RANK));
        place(new Piece(PieceType.PAWN, PieceColor.BLACK), new Location(file, BLACK_PAWN_RANK));
        place(backRankPiece.apply(PieceColor.BLACK), new Location(file, BLACK_BACK_RANK));
    }

    private void place(Piece piece, Location location) {
        rankOf(location).set(location.getFile(), Optional.of(piece));
    }

    public Piece pieceAt(Location location) {
        return occupantOf(location).orElseThrow(() -> new IllegalArgumentException(
                "Cannot read a piece from the empty square at file "
                        + location.getFile() + ", rank " + location.getRank()));
    }

    public boolean isOccupied(Location location) {
        return occupantOf(location).isPresent();
    }

    public void move(Location from, Location to) {
        Piece movingPiece = pieceAt(from);
        clear(from);
        place(movingPiece, to);
    }

    private void clear(Location location) {
        rankOf(location).set(location.getFile(), Optional.empty());
    }

    private Optional<Piece> occupantOf(Location location) {
        return rankOf(location).get(location.getFile());
    }

    private List<Optional<Piece>> rankOf(Location location) {
        return squares.get(location.getRank() - Location.MIN_RANK);
    }
}
