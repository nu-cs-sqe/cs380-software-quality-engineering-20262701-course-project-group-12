package domain.piece;

import domain.Board;
import domain.Location;

public class Rook extends Piece {
    public Rook(PieceColor color) {
        super(PieceType.ROOK, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        requireMoveArguments(from, to, board);
        if (isFriendlyPiece(to, board)) {
            return false;
        }
        return isAlongRookLine(from, to) && pathIsClear(from, to, board);
    }

    private void requireMoveArguments(Location from, Location to, Board board) {
        if (from == null) {
            throw new IllegalArgumentException("Cannot decide a rook move from a null square");
        }
        if (to == null) {
            throw new IllegalArgumentException("Cannot decide a rook move to a null square");
        }
        if (board == null) {
            throw new IllegalArgumentException("Cannot decide a rook move without a board");
        }
    }

    private boolean isFriendlyPiece(Location destination, Board board) {
        return board.isOccupied(destination)
                && board.pieceAt(destination).getColor() == getColor();
    }

    private boolean isAlongRookLine(Location from, Location to) {
        return isAlongFile(from, to) || isAlongRank(from, to);
    }

    private boolean isAlongFile(Location from, Location to) {
        return from.getFile() == to.getFile() && from.getRank() != to.getRank();
    }

    private boolean isAlongRank(Location from, Location to) {
        return from.getRank() == to.getRank() && from.getFile() != to.getFile();
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
