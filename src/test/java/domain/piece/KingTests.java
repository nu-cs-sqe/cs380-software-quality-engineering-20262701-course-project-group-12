package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KingTests {
    private static final Location D4 = new Location(3, 4);
    private static final Location E1 = new Location(4, 1);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location F4 = new Location(5, 4);
    private static final Location F5 = new Location(5, 5);
    private static final Location E7 = new Location(4, 7);
    private static final Location E8 = new Location(4, 8);
    private static final King WHITE_KING = new King(PieceColor.WHITE);
    private static final King BLACK_KING = new King(PieceColor.BLACK);

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

    @Test
    public void CanMove_BlackKingOneSquareDownTheFileOntoEmptySquare_ReturnsTrue() {
        board.move(E7, E5);

        boolean canMove = BLACK_KING.canMove(E8, E7, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneSquareTowardTheHfileOntoEmptySquare_ReturnsTrue() {
        board.move(E1, E4);

        boolean canMove = WHITE_KING.canMove(E4, F4, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneSquareTowardTheAfileOntoEmptySquare_ReturnsTrue() {
        board.move(E1, E4);

        boolean canMove = WHITE_KING.canMove(E4, D4, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneSquareUpAndRightOntoEmptySquare_ReturnsTrue() {
        board.move(E1, E4);

        boolean canMove = WHITE_KING.canMove(E4, F5, board);

        assertTrue(canMove);
    }
}
