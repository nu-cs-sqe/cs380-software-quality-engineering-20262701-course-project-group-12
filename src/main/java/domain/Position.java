package domain;

import domain.piece.PieceColor;

public class Position {
    private final PieceColor colorToMove;

    Position(PieceColor colorToMove) {
        this.colorToMove = colorToMove;
    }

    public boolean isSameAs(Position other) {
        return colorToMove == other.colorToMove;
    }
}
