package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PawnTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location A2 = new Location(0, 2);
    private static final Location A5 = new Location(0, 5);
    private static final Location A7 = new Location(0, 7);
    private static final Location A8 = new Location(0, 8);
    private static final Location B3 = new Location(1, 3);
    private static final Location B7 = new Location(1, 7);
    private static final Location D3 = new Location(3, 3);
    private static final Location D7 = new Location(3, 7);
    private static final Location E2 = new Location(4, 2);
    private static final Location E3 = new Location(4, 3);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location E6 = new Location(4, 6);
    private static final Location E7 = new Location(4, 7);
    private static final Location F3 = new Location(5, 3);
    private static final Location F4 = new Location(5, 4);
    private static final Location G1 = new Location(6, 1);
    private static final Location G3 = new Location(6, 3);
    private static final Location G4 = new Location(6, 4);
    private static final Location G7 = new Location(6, 7);
    private static final Location H2 = new Location(7, 2);

    private final Pawn whitePawn = new Pawn(PieceColor.WHITE);
    private final Pawn blackPawn = new Pawn(PieceColor.BLACK);
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void CanMove_WhitePawnOneSquareForwardOntoEmptySquare_ReturnsTrue() {
        boolean canMove = whitePawn.canMove(E2, E3, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_WhitePawnTwoSquaresForwardFromStartingRank_ReturnsTrue() {
        boolean canMove = whitePawn.canMove(E2, E4, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackPawnOneSquareForwardOntoEmptySquare_ReturnsTrue() {
        boolean canMove = blackPawn.canMove(E7, E6, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackPawnTwoSquaresForwardFromStartingRank_ReturnsTrue() {
        boolean canMove = blackPawn.canMove(E7, E5, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_CaptureTowardHigherFileFromFileA_ReturnsTrue() {
        board.move(B7, B3);

        boolean canMove = whitePawn.canMove(A2, B3, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_CaptureTowardLowerFileFromFileH_ReturnsTrue() {
        board.move(G7, G3);

        boolean canMove = whitePawn.canMove(H2, G3, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackPawnCapturesDiagonallyForward_ReturnsTrue() {
        board.move(E2, E6);

        boolean canMove = blackPawn.canMove(D7, E6, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneSquareForwardOntoLastRank_ReturnsTrue() {
        board.move(A2, A7);
        board.move(A8, A5);

        boolean canMove = whitePawn.canMove(A7, A8, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_SameSquare_ReturnsFalse() {
        boolean canMove = whitePawn.canMove(E2, E2, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_WhitePawnOneSquareBackward_ReturnsFalse() {
        board.move(E2, E4);

        boolean canMove = whitePawn.canMove(E4, E3, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_BlackPawnOneSquareBackward_ReturnsFalse() {
        board.move(E7, E5);

        boolean canMove = blackPawn.canMove(E5, E6, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_OneSquareSideways_ReturnsFalse() {
        board.move(E2, E4);

        boolean canMove = whitePawn.canMove(E4, F4, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_WhiteTwoSquareAdvanceOffStartingRank_ReturnsFalse() {
        board.move(E2, E3);

        boolean canMove = whitePawn.canMove(E3, E5, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_BlackTwoSquareAdvanceOffStartingRank_ReturnsFalse() {
        board.move(E7, E6);

        boolean canMove = blackPawn.canMove(E6, E4, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_ThreeSquareAdvanceFromStartingRank_ReturnsFalse() {
        boolean canMove = whitePawn.canMove(E2, E5, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_StraightCaptureOneSquareForward_ReturnsFalse() {
        board.move(E7, E3);

        boolean canMove = whitePawn.canMove(E2, E3, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_OneSquareForwardOntoOwnPiece_ReturnsFalse() {
        board.move(G1, E3);

        boolean canMove = whitePawn.canMove(E2, E3, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_TwoSquareAdvanceJumpsPiece_ReturnsFalse() {
        board.move(G1, E3);

        boolean canMove = whitePawn.canMove(E2, E4, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_TwoSquareAdvanceCaptures_ReturnsFalse() {
        board.move(E7, E4);

        boolean canMove = whitePawn.canMove(E2, E4, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_DiagonalOntoEmptySquare_ReturnsFalse() {
        boolean canMove = whitePawn.canMove(E2, F3, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_DiagonalOntoOwnPiece_ReturnsFalse() {
        board.move(G1, F3);

        boolean canMove = whitePawn.canMove(E2, F3, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_DiagonalCaptureBackward_ReturnsFalse() {
        board.move(E2, E4);
        board.move(D7, D3);

        boolean canMove = whitePawn.canMove(E4, D3, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_CaptureTwoSquaresDiagonallyForward_ReturnsFalse() {
        board.move(G7, G4);

        boolean canMove = whitePawn.canMove(E2, G4, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_CaptureOneRankForwardTwoFilesOver_ReturnsFalse() {
        board.move(G7, G3);

        boolean canMove = whitePawn.canMove(E2, G3, board);

        assertFalse(canMove);
    }

    @Test
    public void PromotesOn_WhitePawnOnRankEight_ReturnsTrue() {
        boolean promotes = whitePawn.promotesOn(A8);

        assertTrue(promotes);
    }

    @Test
    public void PromotesOn_WhitePawnOnRankSeven_ReturnsFalse() {
        boolean promotes = whitePawn.promotesOn(A7);

        assertFalse(promotes);
    }

    @Test
    public void PromotesOn_BlackPawnOnRankOne_ReturnsTrue() {
        boolean promotes = blackPawn.promotesOn(A1);

        assertTrue(promotes);
    }

    @Test
    public void PromotesOn_BlackPawnOnRankTwo_ReturnsFalse() {
        boolean promotes = blackPawn.promotesOn(A2);

        assertFalse(promotes);
    }
}
