package domain.piece;

import domain.Board;
import domain.Location;

public class King extends Piece {
    private static final int ONE_SQUARE = 1;

    public King(PieceColor color) {
        super(PieceType.KING, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        if (isOnSameFile(from, to)) {
            return rankDistance(from, to) == ONE_SQUARE;
        }
        if (from.getRank() == to.getRank()) {
            return fileDistance(from, to) == ONE_SQUARE;
        }
        return true;
    }

    private boolean isOnSameFile(Location from, Location to) {
        return from.getFile() == to.getFile();
    }

    private int rankDistance(Location from, Location to) {
        return Math.abs(to.getRank() - from.getRank());
    }

    private int fileDistance(Location from, Location to) {
        return Math.abs(to.getFile() - from.getFile());
    }
}
