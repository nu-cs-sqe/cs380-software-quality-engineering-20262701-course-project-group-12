package domain;

import domain.piece.Bishop;
import domain.piece.King;
import domain.piece.Knight;
import domain.piece.Pawn;
import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;
import domain.piece.Queen;
import domain.piece.Rook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Board {
    private static final int FILES = Location.MAX_FILE - Location.MIN_FILE + 1;
    private static final int RANKS = Location.MAX_RANK - Location.MIN_RANK + 1;
    private static final int WHITE_BACK_RANK = Location.MIN_RANK;
    private static final int WHITE_PAWN_RANK = 2;
    private static final int BLACK_PAWN_RANK = 7;
    private static final int BLACK_BACK_RANK = Location.MAX_RANK;
    private static final int TWO_RANKS = 2;
    private static final int ADJACENT_FILE_DISTANCE = 1;
    private static final List<Function<PieceColor, Piece>> BACK_RANK_PIECE_MAKERS = List.of(
            Rook::new,
            Knight::new,
            Bishop::new,
            Queen::new,
            King::new,
            Bishop::new,
            Knight::new,
            Rook::new);
    private static final Map<PieceType, Function<PieceColor, Piece>> PROMOTION_PIECE_MAKERS =
            Map.of(
                    PieceType.QUEEN, Queen::new,
                    PieceType.ROOK, Rook::new,
                    PieceType.BISHOP, Bishop::new,
                    PieceType.KNIGHT, Knight::new);

    private final List<List<Optional<Piece>>> squares = emptySquares();
    private Optional<Location> pawnThatJustAdvancedTwoRanks = Optional.empty();

    public Board() {
        for (int file = Location.MIN_FILE; file <= Location.MAX_FILE; file++) {
            placeStartingPiecesOnFile(file);
        }
    }

    private Board(Board original) {
        allSquares().forEach(square ->
                rankOf(square).set(square.getFile(), original.occupantOf(square)));
        pawnThatJustAdvancedTwoRanks = original.pawnThatJustAdvancedTwoRanks;
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
        place(new Pawn(PieceColor.WHITE), new Location(file, WHITE_PAWN_RANK));
        place(new Pawn(PieceColor.BLACK), new Location(file, BLACK_PAWN_RANK));
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
        removePawnCapturedEnPassant(movingPiece, from, to);
        place(movingPiece, to);
        pawnThatJustAdvancedTwoRanks = isPawnAdvancingTwoRanks(movingPiece, from, to)
                ? Optional.of(to)
                : Optional.empty();
    }

    public boolean canBeCapturedEnPassant(Location square) {
        return pawnThatJustAdvancedTwoRanks.filter(square::isSameSquareAs).isPresent();
    }

    private void removePawnCapturedEnPassant(Piece movingPiece, Location from, Location to) {
        Location besideFrom = new Location(to.getFile(), from.getRank());
        if (isPawn(movingPiece) && canBeCapturedEnPassant(besideFrom)) {
            clear(besideFrom);
        }
    }

    private boolean isPawnAdvancingTwoRanks(Piece piece, Location from, Location to) {
        return isPawn(piece) && Math.abs(to.getRank() - from.getRank()) == TWO_RANKS;
    }

    private boolean isPawn(Piece piece) {
        return piece.getType() == PieceType.PAWN;
    }

    public void promote(Location square, PieceType type) {
        if (!PROMOTION_PIECE_MAKERS.containsKey(type)) {
            throw new IllegalArgumentException("Cannot promote a pawn to a " + type
                    + "; the choices are a queen, rook, bishop, or knight");
        }
        PieceColor pawnColor = pieceAt(square).getColor();
        place(PROMOTION_PIECE_MAKERS.get(type).apply(pawnColor), square);
    }

    public boolean isInCheck(PieceColor color) {
        Location kingSquare = kingSquareOf(color);
        return occupiedSquares()
                .anyMatch(square -> pieceAt(square).canMove(square, kingSquare, this));
    }

    private Location kingSquareOf(PieceColor color) {
        return occupiedSquares()
                .filter(square -> isKingOf(color, pieceAt(square)))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Cannot look for check: the board has no " + color + " king"));
    }

    private boolean isKingOf(PieceColor color, Piece piece) {
        return piece.getType() == PieceType.KING && piece.getColor() == color;
    }

    private Stream<Location> occupiedSquares() {
        return allSquares().filter(this::isOccupied);
    }

    private static Stream<Location> allSquares() {
        return IntStream.rangeClosed(Location.MIN_FILE, Location.MAX_FILE).boxed()
                .flatMap(file -> IntStream.rangeClosed(Location.MIN_RANK, Location.MAX_RANK)
                        .mapToObj(rank -> new Location(file, rank)));
    }

    public boolean isCheckmated(PieceColor color) {
        return isInCheck(color) && !hasLegalMove(color);
    }

    public boolean isDeadPosition() {
        return false;
    }

    private boolean hasLegalMove(PieceColor color) {
        return occupiedSquares()
                .filter(from -> pieceAt(from).getColor() == color)
                .anyMatch(this::hasLegalMoveFrom);
    }

    private boolean hasLegalMoveFrom(Location from) {
        return allSquares().anyMatch(to -> isLegalMove(from, to));
    }

    public boolean isLegalMove(Location from, Location to) {
        Piece movingPiece = pieceAt(from);
        return movingPiece.canMove(from, to, this)
                && !boardAfterMove(from, to).isInCheck(movingPiece.getColor());
    }

    public Position position(PieceColor colorToMove) {
        return new Position(colorToMove,
                allSquares().map(this::occupantOf).collect(Collectors.toUnmodifiableList()),
                isEnPassantCaptureAvailableTo(colorToMove));
    }

    public Position positionAfterMove(Location from, Location to, PieceColor colorToMove) {
        return boardAfterMove(from, to).position(colorToMove);
    }

    private boolean isEnPassantCaptureAvailableTo(PieceColor color) {
        return pawnThatJustAdvancedTwoRanks
                .filter(square -> hasPieceOfColorBeside(color, square))
                .isPresent();
    }

    private boolean hasPieceOfColorBeside(PieceColor color, Location square) {
        return occupiedSquares().anyMatch(other ->
                isBeside(other, square) && pieceAt(other).getColor() == color);
    }

    private static boolean isBeside(Location square, Location other) {
        return square.getRank() == other.getRank()
                && square.fileDistanceTo(other) == ADJACENT_FILE_DISTANCE;
    }

    private Board boardAfterMove(Location from, Location to) {
        Board copy = new Board(this);
        copy.move(from, to);
        return copy;
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
