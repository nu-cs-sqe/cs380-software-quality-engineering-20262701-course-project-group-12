package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KingTests {
    private static final Location D3 = new Location(3, 3);
    private static final Location D4 = new Location(3, 4);
    private static final Location D5 = new Location(3, 5);
    private static final Location E1 = new Location(4, 1);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location E6 = new Location(4, 6);
    private static final Location F3 = new Location(5, 3);
    private static final Location F4 = new Location(5, 4);
    private static final Location F5 = new Location(5, 5);
    private static final Location F6 = new Location(5, 6);
    private static final Location G1 = new Location(6, 1);
    private static final Location G3 = new Location(6, 3);
    private static final Location G6 = new Location(6, 6);
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

    @Test
    public void CanMove_OneSquareUpAndLeftOntoEmptySquare_ReturnsTrue() {
        board.move(E1, E4);

        boolean canMove = WHITE_KING.canMove(E4, D5, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneSquareDownAndRightOntoEmptySquare_ReturnsTrue() {
        board.move(E1, E4);

        boolean canMove = WHITE_KING.canMove(E4, F3, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneSquareDownAndLeftOntoEmptySquare_ReturnsTrue() {
        board.move(E1, E4);

        boolean canMove = WHITE_KING.canMove(E4, D3, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_SameSquare_ReturnsFalse() {
        boolean canMove = WHITE_KING.canMove(E1, E1, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_TwoSquaresAlongTheFile_ReturnsFalse() {
        board.move(E1, E4);

        boolean canMove = WHITE_KING.canMove(E4, E6, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_TwoSquaresAlongTheRank_ReturnsFalse() {
        board.move(G1, G3);

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_TwoSquaresAlongDiagonal_ReturnsFalse() {
        board.move(E1, E4);

        boolean canMove = WHITE_KING.canMove(E4, G6, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_TwoSquaresInOneDirectionAndOneInTheOther_ReturnsFalse() {
        board.move(E1, E4);

        boolean canMove = WHITE_KING.canMove(E4, F6, board);

        assertFalse(canMove);
    }
}
