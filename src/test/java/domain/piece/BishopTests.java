package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BishopTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location A6 = new Location(0, 6);
    private static final Location A8 = new Location(0, 8);
    private static final Location B2 = new Location(1, 2);
    private static final Location B4 = new Location(1, 4);
    private static final Location B5 = new Location(1, 5);
    private static final Location B7 = new Location(1, 7);
    private static final Location C1 = new Location(2, 1);
    private static final Location C8 = new Location(2, 8);
    private static final Location D2 = new Location(3, 2);
    private static final Location D4 = new Location(3, 4);
    private static final Location D5 = new Location(3, 5);
    private static final Location D7 = new Location(3, 7);
    private static final Location E5 = new Location(4, 5);
    private static final Location E7 = new Location(4, 7);
    private static final Location F1 = new Location(5, 1);
    private static final Location F8 = new Location(5, 8);
    private static final Location G2 = new Location(6, 2);
    private static final Location G4 = new Location(6, 4);
    private static final Location G5 = new Location(6, 5);
    private static final Location G7 = new Location(6, 7);
    private static final Location H1 = new Location(7, 1);
    private static final Location H6 = new Location(7, 6);
    private static final Location H8 = new Location(7, 8);

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void CanMove_OneSquareUpAndRightOntoEmptySquare_ReturnsTrue() {
        board.move(D2, D4);
        Bishop bishop = new Bishop(PieceColor.WHITE);

        boolean canMove = bishop.canMove(C1, D2, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_OneSquareUpAndLeftOntoEmptySquare_ReturnsTrue() {
        board.move(B2, B4);
        Bishop bishop = new Bishop(PieceColor.WHITE);

        boolean canMove = bishop.canMove(C1, B2, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackBishopOneSquareDownAndRightOntoEmptySquare_ReturnsTrue() {
        board.move(D7, D5);
        Bishop bishop = new Bishop(PieceColor.BLACK);

        boolean canMove = bishop.canMove(C8, D7, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackBishopOneSquareDownAndLeftOntoEmptySquare_ReturnsTrue() {
        board.move(E7, E5);
        Bishop bishop = new Bishop(PieceColor.BLACK);

        boolean canMove = bishop.canMove(F8, E7, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_SevenSquaresAlongDarkDiagonalOntoEmptySquare_ReturnsTrue() {
        board.move(C1, A1);
        board.move(B2, B4);
        board.move(G7, G5);
        board.move(H8, H6);
        Bishop bishop = new Bishop(PieceColor.WHITE);

        boolean canMove = bishop.canMove(A1, H8, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_SevenSquaresAlongLightDiagonalOntoEmptySquare_ReturnsTrue() {
        board.move(F1, H1);
        board.move(G2, G4);
        board.move(B7, B5);
        board.move(A8, A6);
        Bishop bishop = new Bishop(PieceColor.WHITE);

        boolean canMove = bishop.canMove(H1, A8, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_SameSquare_ReturnsFalse() {
        Bishop bishop = new Bishop(PieceColor.WHITE);

        boolean canMove = bishop.canMove(C1, C1, board);

        assertFalse(canMove);
    }
}
