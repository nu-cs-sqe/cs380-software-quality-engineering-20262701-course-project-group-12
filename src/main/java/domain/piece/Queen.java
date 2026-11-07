package domain.piece;

import domain.Board;
import domain.Location;

public class Queen extends Piece {
    public Queen(PieceColor color) {
        super(PieceType.QUEEN, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return isAlongLine(from, to) && !isSameSquare(from, to);
    }

    private boolean isAlongLine(Location from, Location to) {
        int fileDistance = Math.abs(to.getFile() - from.getFile());
        int rankDistance = Math.abs(to.getRank() - from.getRank());
        return fileDistance == 0 || rankDistance == 0 || fileDistance == rankDistance;
    }

    private boolean isSameSquare(Location from, Location to) {
        return from.getFile() == to.getFile() && from.getRank() == to.getRank();
    }
}
