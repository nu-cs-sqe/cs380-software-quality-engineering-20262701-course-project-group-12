package domain;

import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionTests {
    private static final Location A3 = new Location(0, 3);
    private static final Location B1 = new Location(1, 1);
    private static final Location F3 = new Location(5, 3);
    private static final Location G1 = new Location(6, 1);

    @Test
    public void IsSameAs_SamePiecesAndPlayerToMove_ReturnsTrue() {
        Position position = new Board().position(PieceColor.WHITE);
        Position other = new Board().position(PieceColor.WHITE);

        boolean same = position.isSameAs(other);

        assertTrue(same);
    }

    @Test
    public void IsSameAs_AnotherPlayerToMove_ReturnsFalse() {
        Position position = new Board().position(PieceColor.WHITE);
        Position other = new Board().position(PieceColor.BLACK);

        boolean same = position.isSameAs(other);

        assertFalse(same);
    }

    @Test
    public void IsSameAs_PieceOnAnotherSquare_ReturnsFalse() {
        Board board = new Board();
        board.move(G1, F3);
        Position position = new Board().position(PieceColor.WHITE);

        boolean same = position.isSameAs(board.position(PieceColor.WHITE));

        assertFalse(same);
    }

    @Test
    public void IsSameAs_KnightsOfOneColorSwapSquares_ReturnsTrue() {
        Board board = new Board();
        board.move(B1, A3);
        board.move(G1, B1);
        board.move(A3, G1);
        Position position = new Board().position(PieceColor.WHITE);

        boolean same = position.isSameAs(board.position(PieceColor.WHITE));

        assertTrue(same);
    }
}
