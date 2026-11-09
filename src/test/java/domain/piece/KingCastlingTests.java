package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KingCastlingTests {
    private static final Location B1 = new Location(1, 1);
    private static final Location B4 = new Location(1, 4);
    private static final Location C1 = new Location(2, 1);
    private static final Location C4 = new Location(2, 4);
    private static final Location D1 = new Location(3, 1);
    private static final Location D4 = new Location(3, 4);
    private static final Location E1 = new Location(4, 1);
    private static final Location F1 = new Location(5, 1);
    private static final Location F4 = new Location(5, 4);
    private static final Location G1 = new Location(6, 1);
    private static final Location G4 = new Location(6, 4);
    private static final King WHITE_KING = new King(PieceColor.WHITE);

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

    private void clearWhiteKingside() {
        board.move(F1, F4);
        board.move(G1, G4);
    }

    private void clearWhiteQueenside() {
        board.move(B1, B4);
        board.move(C1, C4);
        board.move(D1, D4);
    }
}
