package domain.piece;

import domain.Board;
import domain.Location;

import java.util.stream.IntStream;

public class Piece {
    private static final int FIRST_STEP_AFTER_START = 1;

    private final PieceType type;
    private final PieceColor color;

    public Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }

    public PieceType getType() {
        return type;
    }

    public PieceColor getColor() {
        return color;
    }

    public boolean canMove(Location from, Location to, Board board) {
        return false;
    }

    protected boolean holdsOwnPiece(Location square, Board board) {
        return board.isOccupied(square) && board.pieceAt(square).getColor() == color;
    }

    protected boolean isPathClear(Location from, Location to, Board board) {
        int fileStep = Integer.signum(to.getFile() - from.getFile());
        int rankStep = Integer.signum(to.getRank() - from.getRank());
        int distance = Math.max(from.fileDistanceTo(to), from.rankDistanceTo(to));
        return IntStream.range(FIRST_STEP_AFTER_START, distance)
                .mapToObj(step -> new Location(
                        from.getFile() + step * fileStep, from.getRank() + step * rankStep))
                .noneMatch(board::isOccupied);
    }
}
