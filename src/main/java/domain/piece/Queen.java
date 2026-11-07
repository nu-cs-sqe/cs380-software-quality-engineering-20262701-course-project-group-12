package domain.piece;

import domain.Board;
import domain.Location;

public class Queen extends Piece {
    public Queen(PieceColor color) {
        super(PieceType.QUEEN, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return true;
    }
}
