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

    public Pawn(PieceColor color) {
        super(PieceType.PAWN, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        int forwardDistance = forwardDistance(from, to);
        if (forwardDistance == TWO_SQUARE_ADVANCE) {
            return isOnStartingRank(from);
        }
        return forwardDistance == ONE_SQUARE_ADVANCE;
    }

    private boolean isOnStartingRank(Location square) {
        if (getColor() == PieceColor.WHITE) {
            return square.getRank() == WHITE_STARTING_RANK;
        }
        return square.getRank() == BLACK_STARTING_RANK;
    }

    private int forwardDistance(Location from, Location to) {
        return (to.getRank() - from.getRank()) * forwardDirection();
    }

    private int forwardDirection() {
        if (getColor() == PieceColor.WHITE) {
            return WHITE_FORWARD;
        }
        return BLACK_FORWARD;
    }
}
