package domain.piece;

import domain.Board;
import domain.Location;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super(PieceType.BISHOP, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return true;
    }
}
