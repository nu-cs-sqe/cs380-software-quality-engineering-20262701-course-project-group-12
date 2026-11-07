package domain.piece;

import domain.Board;
import domain.Location;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super(PieceType.BISHOP, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return !isOnSameFile(from, to) && !isOnSameRank(from, to);
    }

    private boolean isOnSameFile(Location from, Location to) {
        return from.getFile() == to.getFile();
    }

    private boolean isOnSameRank(Location from, Location to) {
        return from.getRank() == to.getRank();
    }
}
