package domain.piece;

import domain.Board;
import domain.Location;

public class Knight extends Piece {
    public Knight(PieceColor color) {
        super(PieceType.KNIGHT, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return true;
    }
}
