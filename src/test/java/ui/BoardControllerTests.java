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

    private Board board;

    @BeforeEach
    public void createBoardMock() {
        board = mock(Board.class);
    }

    @Test
    public void BoardController_NullBoard_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new BoardController(null));
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
    public void PieceAt_NullLocation_ThrowsIllegalArgumentException() {
        expect(board.pieceAt(null)).andThrow(new IllegalArgumentException());
        BoardController controller = replayedController();

        assertThrows(IllegalArgumentException.class, () -> controller.pieceAt(null));
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
    public void IsOccupied_NullLocation_ThrowsIllegalArgumentException() {
        expect(board.isOccupied(null)).andThrow(new IllegalArgumentException());
        BoardController controller = replayedController();

        assertThrows(IllegalArgumentException.class, () -> controller.isOccupied(null));
        verify(board);
    }

    private BoardController replayedController() {
        replay(board);
        return new BoardController(board);
    }
}
