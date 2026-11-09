package domain.piece;

import domain.Board;
import domain.Location;

public class Queen extends Piece {
    public Queen(PieceColor color) {
        super(PieceType.QUEEN, color);
    }

    @Override
    public boolean canMove(Location from, Location to, Board board) {
        return isAlongLine(from, to)
                && !isSameSquare(from, to)
                && !holdsOwnPiece(to, board)
                && isPathClear(from, to, board);
    }

    private boolean isAlongLine(Location from, Location to) {
        int fileDistance = from.fileDistanceTo(to);
        int rankDistance = from.rankDistanceTo(to);
        return fileDistance == 0 || rankDistance == 0 || fileDistance == rankDistance;
    }

    private boolean isSameSquare(Location from, Location to) {
        return from.getFile() == to.getFile() && from.getRank() == to.getRank();
    }
}
