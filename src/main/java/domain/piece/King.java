package domain.piece;

import domain.Board;
import domain.Location;

public class King extends Piece {
    private static final int ONE_SQUARE = 1;

    public King(PieceColor color) {
        super(PieceType.KING, color);
    }

    @Override
    public boolean canMove(Location from, Location to, Board board) {
        if (holdsOwnPiece(to, board)) {
            return false;
        }
        return Math.max(from.fileDistanceTo(to), from.rankDistanceTo(to)) == ONE_SQUARE;
    }
}
