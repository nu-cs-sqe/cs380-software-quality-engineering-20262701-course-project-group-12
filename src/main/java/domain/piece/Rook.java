package domain.piece;

import domain.Board;
import domain.Location;

public class Rook extends Piece {
    public Rook(PieceColor color) {
        super(PieceType.ROOK, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return true;
    }
}
