package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KingCastlingTests {
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

    private void clearWhiteKingside() {
        board.move(F1, F4);
        board.move(G1, G4);
    }
}
