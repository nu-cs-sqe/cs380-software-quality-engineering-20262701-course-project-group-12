package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardLegalMoveTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location A3 = new Location(0, 3);
    private static final Location B1 = new Location(1, 1);
    private static final Location B5 = new Location(1, 5);
    private static final Location B8 = new Location(1, 8);
    private static final Location C3 = new Location(2, 3);
    private static final Location C6 = new Location(2, 6);
    private static final Location D4 = new Location(3, 4);
    private static final Location D6 = new Location(3, 6);
    private static final Location D7 = new Location(3, 7);
    private static final Location E2 = new Location(4, 2);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location E7 = new Location(4, 7);
    private static final Location F1 = new Location(5, 1);
    private static final Location F3 = new Location(5, 3);
    private static final Location G1 = new Location(6, 1);

    @Test
    public void IsLegalMove_MoveThatKeepsTheKingSafe_ReturnsTrue() {
        Board board = new Board();

        boolean legal = board.isLegalMove(B1, C3);

        assertTrue(legal);
    }

    @Test
    public void IsLegalMove_MoveThePieceCannotMake_ReturnsFalse() {
        Board board = new Board();

        boolean legal = board.isLegalMove(A1, A3);

        assertFalse(legal);
    }

    @Test
    public void IsLegalMove_PinnedPieceOffTheLineToItsKing_ReturnsFalse() {
        Board board = knightPinnedByBishop();

        boolean legal = board.isLegalMove(C6, D4);

        assertFalse(legal);
    }

    private Board knightPinnedByBishop() {
        Board board = new Board();
        board.move(E2, E4);
        board.move(E7, E5);
        board.move(G1, F3);
        board.move(B8, C6);
        board.move(F1, B5);
        board.move(D7, D6);
        return board;
    }
}
