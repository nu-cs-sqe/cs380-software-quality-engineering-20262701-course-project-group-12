package domain.piece;

import domain.Board;
import domain.Location;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super(PieceType.BISHOP, color);
    }

    @Override
    public boolean canMove(Location from, Location to, Board board) {
        return isAlongDiagonal(from, to)
                && !holdsOwnPiece(to, board)
                && isPathClear(from, to, board);
    }

    private boolean isAlongDiagonal(Location from, Location to) {
        int fileDistance = from.fileDistanceTo(to);
        int rankDistance = from.rankDistanceTo(to);
        return fileDistance == rankDistance && fileDistance != 0;
    }

    private boolean isPathClear(Location from, Location to, Board board) {
        int fileStep = Integer.signum(to.getFile() - from.getFile());
        int rankStep = Integer.signum(to.getRank() - from.getRank());
        int file = from.getFile() + fileStep;
        int rank = from.getRank() + rankStep;
        while (file != to.getFile()) {
            if (board.isOccupied(new Location(file, rank))) {
                return false;
            }
            file += fileStep;
            rank += rankStep;
        }
        return true;
    }
}
