package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PawnTests {
    private static final Location E2 = new Location(4, 2);
    private static final Location E3 = new Location(4, 3);
    private static final Location E4 = new Location(4, 4);
    private static final Pawn WHITE_PAWN = new Pawn(PieceColor.WHITE);

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
}
