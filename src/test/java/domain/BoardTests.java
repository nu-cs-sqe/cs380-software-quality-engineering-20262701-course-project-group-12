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

    @Test
    public void PieceAt_E1_ReturnsWhiteKing() {
        assertOccupant(4, 1, PieceType.KING, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_A2_ReturnsWhitePawn() {
        assertOccupant(0, 2, PieceType.PAWN, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_H2_ReturnsWhitePawn() {
        assertOccupant(7, 2, PieceType.PAWN, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_A3_ReturnsEmpty() {
        assertEmpty(0, 3);
    }

    @Test
    public void PieceAt_H3_ReturnsEmpty() {
        assertEmpty(7, 3);
    }

    @Test
    public void PieceAt_A6_ReturnsEmpty() {
        assertEmpty(0, 6);
    }

    @Test
    public void PieceAt_H6_ReturnsEmpty() {
        assertEmpty(7, 6);
    }

    @Test
    public void PieceAt_A7_ReturnsBlackPawn() {
        assertOccupant(0, 7, PieceType.PAWN, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_H7_ReturnsBlackPawn() {
        assertOccupant(7, 7, PieceType.PAWN, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_A8_ReturnsBlackRook() {
        assertOccupant(0, 8, PieceType.ROOK, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_B8_ReturnsBlackKnight() {
        assertOccupant(1, 8, PieceType.KNIGHT, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_C8_ReturnsBlackBishop() {
        assertOccupant(2, 8, PieceType.BISHOP, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_D8_ReturnsBlackQueen() {
        assertOccupant(3, 8, PieceType.QUEEN, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_E8_ReturnsBlackKing() {
        assertOccupant(4, 8, PieceType.KING, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_H8_ReturnsBlackRook() {
        assertOccupant(7, 8, PieceType.ROOK, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_NullLocation_ThrowsIllegalArgumentException() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> board.pieceAt(null));
    }

    @Test
    public void PieceAt_CalledTwiceOnA1_StillReturnsWhiteRook() {
        Board board = new Board();
        Location a1 = new Location(0, 1);

        Piece first = board.pieceAt(a1);
        Piece second = board.pieceAt(a1);

        assertEquals(PieceType.ROOK, first.getType());
        assertEquals(PieceColor.WHITE, first.getColor());
        assertEquals(PieceType.ROOK, second.getType());
        assertEquals(PieceColor.WHITE, second.getColor());
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
