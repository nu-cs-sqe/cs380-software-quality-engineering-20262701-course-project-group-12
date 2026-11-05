package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RookTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location A2 = new Location(0, 2);
    private static final Location A3 = new Location(0, 3);
    private static final Location A4 = new Location(0, 4);
    private static final Location A5 = new Location(0, 5);
    private static final Location A6 = new Location(0, 6);
    private static final Location A7 = new Location(0, 7);
    private static final Location A8 = new Location(0, 8);
    private static final Location B1 = new Location(1, 1);
    private static final Location B3 = new Location(1, 3);
    private static final Location B4 = new Location(1, 4);
    private static final Location B5 = new Location(1, 5);
    private static final Location B6 = new Location(1, 6);
    private static final Location C1 = new Location(2, 1);
    private static final Location C3 = new Location(2, 3);
    private static final Location C6 = new Location(2, 6);
    private static final Location D1 = new Location(3, 1);
    private static final Location E1 = new Location(4, 1);
    private static final Location F1 = new Location(5, 1);
    private static final Location G1 = new Location(6, 1);
    private static final Location H1 = new Location(7, 1);

    @Test
    public void CanMove_OneSquareAlongFileOntoEmptySquare_ReturnsTrue() {
        Board board = new Board();
        board.move(A2, B3);
        Rook rook = new Rook(PieceColor.WHITE);

        boolean canMove = rook.canMove(A1, A2, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneSquareAlongRankOntoEmptySquare_ReturnsTrue() {
        Board board = new Board();
        board.move(B1, C3);
        Rook rook = new Rook(PieceColor.WHITE);

        boolean canMove = rook.canMove(A1, B1, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_SevenSquaresAlongFileOntoEmptySquare_ReturnsTrue() {
        Board board = new Board();
        board.move(A2, B3);
        board.move(A7, B6);
        board.move(A8, C6);
        Rook rook = new Rook(PieceColor.WHITE);

        boolean canMove = rook.canMove(A1, A8, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_SevenSquaresAlongRankOntoEmptySquare_ReturnsTrue() {
        Board board = new Board();
        board.move(B1, A3);
        board.move(C1, A4);
        board.move(D1, A5);
        board.move(E1, A6);
        board.move(F1, B3);
        board.move(G1, B4);
        board.move(H1, B5);
        Rook rook = new Rook(PieceColor.WHITE);

        boolean canMove = rook.canMove(A1, H1, board);

        assertTrue(canMove);
    }
}
