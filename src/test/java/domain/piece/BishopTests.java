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
    private static final Location C8 = new Location(2, 8);
    private static final Location D2 = new Location(3, 2);
    private static final Location D4 = new Location(3, 4);
    private static final Location D5 = new Location(3, 5);
    private static final Location D7 = new Location(3, 7);
    private static final Location E5 = new Location(4, 5);
    private static final Location E7 = new Location(4, 7);
    private static final Location F8 = new Location(5, 8);

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

    @Test
    public void CanMove_BlackBishopOneSquareDownAndRightOntoEmptySquare_ReturnsTrue() {
        board.move(D7, D5);
        Bishop bishop = new Bishop(PieceColor.BLACK);

        boolean canMove = bishop.canMove(C8, D7, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackBishopOneSquareDownAndLeftOntoEmptySquare_ReturnsTrue() {
        board.move(E7, E5);
        Bishop bishop = new Bishop(PieceColor.BLACK);

        boolean canMove = bishop.canMove(F8, E7, board);

        assertTrue(canMove);
    }
}
