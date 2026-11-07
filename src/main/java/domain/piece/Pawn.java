package domain.piece;

import domain.Board;
import domain.Location;

public class Pawn extends Piece {
    private static final int WHITE_FORWARD = 1;
    private static final int BLACK_FORWARD = -1;

    public Pawn(PieceColor color) {
        super(PieceType.PAWN, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return forwardDistance(from, to) > 0;
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
