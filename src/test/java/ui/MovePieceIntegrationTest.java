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
    private static final Location E1 = new Location(4, 1);
    private static final Location E2 = new Location(4, 2);
    private static final Location E4 = new Location(4, 4);
    private static final Location F1 = new Location(5, 1);
    private static final Location F3 = new Location(5, 3);
    private static final Location F4 = new Location(5, 4);
    private static final Location G1 = new Location(6, 1);
    private static final Location G4 = new Location(6, 4);
    private static final Location H1 = new Location(7, 1);

    private Board board;
    private BoardController boardController;

    @BeforeEach
    public void startGame() {
        Game game = new Game();
        game.start("Alice", "Bob");
        board = game.board();
        boardController = new BoardController(board, game);
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
    public void ClickSquare_WhitePawnE2ThenE4_MovesThePawnToE4() {
        Piece whitePawn = boardController.pieceAt(E2);

        clickFromTo(E2, E4);

        assertSame(whitePawn, boardController.pieceAt(E4));
    }

    @Test
    public void ClickSquare_WhiteKnightG1ThenF3_MovesTheKnightToF3() {
        Piece whiteKnight = boardController.pieceAt(G1);

        clickFromTo(G1, F3);

        assertSame(whiteKnight, boardController.pieceAt(F3));
    }

    @Test
    public void ClickSquare_WhiteKingE1ThenG1_CastlesTheRookToF1() {
        board.move(F1, F4);
        board.move(G1, G4);
        Piece kingsideRook = boardController.pieceAt(H1);

        clickFromTo(E1, G1);

        assertSame(kingsideRook, boardController.pieceAt(F1));
    }

    private void clickFromTo(Location from, Location to) {
        boardController.clickSquare(from);
        boardController.clickSquare(to);
    }
}
