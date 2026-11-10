package domain.piece;

import domain.Board;
import domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.partialMockBuilder;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KingCastlingTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location A3 = new Location(0, 3);
    private static final Location A6 = new Location(0, 6);
    private static final Location A8 = new Location(0, 8);
    private static final Location B1 = new Location(1, 1);
    private static final Location B4 = new Location(1, 4);
    private static final Location B5 = new Location(1, 5);
    private static final Location B8 = new Location(1, 8);
    private static final Location C1 = new Location(2, 1);
    private static final Location C4 = new Location(2, 4);
    private static final Location C5 = new Location(2, 5);
    private static final Location C8 = new Location(2, 8);
    private static final Location D1 = new Location(3, 1);
    private static final Location D3 = new Location(3, 3);
    private static final Location D4 = new Location(3, 4);
    private static final Location D5 = new Location(3, 5);
    private static final Location D8 = new Location(3, 8);
    private static final Location E1 = new Location(4, 1);
    private static final Location E3 = new Location(4, 3);
    private static final Location E8 = new Location(4, 8);
    private static final Location F1 = new Location(5, 1);
    private static final Location F4 = new Location(5, 4);
    private static final Location F5 = new Location(5, 5);
    private static final Location F8 = new Location(5, 8);
    private static final Location G1 = new Location(6, 1);
    private static final Location G3 = new Location(6, 3);
    private static final Location G4 = new Location(6, 4);
    private static final Location G5 = new Location(6, 5);
    private static final Location G8 = new Location(6, 8);
    private static final Location H1 = new Location(7, 1);
    private static final Location H2 = new Location(7, 2);
    private static final Location H3 = new Location(7, 3);
    private static final Location H7 = new Location(7, 7);
    private static final Location H8 = new Location(7, 8);
    private static final King WHITE_KING = new King(PieceColor.WHITE);
    private static final King BLACK_KING = new King(PieceColor.BLACK);

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void CanMove_WhiteKingside_ReturnsTrue() {
        clearWhiteKingside();

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_WhiteQueenside_ReturnsTrue() {
        clearWhiteQueenside();

        boolean canMove = WHITE_KING.canMove(E1, C1, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackKingside_ReturnsTrue() {
        clearBlackKingside();

        boolean canMove = BLACK_KING.canMove(E8, G8, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_BlackQueenside_ReturnsTrue() {
        clearBlackQueenside();

        boolean canMove = BLACK_KING.canMove(E8, C8, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_KingMovedAndCameBack_ReturnsFalse() {
        clearWhiteKingside();
        board.move(E1, F1);
        board.move(F1, E1);

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_RookMovedAndCameBack_ReturnsFalse() {
        clearWhiteKingside();
        board.move(H1, G1);
        board.move(G1, H1);

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_KingsideAfterQueensideRookMoved_ReturnsTrue() {
        clearWhiteKingside();
        board.move(A1, A3);
        board.move(A3, A1);

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_RookIsGone_ReturnsFalse() {
        clearWhiteKingside();
        board.move(H1, H3);

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_PieceNextToTheRook_ReturnsFalse() {
        board.move(C1, C4);
        board.move(D1, D4);

        boolean canMove = WHITE_KING.canMove(E1, C1, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_KingInCheck_ReturnsFalse() {
        clearWhiteKingside();
        board.move(B8, D3);

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_CrossedSquareAttacked_ReturnsFalse() {
        clearWhiteKingside();
        board.move(B8, E3);

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_LandingSquareAttacked_ReturnsFalse() {
        clearWhiteKingside();
        board.move(G8, H3);

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_QueensideWhileB1Attacked_ReturnsTrue() {
        clearWhiteQueenside();
        board.move(B8, A3);

        boolean canMove = WHITE_KING.canMove(E1, C1, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_KingsideWhileRookAttacked_ReturnsTrue() {
        clearWhiteKingside();
        board.move(H2, A3);
        board.move(H7, A6);

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertTrue(canMove);
    }

    @Test
    public void CanMove_TwoFilesOffTheRank_ReturnsFalse() {
        clearWhiteKingside();

        boolean canMove = WHITE_KING.canMove(E1, G3, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_ThreeFilesTowardQueensideRook_ReturnsFalse() {
        clearWhiteQueenside();

        boolean canMove = WHITE_KING.canMove(E1, B1, board);

        assertFalse(canMove);
    }

    @Test
    public void CanMove_UnmovedOpponentPieceOnRookSquare_ReturnsFalse() {
        board = boardWhereEveryPieceIsUnmoved();
        clearWhiteKingside();
        board.move(G8, H1);

        boolean canMove = WHITE_KING.canMove(E1, G1, board);

        assertFalse(canMove);
    }

    @Test
    public void Attacks_CastleSquare_ReturnsFalse() {
        clearWhiteKingside();

        boolean attacks = WHITE_KING.attacks(E1, G1, board);

        assertFalse(attacks);
    }

    @Test
    public void Move_WhiteKingside_PutsTheRookOnF1() {
        clearWhiteKingside();
        Piece kingsideRook = board.pieceAt(H1);

        WHITE_KING.move(E1, G1, board);

        assertSame(kingsideRook, board.pieceAt(F1));
    }

    @Test
    public void Move_WhiteQueenside_PutsTheRookOnD1() {
        clearWhiteQueenside();
        Piece queensideRook = board.pieceAt(A1);

        WHITE_KING.move(E1, C1, board);

        assertSame(queensideRook, board.pieceAt(D1));
    }

    @Test
    public void Move_BlackKingside_PutsTheRookOnF8() {
        clearBlackKingside();
        Piece kingsideRook = board.pieceAt(H8);

        BLACK_KING.move(E8, G8, board);

        assertSame(kingsideRook, board.pieceAt(F8));
    }

    @Test
    public void Move_BlackQueenside_PutsTheRookOnD8() {
        clearBlackQueenside();
        Piece queensideRook = board.pieceAt(A8);

        BLACK_KING.move(E8, C8, board);

        assertSame(queensideRook, board.pieceAt(D8));
    }

    @Test
    public void Move_OneSquare_LeavesTheRookInPlace() {
        clearWhiteKingside();
        Piece kingsideRook = board.pieceAt(H1);

        WHITE_KING.move(E1, F1, board);

        assertSame(kingsideRook, board.pieceAt(H1));
    }

    @Test
    public void Move_WhiteKingside_PutsTheKingOnG1() {
        clearWhiteKingside();
        Piece king = board.pieceAt(E1);

        WHITE_KING.move(E1, G1, board);

        assertSame(king, board.pieceAt(G1));
    }

    private static Board boardWhereEveryPieceIsUnmoved() {
        Board partialBoard = partialMockBuilder(Board.class)
                .withConstructor()
                .addMockedMethod("holdsUnmovedPiece")
                .createMock();
        expect(partialBoard.holdsUnmovedPiece(anyObject())).andStubReturn(true);
        replay(partialBoard);
        return partialBoard;
    }

    private void clearWhiteKingside() {
        board.move(F1, F4);
        board.move(G1, G4);
    }

    private void clearWhiteQueenside() {
        board.move(B1, B4);
        board.move(C1, C4);
        board.move(D1, D4);
    }

    private void clearBlackKingside() {
        board.move(F8, F5);
        board.move(G8, G5);
    }

    private void clearBlackQueenside() {
        board.move(B8, B5);
        board.move(C8, C5);
        board.move(D8, D5);
    }
}
