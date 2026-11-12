package ui;

import domain.Game;
import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameStatsControllerTests {

    @Test
    void GameStatsController_StartedGame_IsCreated() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        replay(game);

        assertDoesNotThrow(() -> new GameStatsController(game));
    }

    @Test
    void GameStatsController_UnstartedGame_ThrowsIllegalArgumentException() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(false);
        replay(game);

        assertThrows(IllegalArgumentException.class, () -> new GameStatsController(game));
    }

    @Test
    void CreateView_PlayerOneToMove_ShowsSetupLines() {
        Game game = startedGame("Ada", "Bo", "Ada");

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Player 1: Ada (White)", view.playerOneLine());
        assertEquals("Player 2: Bo (Black)", view.playerTwoLine());
        assertEquals("Current player: Ada", view.currentPlayerLine());
    }

    @Test
    void CreateView_OneCharacterNames_ShowsSetupLines() {
        Game game = startedGame("A", "B", "A");

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Player 1: A (White)", view.playerOneLine());
        assertEquals("Player 2: B (Black)", view.playerTwoLine());
        assertEquals("Current player: A", view.currentPlayerLine());
    }

    @Test
    void CreateView_NamesWithSpaces_ShowsSetupLines() {
        Game game = startedGame("Ann Marie", "Li Wei", "Ann Marie");

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Player 1: Ann Marie (White)", view.playerOneLine());
        assertEquals("Player 2: Li Wei (Black)", view.playerTwoLine());
        assertEquals("Current player: Ann Marie", view.currentPlayerLine());
    }

    @Test
    void CreateView_SimilarButDifferentNames_ShowsBothPlayers() {
        Game game = startedGame("Alex", "Alexa", "Alex");

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Player 1: Alex (White)", view.playerOneLine());
        assertEquals("Player 2: Alexa (Black)", view.playerTwoLine());
        assertEquals("Current player: Alex", view.currentPlayerLine());
    }

    @Test
    void CreateView_PlayerTwoToMove_ShowsPlayerTwoAsCurrent() {
        Game game = startedGame("Ada", "Bo", "Bo");

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Player 1: Ada (White)", view.playerOneLine());
        assertEquals("Player 2: Bo (Black)", view.playerTwoLine());
        assertEquals("Current player: Bo", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_AfterTurn_ShowsPlayerTwo() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andReturn("Ada").andReturn("Bo");
        expect(game.isOver()).andStubReturn(false);
        expect(game.isPlayerToMoveInCheck()).andStubReturn(false);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Current player: Bo", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_PlayerTwoCheckmatesPlayerOne_ShowsPlayerTwoWins() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.winnerName()).andStubReturn("Bo");
        expect(game.winnerColor()).andStubReturn(PieceColor.BLACK);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Checkmate: Bo (Black) wins", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_PlayerOneCheckmatesPlayerTwo_ShowsPlayerOneWins() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.winnerName()).andStubReturn("Ada");
        expect(game.winnerColor()).andStubReturn(PieceColor.WHITE);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Checkmate: Ada (White) wins", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_DrawByRepetition_ShowsTheDraw() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(true);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by repetition", view.currentPlayerLine());
    }

    @Test
    public void RefreshStatus_PlayerToMoveInCheck_ShowsTheCheck() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andReturn("Ada").andReturn("Bo");
        expect(game.isOver()).andStubReturn(false);
        expect(game.isPlayerToMoveInCheck()).andStubReturn(true);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Current player: Bo (in check)", view.currentPlayerLine());
    }

    @Test
    public void RefreshStatus_CheckmatedPlayerInCheck_ShowsTheCheckmate() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isPlayerToMoveInCheck()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.winnerName()).andStubReturn("Bo");
        expect(game.winnerColor()).andStubReturn(PieceColor.BLACK);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Checkmate: Bo (Black) wins", view.currentPlayerLine());
    }

    private static Game startedGame(
            String playerOneName, String playerTwoName, String playerToMoveName) {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn(playerOneName);
        expect(game.playerTwoName()).andStubReturn(playerTwoName);
        expect(game.playerToMoveName()).andStubReturn(playerToMoveName);
        replay(game);
        return game;
    }
}
