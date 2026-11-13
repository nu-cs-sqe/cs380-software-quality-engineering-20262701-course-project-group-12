package ui;

import domain.Game;
import domain.Location;
import domain.piece.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class KeepKingOutOfCheckIntegrationTest {
    private static final Location B5 = new Location(1, 5);
    private static final Location B8 = new Location(1, 8);
    private static final Location C6 = new Location(2, 6);
    private static final Location D1 = new Location(3, 1);
    private static final Location D4 = new Location(3, 4);
    private static final Location D6 = new Location(3, 6);
    private static final Location D7 = new Location(3, 7);
    private static final Location E2 = new Location(4, 2);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location E7 = new Location(4, 7);
    private static final Location F1 = new Location(5, 1);
    private static final Location F3 = new Location(5, 3);
    private static final Location F6 = new Location(5, 6);
    private static final Location F7 = new Location(5, 7);
    private static final Location G1 = new Location(6, 1);
    private static final Location H5 = new Location(7, 5);

    private Game game;
    private BoardController boardController;
    private GameStatsController statsController;
    private GameStatsView statsView;

    @BeforeEach
    public void startGame() {
        game = new Game();
        game.start("Alice", "Bob");
        boardController = new BoardController(game.board(), game);
        statsController = new GameStatsController(game);
        statsView = statsController.createView();
    }

    @Test
    public void ClickSquare_PinnedKnightOffTheLine_LeavesTheKnightOnItsSquare() {
        clickFromTo(E2, E4);
        clickFromTo(E7, E5);
        clickFromTo(G1, F3);
        clickFromTo(B8, C6);
        clickFromTo(F1, B5);
        clickFromTo(D7, D6);
        Piece pinnedKnight = boardController.pieceAt(C6);

        clickFromTo(C6, D4);

        assertSame(pinnedKnight, boardController.pieceAt(C6));
    }

    @Test
    public void RefreshStatus_AfterQueenChecksBlack_ShowsBlackInCheck() {
        clickFromTo(E2, E4);
        clickFromTo(F7, F6);
        clickFromTo(D1, H5);

        statsController.refreshStatus();

        assertEquals("Current player: Bob (in check)", statsView.currentPlayerLine());
    }

    private void clickFromTo(Location from, Location to) {
        boardController.clickSquare(from);
        boardController.clickSquare(to);
    }
}
