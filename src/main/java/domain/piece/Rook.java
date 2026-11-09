package domain.piece;

import domain.Board;
import domain.Location;

public class Rook extends Piece {
    public Rook(PieceColor color) {
        super(PieceType.ROOK, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return isAlongRookLine(from, to)
                && !isFriendlyPiece(to, board)
                && pathIsClear(from, to, board);
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
