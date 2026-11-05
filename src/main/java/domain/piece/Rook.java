package domain.piece;

import domain.Board;
import domain.Location;

public class Rook extends Piece {
    public Rook(PieceColor color) {
        super(PieceType.ROOK, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        if (from == null) {
            throw new IllegalArgumentException("Cannot decide a rook move from a null square");
        }
        if (to == null) {
            throw new IllegalArgumentException("Cannot decide a rook move to a null square");
        }
        if (board == null) {
            throw new IllegalArgumentException("Cannot decide a rook move without a board");
        }
        if (board.isOccupied(to) && board.pieceAt(to).getColor() == getColor()) {
            return false;
        }
        if ((from.getFile() == to.getFile() && from.getRank() != to.getRank())
                || (from.getRank() == to.getRank() && from.getFile() != to.getFile())) {
            return pathIsClear(from, to, board);
        }
        return false;
    }

    private boolean pathIsClear(Location from, Location to, Board board) {
        int fileStep = Integer.signum(to.getFile() - from.getFile());
        int rankStep = Integer.signum(to.getRank() - from.getRank());
        int file = from.getFile() + fileStep;
        int rank = from.getRank() + rankStep;
        while (file != to.getFile() || rank != to.getRank()) {
            if (board.isOccupied(new Location(file, rank))) {
                return false;
            }
            file += fileStep;
            rank += rankStep;
        }
        return true;
    }
}
