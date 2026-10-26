package domain;

import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardTests {

    @Test
    public void PieceAt_A1_ReturnsWhiteRook() {
        assertOccupant(0, 1, PieceType.ROOK, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_H1_ReturnsWhiteRook() {
        assertOccupant(7, 1, PieceType.ROOK, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_B1_ReturnsWhiteKnight() {
        assertOccupant(1, 1, PieceType.KNIGHT, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_C1_ReturnsWhiteBishop() {
        assertOccupant(2, 1, PieceType.BISHOP, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_D1_ReturnsWhiteQueen() {
        assertOccupant(3, 1, PieceType.QUEEN, PieceColor.WHITE);
    }

    private void assertOccupant(int file, int rank, PieceType type, PieceColor color) {
        Board board = new Board();
        Piece piece = board.pieceAt(new Location(file, rank));

        assertEquals(type, piece.getType());
        assertEquals(color, piece.getColor());
    }

    private void assertEmpty(int file, int rank) {
        Board board = new Board();

        assertNull(board.pieceAt(new Location(file, rank)));
    }
}
