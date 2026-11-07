package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KnightTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location B1 = new Location(1, 1);
    private static final Location B3 = new Location(1, 3);
    private static final Location C2 = new Location(2, 2);
    private static final Location C3 = new Location(2, 3);
    private static final Location C4 = new Location(2, 4);
    private static final Location C5 = new Location(2, 5);
    private static final Location D2 = new Location(3, 2);
    private static final Location D3 = new Location(3, 3);
    private static final Location D6 = new Location(3, 6);
    private static final Location E4 = new Location(4, 4);
    private static final Location F2 = new Location(5, 2);
    private static final Location F3 = new Location(5, 3);
    private static final Location F5 = new Location(5, 5);
    private static final Location F6 = new Location(5, 6);
    private static final Location F7 = new Location(5, 7);
    private static final Location G1 = new Location(6, 1);
    private static final Location G3 = new Location(6, 3);
    private static final Location G5 = new Location(6, 5);
    private static final Location G6 = new Location(6, 6);
    private static final Location G8 = new Location(6, 8);
    private static final Location H8 = new Location(7, 8);
    private static final Knight WHITE_KNIGHT = new Knight(PieceColor.WHITE);
    private static final Knight BLACK_KNIGHT = new Knight(PieceColor.BLACK);

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

    @Test
    public void CanMove_OneDownAndTwoLeftOntoEmptySquare_ReturnsTrue() {
        placeWhiteKnightOnE4WithEmptyDestinations();

        boolean canMove = WHITE_KNIGHT.canMove(E4, C3, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_TwoDownAndOneRightOntoEmptySquare_ReturnsTrue() {
        placeWhiteKnightOnE4WithEmptyDestinations();

        boolean canMove = WHITE_KNIGHT.canMove(E4, F2, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_TwoDownAndOneLeftOntoEmptySquare_ReturnsTrue() {
        placeWhiteKnightOnE4WithEmptyDestinations();

        boolean canMove = WHITE_KNIGHT.canMove(E4, D2, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_LowestCornerTwoUpAndOneRight_ReturnsTrue() {
        board.move(B1, A1);

        boolean canMove = WHITE_KNIGHT.canMove(A1, B3, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_LowestCornerOneUpAndTwoRight_ReturnsTrue() {
        board.move(B1, A1);
        board.move(C2, C4);

        boolean canMove = WHITE_KNIGHT.canMove(A1, C2, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackKnightHighestCornerTwoDownAndOneLeft_ReturnsTrue() {
        board.move(G8, H8);

        boolean canMove = BLACK_KNIGHT.canMove(H8, G6, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackKnightHighestCornerOneDownAndTwoLeft_ReturnsTrue() {
        board.move(G8, H8);
        board.move(F7, F5);

        boolean canMove = BLACK_KNIGHT.canMove(H8, F7, board);

        assertTrue(canMove);
    }

    private void placeWhiteKnightOnE4WithEmptyDestinations() {
        board.move(G1, E4);
        board.move(D2, D3);
        board.move(F2, F3);
    }
}
