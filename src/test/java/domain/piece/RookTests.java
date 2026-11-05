package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RookTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location A2 = new Location(0, 2);
    private static final Location B1 = new Location(1, 1);
    private static final Location B3 = new Location(1, 3);
    private static final Location C3 = new Location(2, 3);

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
}
