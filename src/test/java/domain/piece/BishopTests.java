package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BishopTests {
    private static final Location B2 = new Location(1, 2);
    private static final Location B4 = new Location(1, 4);
    private static final Location C1 = new Location(2, 1);
    private static final Location D2 = new Location(3, 2);
    private static final Location D4 = new Location(3, 4);

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void CanMove_OneSquareUpAndRightOntoEmptySquare_ReturnsTrue() {
        board.move(D2, D4);
        Bishop bishop = new Bishop(PieceColor.WHITE);

        boolean canMove = bishop.canMove(C1, D2, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneSquareUpAndLeftOntoEmptySquare_ReturnsTrue() {
        board.move(B2, B4);
        Bishop bishop = new Bishop(PieceColor.WHITE);

        boolean canMove = bishop.canMove(C1, B2, board);

        assertTrue(canMove);
    }
}
