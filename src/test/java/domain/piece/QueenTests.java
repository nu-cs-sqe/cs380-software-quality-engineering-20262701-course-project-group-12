package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QueenTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location A3 = new Location(0, 3);
    private static final Location B2 = new Location(1, 2);
    private static final Location B4 = new Location(1, 4);
    private static final Location C3 = new Location(2, 3);
    private static final Location C4 = new Location(2, 4);
    private static final Location C5 = new Location(2, 5);
    private static final Location D1 = new Location(3, 1);
    private static final Location D2 = new Location(3, 2);
    private static final Location D4 = new Location(3, 4);
    private static final Location D5 = new Location(3, 5);
    private static final Location D7 = new Location(3, 7);
    private static final Location D8 = new Location(3, 8);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location E6 = new Location(4, 6);
    private static final Location F5 = new Location(5, 5);
    private static final Location G5 = new Location(6, 5);
    private static final Location G7 = new Location(6, 7);
    private static final Location H3 = new Location(7, 3);
    private static final Location H6 = new Location(7, 6);
    private static final Location H8 = new Location(7, 8);
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

    @Test
    public void CanMove_BlackQueenSevenSquaresDownDiagonalOntoEmptySquare_ReturnsTrue() {
        board.move(H8, H6);
        board.move(D8, H8);
        board.move(G7, G5);
        board.move(B2, B4);
        board.move(A1, A3);

        boolean canMove = BLACK_QUEEN.canMove(H8, A1, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OpponentSevenSquaresAlongClearFile_ReturnsTrue() {
        board.move(D2, C3);
        board.move(D7, E6);

        boolean canMove = WHITE_QUEEN.canMove(D1, D8, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_SameSquare_ReturnsFalse() {
        boolean canMove = WHITE_QUEEN.canMove(D1, D1, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_KnightMoveOneFileAndTwoRanksAway_ReturnsFalse() {
        board.move(D1, D4);

        boolean canMove = WHITE_QUEEN.canMove(D4, E6, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_KnightMoveTwoFilesAndOneRankAway_ReturnsFalse() {
        board.move(D1, D4);

        boolean canMove = WHITE_QUEEN.canMove(D4, F5, board);

        assertFalse(canMove);
    }
}
