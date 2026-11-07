package ui;

import domain.Board;
import domain.Game;
import domain.Location;
import domain.piece.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

public class MovePieceIntegrationTest {
    private static final Location B1 = new Location(1, 1);
    private static final Location C3 = new Location(2, 3);
    private static final Location D2 = new Location(3, 2);
    private static final Location E2 = new Location(4, 2);
    private static final Location E4 = new Location(4, 4);

    private Board board;
    private BoardController boardController;

    @BeforeEach
    public void startGame() {
        Game game = new Game();
        game.start("Alice", "Bob");
        board = game.board();
        boardController = new BoardController(board);
    }

    @Test
    public void Move_WhitePawnFromE2ToE4_ShowsPawnOnE4() {
        Piece whitePawn = boardController.pieceAt(E2);

        board.move(E2, E4);

        assertSame(whitePawn, boardController.pieceAt(E4));
    }

    @Test
    public void Move_WhitePawnFromE2ToE4_ShowsE2Empty() {
        board.move(E2, E4);

        assertFalse(boardController.isOccupied(E2));
    }

    @Test
    public void ClickSquare_KnightThenSquareItCanReach_MovesKnight() {
        Piece whiteKnight = boardController.pieceAt(B1);

        boardController.clickSquare(B1);
        boardController.clickSquare(C3);

        assertSame(whiteKnight, boardController.pieceAt(C3));
    }

    @Test
    public void ClickSquare_KnightThenOwnPawn_LeavesKnightOnItsSquare() {
        Piece whiteKnight = boardController.pieceAt(B1);

        boardController.clickSquare(B1);
        boardController.clickSquare(D2);

        assertSame(whiteKnight, boardController.pieceAt(B1));
    }
}
