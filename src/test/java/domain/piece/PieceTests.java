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

    private void assertPiece(PieceType type, PieceColor color) {
        Piece piece = new Piece(type, color);

        assertEquals(type, piece.getType());
        assertEquals(color, piece.getColor());
    }
}
