package domain.piece;

import domain.Board;
import domain.Location;

public class Pawn extends Piece {
    private static final int WHITE_FORWARD = 1;
    private static final int BLACK_FORWARD = -1;
    private static final int WHITE_STARTING_RANK = 2;
    private static final int BLACK_STARTING_RANK = 7;
    private static final int ONE_SQUARE_ADVANCE = 1;
    private static final int TWO_SQUARE_ADVANCE = 2;

    private final int forwardDirection;
    private final int startingRank;

    public Pawn(PieceColor color) {
        super(PieceType.PAWN, color);
        if (color == PieceColor.WHITE) {
            forwardDirection = WHITE_FORWARD;
            startingRank = WHITE_STARTING_RANK;
        } else {
            forwardDirection = BLACK_FORWARD;
            startingRank = BLACK_STARTING_RANK;
        }
    }

    public boolean canMove(Location from, Location to, Board board) {
        return hasExpectedOccupant(from, to, board) && isAllowedAdvance(from, to, board);
    }

    private boolean hasExpectedOccupant(Location from, Location to, Board board) {
        if (isOnSameFile(from, to)) {
            return !board.isOccupied(to);
        }
        return board.isOccupied(to);
    }

    private boolean isAllowedAdvance(Location from, Location to, Board board) {
        int forwardDistance = forwardDistance(from, to);
        if (forwardDistance == TWO_SQUARE_ADVANCE) {
            return isOnStartingRank(from) && !board.isOccupied(squareInFrontOf(from));
        }
        return forwardDistance == ONE_SQUARE_ADVANCE;
    }

    private boolean isOnSameFile(Location from, Location to) {
        return from.getFile() == to.getFile();
    }

    private boolean isOnStartingRank(Location square) {
        return square.getRank() == startingRank;
    }

    private Location squareInFrontOf(Location square) {
        return new Location(square.getFile(), square.getRank() + forwardDirection);
    }

    private int forwardDistance(Location from, Location to) {
        return (to.getRank() - from.getRank()) * forwardDirection;
    }
}
