package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PawnTests {
    private static final Location A2 = new Location(0, 2);
    private static final Location B3 = new Location(1, 3);
    private static final Location B7 = new Location(1, 7);
    private static final Location E2 = new Location(4, 2);
    private static final Location E3 = new Location(4, 3);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location E6 = new Location(4, 6);
    private static final Location E7 = new Location(4, 7);
    private static final Location G3 = new Location(6, 3);
    private static final Location G7 = new Location(6, 7);
    private static final Location H2 = new Location(7, 2);
    private static final Pawn WHITE_PAWN = new Pawn(PieceColor.WHITE);
    private static final Pawn BLACK_PAWN = new Pawn(PieceColor.BLACK);

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void CanMove_WhitePawnOneSquareForwardOntoEmptySquare_ReturnsTrue() {
        boolean canMove = WHITE_PAWN.canMove(E2, E3, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_WhitePawnTwoSquaresForwardFromStartingRank_ReturnsTrue() {
        boolean canMove = WHITE_PAWN.canMove(E2, E4, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackPawnOneSquareForwardOntoEmptySquare_ReturnsTrue() {
        boolean canMove = BLACK_PAWN.canMove(E7, E6, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackPawnTwoSquaresForwardFromStartingRank_ReturnsTrue() {
        boolean canMove = BLACK_PAWN.canMove(E7, E5, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_CaptureTowardHigherFileFromAFile_ReturnsTrue() {
        board.move(B7, B3);

        boolean canMove = WHITE_PAWN.canMove(A2, B3, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_CaptureTowardLowerFileFromHFile_ReturnsTrue() {
        board.move(G7, G3);

        boolean canMove = WHITE_PAWN.canMove(H2, G3, board);

        assertTrue(canMove);
    }
}
