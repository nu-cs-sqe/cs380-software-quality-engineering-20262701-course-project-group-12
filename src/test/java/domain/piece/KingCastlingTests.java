package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KingCastlingTests {
    private static final Location B1 = new Location(1, 1);
    private static final Location B4 = new Location(1, 4);
    private static final Location B5 = new Location(1, 5);
    private static final Location B8 = new Location(1, 8);
    private static final Location C1 = new Location(2, 1);
    private static final Location C4 = new Location(2, 4);
    private static final Location C5 = new Location(2, 5);
    private static final Location C8 = new Location(2, 8);
    private static final Location D1 = new Location(3, 1);
    private static final Location D4 = new Location(3, 4);
    private static final Location D5 = new Location(3, 5);
    private static final Location D8 = new Location(3, 8);
    private static final Location E1 = new Location(4, 1);
    private static final Location E8 = new Location(4, 8);
    private static final Location F1 = new Location(5, 1);
    private static final Location F4 = new Location(5, 4);
    private static final Location F5 = new Location(5, 5);
    private static final Location F8 = new Location(5, 8);
    private static final Location G1 = new Location(6, 1);
    private static final Location G4 = new Location(6, 4);
    private static final Location G5 = new Location(6, 5);
    private static final Location G8 = new Location(6, 8);
    private static final King WHITE_KING = new King(PieceColor.WHITE);
    private static final King BLACK_KING = new King(PieceColor.BLACK);

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void CanMove_WhiteKingside_ReturnsTrue() {
        clearWhiteKingside();

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_WhiteQueenside_ReturnsTrue() {
        clearWhiteQueenside();

        boolean canMove = WHITE_KING.canMove(E1, C1, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackKingside_ReturnsTrue() {
        clearBlackKingside();

        boolean canMove = BLACK_KING.canMove(E8, G8, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackQueenside_ReturnsTrue() {
        clearBlackQueenside();

        boolean canMove = BLACK_KING.canMove(E8, C8, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_KingMovedAndCameBack_ReturnsFalse() {
        clearWhiteKingside();
        board.move(E1, F1);
        board.move(F1, E1);

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertFalse(canMove);
    }

    private void clearWhiteKingside() {
        board.move(F1, F4);
        board.move(G1, G4);
    }

    private void clearWhiteQueenside() {
        board.move(B1, B4);
        board.move(C1, C4);
        board.move(D1, D4);
    }

    private void clearBlackKingside() {
        board.move(F8, F5);
        board.move(G8, G5);
    }

    private void clearBlackQueenside() {
        board.move(B8, B5);
        board.move(C8, C5);
        board.move(D8, D5);
    }
}
