package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class QueenTests {
    private static final Location A3 = new Location(0, 3);
    private static final Location C4 = new Location(2, 4);
    private static final Location C5 = new Location(2, 5);
    private static final Location D1 = new Location(3, 1);
    private static final Location D4 = new Location(3, 4);
    private static final Location D5 = new Location(3, 5);
    private static final Location D8 = new Location(3, 8);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location H3 = new Location(7, 3);
    private static final Queen WHITE_QUEEN = new Queen(PieceColor.WHITE);
    private static final Queen BLACK_QUEEN = new Queen(PieceColor.BLACK);

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

    @Test
    public void CanMove_OneSquareUpAndLeftOntoEmptySquare_ReturnsTrue() {
        board.move(D1, D4);

        boolean canMove = WHITE_QUEEN.canMove(D4, C5, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackQueenOneSquareDownFileOntoEmptySquare_ReturnsTrue() {
        board.move(D8, D5);

        boolean canMove = BLACK_QUEEN.canMove(D5, D4, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackQueenOneSquareDownAndRightOntoEmptySquare_ReturnsTrue() {
        board.move(D8, D5);

        boolean canMove = BLACK_QUEEN.canMove(D5, E4, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackQueenOneSquareDownAndLeftOntoEmptySquare_ReturnsTrue() {
        board.move(D8, D5);

        boolean canMove = BLACK_QUEEN.canMove(D5, C4, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_SevenSquaresAlongRankOntoEmptySquare_ReturnsTrue() {
        board.move(D1, A3);

        boolean canMove = WHITE_QUEEN.canMove(A3, H3, board);

        assertTrue(canMove);
    }
}
