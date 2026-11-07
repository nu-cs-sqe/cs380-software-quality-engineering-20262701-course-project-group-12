package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KnightTests {
    private static final Location C5 = new Location(2, 5);
    private static final Location D2 = new Location(3, 2);
    private static final Location D3 = new Location(3, 3);
    private static final Location D6 = new Location(3, 6);
    private static final Location E4 = new Location(4, 4);
    private static final Location F2 = new Location(5, 2);
    private static final Location F3 = new Location(5, 3);
    private static final Location F6 = new Location(5, 6);
    private static final Location G1 = new Location(6, 1);
    private static final Location G3 = new Location(6, 3);
    private static final Location G5 = new Location(6, 5);
    private static final Knight WHITE_KNIGHT = new Knight(PieceColor.WHITE);

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void CanMove_TwoUpAndOneRightOntoEmptySquare_ReturnsTrue() {
        placeWhiteKnightOnE4WithEmptyDestinations();

        boolean canMove = WHITE_KNIGHT.canMove(E4, F6, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_TwoUpAndOneLeftOntoEmptySquare_ReturnsTrue() {
        placeWhiteKnightOnE4WithEmptyDestinations();

        boolean canMove = WHITE_KNIGHT.canMove(E4, D6, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneUpAndTwoRightOntoEmptySquare_ReturnsTrue() {
        placeWhiteKnightOnE4WithEmptyDestinations();

        boolean canMove = WHITE_KNIGHT.canMove(E4, G5, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneUpAndTwoLeftOntoEmptySquare_ReturnsTrue() {
        placeWhiteKnightOnE4WithEmptyDestinations();

        boolean canMove = WHITE_KNIGHT.canMove(E4, C5, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneDownAndTwoRightOntoEmptySquare_ReturnsTrue() {
        placeWhiteKnightOnE4WithEmptyDestinations();

        boolean canMove = WHITE_KNIGHT.canMove(E4, G3, board);

        assertTrue(canMove);
    }

    private void placeWhiteKnightOnE4WithEmptyDestinations() {
        board.move(G1, E4);
        board.move(D2, D3);
        board.move(F2, F3);
    }
}
