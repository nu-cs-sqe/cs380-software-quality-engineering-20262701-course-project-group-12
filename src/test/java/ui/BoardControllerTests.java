package ui;

import domain.Board;
import domain.Location;
import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardControllerTests {

    @Test
    public void BoardController_WithBoard_IsCreated() {
        Board board = mock(Board.class);
        replay(board);

        BoardController controller = new BoardController(board);

        assertNotNull(controller);
        verify(board);
    }

    @Test
    public void BoardController_NullBoard_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new BoardController(null));
    }

    @Test
    public void PieceAt_OccupiedSquare_ReturnsTheBoardsPiece() {
        Board board = mock(Board.class);
        Location a1 = new Location(0, 1);
        Piece whiteRook = new Piece(PieceType.ROOK, PieceColor.WHITE);
        expect(board.pieceAt(a1)).andReturn(whiteRook);
        replay(board);

        BoardController controller = new BoardController(board);

        assertSame(whiteRook, controller.pieceAt(a1));
        verify(board);
    }

    @Test
    public void PieceAt_EmptySquare_ReturnsNull() {
        Board board = mock(Board.class);
        Location a3 = new Location(0, 3);
        expect(board.pieceAt(a3)).andReturn(null);
        replay(board);

        BoardController controller = new BoardController(board);

        assertNull(controller.pieceAt(a3));
        verify(board);
    }
}
