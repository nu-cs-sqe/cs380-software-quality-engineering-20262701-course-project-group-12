package domain;

import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionTests {
    @Test
    public void IsSameAs_SamePiecesAndPlayerToMove_ReturnsTrue() {
        Position position = new Board().position(PieceColor.WHITE);
        Position other = new Board().position(PieceColor.WHITE);

        boolean same = position.isSameAs(other);

        assertTrue(same);
    }
}
