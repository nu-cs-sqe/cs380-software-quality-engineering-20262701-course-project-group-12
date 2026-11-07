package domain.piece;

import domain.Board;
import domain.Location;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super(PieceType.BISHOP, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return isAlongDiagonal(from, to)
                && !holdsOwnPiece(to, board)
                && isPathClear(from, to, board);
    }

    private boolean isAlongDiagonal(Location from, Location to) {
        int fileDistance = fileDistance(from, to);
        int rankDistance = Math.abs(to.getRank() - from.getRank());
        return fileDistance == rankDistance && fileDistance != 0;
    }

    private boolean holdsOwnPiece(Location square, Board board) {
        return board.isOccupied(square) && board.pieceAt(square).getColor() == getColor();
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

    private int fileDistance(Location from, Location to) {
        return Math.abs(to.getFile() - from.getFile());
    }
}
