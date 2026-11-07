package domain.piece;

import domain.Board;
import domain.Location;

public class King extends Piece {
    private static final int ONE_SQUARE = 1;

    public King(PieceColor color) {
        super(PieceType.KING, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return Math.max(fileDistance(from, to), rankDistance(from, to)) == ONE_SQUARE;
    }

    private int rankDistance(Location from, Location to) {
        return Math.abs(to.getRank() - from.getRank());
    }

    private int fileDistance(Location from, Location to) {
        return Math.abs(to.getFile() - from.getFile());
    }
}
