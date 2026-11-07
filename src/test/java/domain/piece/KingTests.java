package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KingTests {
    private static final Location E1 = new Location(4, 1);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final King WHITE_KING = new King(PieceColor.WHITE);

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void CanMove_OneSquareUpTheFileOntoEmptySquare_ReturnsTrue() {
        board.move(E1, E4);

        boolean canMove = WHITE_KING.canMove(E4, E5, board);

        assertTrue(canMove);
    }
}
