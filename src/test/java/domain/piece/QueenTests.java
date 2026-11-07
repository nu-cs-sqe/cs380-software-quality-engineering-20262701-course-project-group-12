package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class QueenTests {
    private static final Location C4 = new Location(2, 4);
    private static final Location D1 = new Location(3, 1);
    private static final Location D4 = new Location(3, 4);
    private static final Location D5 = new Location(3, 5);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Queen WHITE_QUEEN = new Queen(PieceColor.WHITE);

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void CanMove_OneSquareUpFileOntoEmptySquare_ReturnsTrue() {
        board.move(D1, D4);

        boolean canMove = WHITE_QUEEN.canMove(D4, D5, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneSquareRightAlongRankOntoEmptySquare_ReturnsTrue() {
        board.move(D1, D4);

        boolean canMove = WHITE_QUEEN.canMove(D4, E4, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneSquareLeftAlongRankOntoEmptySquare_ReturnsTrue() {
        board.move(D1, D4);

        boolean canMove = WHITE_QUEEN.canMove(D4, C4, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneSquareUpAndRightOntoEmptySquare_ReturnsTrue() {
        board.move(D1, D4);

        boolean canMove = WHITE_QUEEN.canMove(D4, E5, board);

        assertTrue(canMove);
    }
}
