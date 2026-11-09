package domain.piece;

import domain.Board;
import domain.Location;

public class Pawn extends Piece {
    private static final int WHITE_ONE_SQUARE_FORWARD = 1;
    private static final int WHITE_TWO_SQUARES_FORWARD = 2;
    private static final int BLACK_ONE_SQUARE_FORWARD = -1;
    private static final int BLACK_TWO_SQUARES_FORWARD = -2;
    private static final int WHITE_STARTING_RANK = 2;
    private static final int BLACK_STARTING_RANK = 7;
    private static final int CAPTURE_FILE_DISTANCE = 1;

    private final int oneSquareForward;
    private final int twoSquaresForward;
    private final int startingRank;

    public Pawn(PieceColor color) {
        super(PieceType.PAWN, color);
        if (color == PieceColor.WHITE) {
            oneSquareForward = WHITE_ONE_SQUARE_FORWARD;
            twoSquaresForward = WHITE_TWO_SQUARES_FORWARD;
            startingRank = WHITE_STARTING_RANK;
        } else {
            oneSquareForward = BLACK_ONE_SQUARE_FORWARD;
            twoSquaresForward = BLACK_TWO_SQUARES_FORWARD;
            startingRank = BLACK_STARTING_RANK;
        }
    }

    public boolean canMove(Location from, Location to, Board board) {
        if (isOnSameFile(from, to)) {
            return canAdvance(from, to, board);
        }
        return canCapture(from, to, board);
    }

    @Override
    public boolean promotesOn(Location square) {
        return true;
    }

    private boolean canAdvance(Location from, Location to, Board board) {
        if (board.isOccupied(to)) {
            return false;
        }
        int rankChange = rankChange(from, to);
        if (rankChange == twoSquaresForward) {
            return isOnStartingRank(from) && !board.isOccupied(squareInFrontOf(from));
        }
        return rankChange == oneSquareForward;
    }

    private boolean canCapture(Location from, Location to, Board board) {
        return rankChange(from, to) == oneSquareForward
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
        return new Location(square.getFile(), square.getRank() + oneSquareForward);
    }

    private int rankChange(Location from, Location to) {
        return to.getRank() - from.getRank();
    }

    private int fileDistance(Location from, Location to) {
        return Math.abs(to.getFile() - from.getFile());
    }
}
