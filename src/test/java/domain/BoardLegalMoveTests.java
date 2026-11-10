package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardLegalMoveTests {
    private static final Location B1 = new Location(1, 1);
    private static final Location C3 = new Location(2, 3);

    @Test
    public void IsLegalMove_MoveThatKeepsTheKingSafe_ReturnsTrue() {
        Board board = new Board();

        boolean legal = board.isLegalMove(B1, C3);

        assertTrue(legal);
    }
}
