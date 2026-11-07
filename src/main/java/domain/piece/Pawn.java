package domain.piece;

import domain.Board;
import domain.Location;

public class Pawn extends Piece {
    public Pawn(PieceColor color) {
        super(PieceType.PAWN, color);
    }

    public boolean canMove(Location from, Location to, Board board) {
        return true;
    }
}
