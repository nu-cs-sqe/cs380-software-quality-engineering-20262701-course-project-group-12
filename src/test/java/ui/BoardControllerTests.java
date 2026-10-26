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
}
