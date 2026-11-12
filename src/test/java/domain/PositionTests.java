package domain;

import domain.piece.PieceColor;
import domain.piece.PieceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionTests {
    private static final Location A2 = new Location(0, 2);
    private static final Location A3 = new Location(0, 3);
    private static final Location B1 = new Location(1, 1);
    private static final Location D4 = new Location(3, 4);
    private static final Location D7 = new Location(3, 7);
    private static final Location E2 = new Location(4, 2);
    private static final Location E3 = new Location(4, 3);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location E7 = new Location(4, 7);
    private static final Location F2 = new Location(5, 2);
    private static final Location F3 = new Location(5, 3);
    private static final Location F4 = new Location(5, 4);
    private static final Location G1 = new Location(6, 1);

    @Test
    public void IsSameAs_SamePiecesAndPlayerToMove_ReturnsTrue() {
        Position position = new Board().position(PieceColor.WHITE);
        Position other = new Board().position(PieceColor.WHITE);

        boolean same = position.isSameAs(other);

        assertTrue(same);
    }

    @Test
    public void IsSameAs_AnotherPlayerToMove_ReturnsFalse() {
        Position position = new Board().position(PieceColor.WHITE);
        Position other = new Board().position(PieceColor.BLACK);

        boolean same = position.isSameAs(other);

        assertFalse(same);
    }

    @Test
    public void IsSameAs_PieceOnAnotherSquare_ReturnsFalse() {
        Board board = new Board();
        board.move(G1, F3);
        Position position = new Board().position(PieceColor.WHITE);

        boolean same = position.isSameAs(board.position(PieceColor.WHITE));

        assertFalse(same);
    }

    @Test
    public void IsSameAs_KnightsOfOneColorSwapSquares_ReturnsTrue() {
        Board board = new Board();
        board.move(B1, A3);
        board.move(G1, B1);
        board.move(A3, G1);
        Position position = new Board().position(PieceColor.WHITE);

        boolean same = position.isSameAs(board.position(PieceColor.WHITE));

        assertTrue(same);
    }

    @Test
    public void IsSameAs_SquareWithPieceOfAnotherType_ReturnsFalse() {
        Board board = new Board();
        board.promote(A2, PieceType.QUEEN);
        Position position = new Board().position(PieceColor.WHITE);

        boolean same = position.isSameAs(board.position(PieceColor.WHITE));

        assertFalse(same);
    }

    @Test
    public void IsSameAs_SquaresWithPiecesOfTheOtherColor_ReturnsFalse() {
        Board board = new Board();
        board.move(E2, E4);
        board.move(E7, E5);
        Board other = new Board();
        other.move(E7, E4);
        other.move(E2, E5);

        boolean same = board.position(PieceColor.WHITE).isSameAs(other.position(PieceColor.WHITE));

        assertFalse(same);
    }

    @Test
    public void IsSameAs_EnPassantCaptureAvailableInOneOnly_ReturnsFalse() {
        Board board = new Board();
        board.move(D7, D4);
        board.move(E2, E4);
        Board other = new Board();
        other.move(D7, D4);
        other.move(E2, E3);
        other.move(E3, E4);

        boolean same = board.position(PieceColor.BLACK).isSameAs(other.position(PieceColor.BLACK));

        assertFalse(same);
    }

    @Test
    public void IsSameAs_TwoRankPawnMoveWithNoPawnBeside_ReturnsTrue() {
        Board board = new Board();
        board.move(E2, E4);
        Board other = new Board();
        other.move(E2, E3);
        other.move(E3, E4);

        boolean same = board.position(PieceColor.BLACK).isSameAs(other.position(PieceColor.BLACK));

        assertTrue(same);
    }

    @Test
    public void IsSameAs_PawnBesideBelongsToTheOtherPlayer_ReturnsTrue() {
        Board board = new Board();
        board.move(F2, F4);
        board.move(E2, E4);
        Board other = new Board();
        other.move(F2, F4);
        other.move(E2, E3);
        other.move(E3, E4);

        boolean same = board.position(PieceColor.BLACK).isSameAs(other.position(PieceColor.BLACK));

        assertTrue(same);
    }
}
