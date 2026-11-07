package ui;

import domain.Board;
import domain.Location;
import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardControllerTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location A3 = new Location(0, 3);
    private static final Location B1 = new Location(1, 1);
    private static final Location B3 = new Location(1, 3);
    private static final Location C3 = new Location(2, 3);

    private Board board;
    private Piece piece;

    @BeforeEach
    public void createMocks() {
        board = mock(Board.class);
        piece = mock(Piece.class);
    }

    @Test
    public void PieceAt_OccupiedSquare_ReturnsTheBoardsPiece() {
        Piece whiteRook = new Piece(PieceType.ROOK, PieceColor.WHITE);
        expect(board.pieceAt(A1)).andReturn(whiteRook);
        BoardController controller = replayedController();

        assertSame(whiteRook, controller.pieceAt(A1));
        verify(board);
    }

    @Test
    public void PieceAt_EmptySquare_ThrowsIllegalArgumentException() {
        expect(board.pieceAt(A3)).andThrow(new IllegalArgumentException());
        BoardController controller = replayedController();

        assertThrows(IllegalArgumentException.class, () -> controller.pieceAt(A3));
        verify(board);
    }

    @Test
    public void IsOccupied_BoardReportsPiece_ReturnsTrue() {
        expect(board.isOccupied(A1)).andReturn(true);
        BoardController controller = replayedController();

        assertTrue(controller.isOccupied(A1));
        verify(board);
    }

    @Test
    public void IsOccupied_BoardReportsAnEmptySquare_ReturnsFalse() {
        expect(board.isOccupied(A3)).andReturn(false);
        BoardController controller = replayedController();

        assertFalse(controller.isOccupied(A3));
        verify(board);
    }

    @Test
    public void ClickSquare_PieceThenSquareItCanReach_MovesPiece() {
        expectPieceOnB1();
        expect(piece.canMove(B1, C3, board)).andReturn(true);
        board.move(B1, C3);
        BoardController controller = replayedController();

        controller.clickSquare(B1);
        controller.clickSquare(C3);

        verify(board, piece);
    }

    @Test
    public void ClickSquare_PieceThenSquareItCannotReach_LeavesBoardUnchanged() {
        expectPieceOnB1();
        expect(piece.canMove(B1, B3, board)).andReturn(false);
        BoardController controller = replayedController();

        controller.clickSquare(B1);
        controller.clickSquare(B3);

        verify(board, piece);
    }

    @Test
    public void ClickSquare_EmptySquareFirst_SelectsNothing() {
        expect(board.isOccupied(A3)).andReturn(false);
        expect(board.isOccupied(C3)).andReturn(false);
        BoardController controller = replayedController();

        controller.clickSquare(A3);
        controller.clickSquare(C3);

        verify(board, piece);
    }

    private void expectPieceOnB1() {
        expect(board.isOccupied(B1)).andReturn(true);
        expect(board.pieceAt(B1)).andReturn(piece);
    }

    private BoardController replayedController() {
        replay(board, piece);
        return new BoardController(board);
    }
}
