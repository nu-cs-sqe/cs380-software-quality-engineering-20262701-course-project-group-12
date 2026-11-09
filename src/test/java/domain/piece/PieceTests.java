package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PieceTests {
    private static final Location D1 = new Location(3, 1);
    private static final Location D3 = new Location(3, 3);

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

    @Test
    public void CanMove_PieceWithoutMovementRules_ReturnsFalse() {
        Piece whiteQueen = new Piece(PieceType.QUEEN, PieceColor.WHITE);
        Board board = mock(Board.class);
        replay(board);

        boolean canMove = whiteQueen.canMove(D1, D3, board);

        assertFalse(canMove);
        verify(board);
    }

    @Test
    public void Attacks_PieceWithoutMovementRules_ReturnsFalse() {
        Piece whiteQueen = new Piece(PieceType.QUEEN, PieceColor.WHITE);
        Board board = mock(Board.class);
        replay(board);

        boolean attacks = whiteQueen.attacks(D1, D3, board);

        assertFalse(attacks);
        verify(board);
    }

    @Test
    public void Move_PlainPiece_AsksTheBoardToMoveIt() {
        Piece whiteQueen = new Piece(PieceType.QUEEN, PieceColor.WHITE);
        Board board = mock(Board.class);
        board.move(D1, D3);
        replay(board);

        whiteQueen.move(D1, D3, board);

        verify(board);
    }

    private void assertPiece(PieceType type, PieceColor color) {
        Piece piece = new Piece(type, color);

        assertEquals(type, piece.getType());
        assertEquals(color, piece.getColor());
    }
}
