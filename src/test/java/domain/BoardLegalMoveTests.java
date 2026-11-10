package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardLegalMoveTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location A2 = new Location(0, 2);
    private static final Location A3 = new Location(0, 3);
    private static final Location B1 = new Location(1, 1);
    private static final Location B5 = new Location(1, 5);
    private static final Location B8 = new Location(1, 8);
    private static final Location C3 = new Location(2, 3);
    private static final Location C5 = new Location(2, 5);
    private static final Location C6 = new Location(2, 6);
    private static final Location D2 = new Location(3, 2);
    private static final Location D3 = new Location(3, 3);
    private static final Location D4 = new Location(3, 4);
    private static final Location D6 = new Location(3, 6);
    private static final Location D7 = new Location(3, 7);
    private static final Location D8 = new Location(3, 8);
    private static final Location E1 = new Location(4, 1);
    private static final Location E2 = new Location(4, 2);
    private static final Location E3 = new Location(4, 3);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location E6 = new Location(4, 6);
    private static final Location E7 = new Location(4, 7);
    private static final Location E8 = new Location(4, 8);
    private static final Location F1 = new Location(5, 1);
    private static final Location F2 = new Location(5, 2);
    private static final Location F3 = new Location(5, 3);
    private static final Location F8 = new Location(5, 8);
    private static final Location G1 = new Location(6, 1);
    private static final Location H4 = new Location(7, 4);

    @Test
    public void IsLegalMove_MoveThatKeepsTheKingSafe_ReturnsTrue() {
        Board board = new Board();

        boolean legal = board.isLegalMove(B1, C3);

        assertTrue(legal);
    }

    @Test
    public void IsLegalMove_MoveThePieceCannotMake_ReturnsFalse() {
        Board board = new Board();

        boolean legal = board.isLegalMove(A1, A3);

        assertFalse(legal);
    }

    @Test
    public void IsLegalMove_PinnedPieceOffTheLineToItsKing_ReturnsFalse() {
        Board board = knightPinnedByBishop();

        boolean legal = board.isLegalMove(C6, D4);

        assertFalse(legal);
    }

    @Test
    public void IsLegalMove_KingNextToTheOtherKing_ReturnsFalse() {
        Board board = kingsFaceToFace();

        boolean legal = board.isLegalMove(E3, E4);

        assertFalse(legal);
    }

    @Test
    public void IsLegalMove_KingCapturesDefendedPiece_ReturnsFalse() {
        Board board = defendedQueenChecksWhite();

        boolean legal = board.isLegalMove(E2, F2);

        assertFalse(legal);
    }

    private Board knightPinnedByBishop() {
        Board board = new Board();
        board.move(E2, E4);
        board.move(E7, E5);
        board.move(G1, F3);
        board.move(B8, C6);
        board.move(F1, B5);
        board.move(D7, D6);
        return board;
    }

    private Board kingsFaceToFace() {
        Board board = new Board();
        board.move(D2, D3);
        board.move(D7, D6);
        board.move(E1, D2);
        board.move(E8, D7);
        board.move(D2, E3);
        board.move(D7, E6);
        board.move(A2, A3);
        board.move(E6, E5);
        return board;
    }

    private Board defendedQueenChecksWhite() {
        Board board = new Board();
        board.move(E2, E4);
        board.move(E7, E5);
        board.move(E1, E2);
        board.move(F8, C5);
        board.move(D2, D3);
        board.move(D8, H4);
        board.move(A2, A3);
        board.move(H4, F2);
        return board;
    }
}
