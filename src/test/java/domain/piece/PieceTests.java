package domain.piece;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PieceTests {

    @Test
    public void Piece_WhitePawn_StoresTypeAndColor() {
        assertPiece(PieceType.PAWN, PieceColor.WHITE);
    }

    @Test
    public void Piece_BlackPawn_StoresTypeAndColor() {
        assertPiece(PieceType.PAWN, PieceColor.BLACK);
    }

    @Test
    public void Piece_WhiteRook_StoresTypeAndColor() {
        assertPiece(PieceType.ROOK, PieceColor.WHITE);
    }

    @Test
    public void Piece_BlackRook_StoresTypeAndColor() {
        assertPiece(PieceType.ROOK, PieceColor.BLACK);
    }

    @Test
    public void Piece_WhiteKnight_StoresTypeAndColor() {
        assertPiece(PieceType.KNIGHT, PieceColor.WHITE);
    }

    @Test
    public void Piece_BlackKnight_StoresTypeAndColor() {
        assertPiece(PieceType.KNIGHT, PieceColor.BLACK);
    }

    @Test
    public void Piece_WhiteBishop_StoresTypeAndColor() {
        assertPiece(PieceType.BISHOP, PieceColor.WHITE);
    }

    @Test
    public void Piece_BlackBishop_StoresTypeAndColor() {
        assertPiece(PieceType.BISHOP, PieceColor.BLACK);
    }

    @Test
    public void Piece_WhiteQueen_StoresTypeAndColor() {
        assertPiece(PieceType.QUEEN, PieceColor.WHITE);
    }

    @Test
    public void Piece_BlackQueen_StoresTypeAndColor() {
        assertPiece(PieceType.QUEEN, PieceColor.BLACK);
    }

    @Test
    public void Piece_WhiteKing_StoresTypeAndColor() {
        assertPiece(PieceType.KING, PieceColor.WHITE);
    }

    @Test
    public void Piece_BlackKing_StoresTypeAndColor() {
        assertPiece(PieceType.KING, PieceColor.BLACK);
    }

    private void assertPiece(PieceType type, PieceColor color) {
        Piece piece = new Piece(type, color);

        assertEquals(type, piece.getType());
        assertEquals(color, piece.getColor());
    }
}
