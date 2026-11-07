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
    private static final int CAPTURE_FILE_DISTANCE = 1;

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
        if (isOnSameFile(from, to)) {
            return canAdvance(from, to, board);
        }
        return canCapture(from, to, board);
    }

    private boolean canAdvance(Location from, Location to, Board board) {
        if (board.isOccupied(to)) {
            return false;
        }
        int forwardDistance = forwardDistance(from, to);
        if (forwardDistance == TWO_SQUARE_ADVANCE) {
            return isOnStartingRank(from) && !board.isOccupied(squareInFrontOf(from));
        }
        return forwardDistance == ONE_SQUARE_ADVANCE;
    }

    private boolean canCapture(Location from, Location to, Board board) {
        return forwardDistance(from, to) == ONE_SQUARE_ADVANCE
                && fileDistance(from, to) == CAPTURE_FILE_DISTANCE
                && holdsOpponent(to, board);
    }

    private boolean holdsOpponent(Location square, Board board) {
        return board.isOccupied(square) && board.pieceAt(square).getColor() != getColor();
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

    private int fileDistance(Location from, Location to) {
        return Math.abs(to.getFile() - from.getFile());
    }
}
