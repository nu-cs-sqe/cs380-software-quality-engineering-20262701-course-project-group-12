package domain.piece;

import domain.Board;
import domain.Location;

public class King extends Piece {
    private static final int ONE_SQUARE = 1;
    private static final int CASTLING_DISTANCE = 2;

    public King(PieceColor color) {
        super(PieceType.KING, color);
    }

    @Override
    public boolean canMove(Location from, Location to, Board board) {
        if (holdsOwnPiece(to, board)) {
            return false;
        }
        if (isOneSquare(from, to)) {
            return true;
        }
        return isCastlingStep(from, to) && canCastle(from, to, board);
    }

    private boolean canCastle(Location from, Location to, Board board) {
        return board.holdsUnmovedPiece(from) && isPathClear(from, to, board);
    }

    private boolean isOneSquare(Location from, Location to) {
        return Math.max(from.fileDistanceTo(to), from.rankDistanceTo(to)) == ONE_SQUARE;
    }

    private boolean isCastlingStep(Location from, Location to) {
        return from.getRank() == to.getRank() && from.fileDistanceTo(to) == CASTLING_DISTANCE;
    }
}
