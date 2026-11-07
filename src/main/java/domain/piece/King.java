package domain.piece;

import domain.Board;
import domain.Location;

public class King extends Piece {
    public King(PieceColor color) {
        super(PieceType.KING, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return true;
    }
}
