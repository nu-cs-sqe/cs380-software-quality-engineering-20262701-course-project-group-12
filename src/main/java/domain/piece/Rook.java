package domain.piece;

import domain.Board;
import domain.Location;

public class Rook extends Piece {
    public Rook(PieceColor color) {
        super(PieceType.ROOK, color);
    }

    @Override
    public boolean canMove(Location from, Location to, Board board) {
        return isAlongRookLine(from, to)
                && !holdsOwnPiece(to, board)
                && isPathClear(from, to, board);
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
}
