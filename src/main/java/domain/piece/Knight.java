package domain.piece;

import domain.Board;
import domain.Location;

public class Knight extends Piece {
    private static final int SHORT_LEG = 1;
    private static final int LONG_LEG = 2;

    public Knight(PieceColor color) {
        super(PieceType.KNIGHT, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return isKnightJump(from, to);
    }

    private boolean isKnightJump(Location from, Location to) {
        int fileDistance = Math.abs(to.getFile() - from.getFile());
        int rankDistance = Math.abs(to.getRank() - from.getRank());
        return fileDistance == SHORT_LEG && rankDistance == LONG_LEG
                || fileDistance == LONG_LEG && rankDistance == SHORT_LEG;
    }
}
