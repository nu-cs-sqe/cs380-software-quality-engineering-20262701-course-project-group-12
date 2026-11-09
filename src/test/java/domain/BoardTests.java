package domain;

import domain.piece.Knight;
import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;
import domain.piece.Rook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location A3 = new Location(0, 3);
    private static final Location A4 = new Location(0, 4);
    private static final Location A5 = new Location(0, 5);
    private static final Location A8 = new Location(0, 8);
    private static final Location B1 = new Location(1, 1);
    private static final Location H1 = new Location(7, 1);
    private static final Location H5 = new Location(7, 5);
    private static final Location H8 = new Location(7, 8);
    private static final int STARTING_PIECE_COUNT = 32;

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
    public void IsOccupied_A3_ReturnsFalse() {
        assertNotOccupied(0, 3);
    }

    @Test
    public void IsOccupied_H3_ReturnsFalse() {
        assertNotOccupied(7, 3);
    }

    @Test
    public void IsOccupied_A6_ReturnsFalse() {
        assertNotOccupied(0, 6);
    }

    @Test
    public void IsOccupied_H6_ReturnsFalse() {
        assertNotOccupied(7, 6);
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
    public void PieceAt_EmptyA3_ThrowsIllegalArgumentException() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> board.pieceAt(A3));
    }

    @Test
    public void PieceAt_CalledTwiceOnA1_StillReturnsWhiteRook() {
        Board board = new Board();

        Piece first = board.pieceAt(A1);
        Piece second = board.pieceAt(A1);

        assertEquals(PieceType.ROOK, first.getType());
        assertEquals(PieceColor.WHITE, first.getColor());
        assertEquals(PieceType.ROOK, second.getType());
        assertEquals(PieceColor.WHITE, second.getColor());
    }

    @Test
    public void IsOccupied_A1_ReturnsTrue() {
        assertIsOccupied(0, 1);
    }

    @Test
    public void IsOccupied_H1_ReturnsTrue() {
        assertIsOccupied(7, 1);
    }

    @Test
    public void IsOccupied_H2_ReturnsTrue() {
        assertIsOccupied(7, 2);
    }

    @Test
    public void IsOccupied_A7_ReturnsTrue() {
        assertIsOccupied(0, 7);
    }

    @Test
    public void IsOccupied_A8_ReturnsTrue() {
        assertIsOccupied(0, 8);
    }

    @Test
    public void IsOccupied_H8_ReturnsTrue() {
        assertIsOccupied(7, 8);
    }

    @Test
    public void Move_A1ToEmptyA4_PlacesTheRookOnA4() {
        Board board = new Board();
        Piece whiteRook = board.pieceAt(A1);

        board.move(A1, A4);

        assertSame(whiteRook, board.pieceAt(A4));
    }

    @Test
    public void Move_A1ToEmptyA4_LeavesA1Empty() {
        Board board = new Board();

        board.move(A1, A4);

        assertFalse(board.isOccupied(A1));
    }

    @Test
    public void Move_H8ToEmptyH5_PlacesTheRookOnH5() {
        Board board = new Board();
        Piece blackRook = board.pieceAt(H8);

        board.move(H8, H5);

        assertSame(blackRook, board.pieceAt(H5));
    }

    @Test
    public void Move_H8ToEmptyH5_LeavesH8Empty() {
        Board board = new Board();

        board.move(H8, H5);

        assertFalse(board.isOccupied(H8));
    }

    @Test
    public void Move_A1ToOccupiedA8_ReplacesTheBlackRookWithTheWhiteRook() {
        Board board = new Board();
        Piece whiteRook = board.pieceAt(A1);

        board.move(A1, A8);

        assertSame(whiteRook, board.pieceAt(A8));
    }

    @Test
    public void Move_H8ToOccupiedH1_ReplacesTheWhiteRookWithTheBlackRook() {
        Board board = new Board();
        Piece blackRook = board.pieceAt(H8);

        board.move(H8, H1);

        assertSame(blackRook, board.pieceAt(H1));
    }

    @Test
    public void Move_A8ToOccupiedA1_ReplacesTheWhiteRookWithTheBlackRook() {
        Board board = new Board();
        Piece blackRook = board.pieceAt(A8);

        board.move(A8, A1);

        assertSame(blackRook, board.pieceAt(A1));
    }

    @Test
    public void Move_H1ToOccupiedH8_ReplacesTheBlackRookWithTheWhiteRook() {
        Board board = new Board();
        Piece whiteRook = board.pieceAt(H1);

        board.move(H1, H8);

        assertSame(whiteRook, board.pieceAt(H8));
    }

    @Test
    public void Move_A1ToA1_LeavesTheRookOnA1() {
        Board board = new Board();
        Piece whiteRook = board.pieceAt(A1);

        board.move(A1, A1);

        assertSame(whiteRook, board.pieceAt(A1));
    }

    @Test
    public void Move_FromEmptyA4_ThrowsIllegalArgumentException() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> board.move(A4, A5));
    }

    @Test
    public void Move_EmptyA4ToA1_LeavesTheRookOnA1() {
        Board board = new Board();
        Piece whiteRook = board.pieceAt(A1);

        assertThrows(IllegalArgumentException.class, () -> board.move(A4, A1));

        assertSame(whiteRook, board.pieceAt(A1));
    }

    @Test
    public void Move_A1ToOccupiedA8_TakesTheCapturedRookOffTheBoard() {
        Board board = new Board();

        board.move(A1, A8);

        assertEquals(STARTING_PIECE_COUNT - 1, countPieces(board));
    }

    @Test
    public void Board_A1_PlacesRook() {
        assertPieceClass(A1, Rook.class);
    }

    @Test
    public void Board_H8_PlacesRook() {
        assertPieceClass(H8, Rook.class);
    }

    @Test
    public void Board_B1_PlacesKnight() {
        assertPieceClass(B1, Knight.class);
    }

    private int countPieces(Board board) {
        int pieces = 0;
        for (int file = Location.MIN_FILE; file <= Location.MAX_FILE; file++) {
            for (int rank = Location.MIN_RANK; rank <= Location.MAX_RANK; rank++) {
                if (board.isOccupied(new Location(file, rank))) {
                    pieces++;
                }
            }
        }
        return pieces;
    }

    private void assertPieceClass(Location square, Class<? extends Piece> pieceClass) {
        Board board = new Board();

        assertInstanceOf(pieceClass, board.pieceAt(square));
    }

    private void assertOccupant(int file, int rank, PieceType type, PieceColor color) {
        Board board = new Board();
        Piece piece = board.pieceAt(new Location(file, rank));

        assertEquals(type, piece.getType());
        assertEquals(color, piece.getColor());
    }

    private void assertIsOccupied(int file, int rank) {
        Board board = new Board();

        assertTrue(board.isOccupied(new Location(file, rank)));
    }

    private void assertNotOccupied(int file, int rank) {
        Board board = new Board();

        assertFalse(board.isOccupied(new Location(file, rank)));
    }
}
