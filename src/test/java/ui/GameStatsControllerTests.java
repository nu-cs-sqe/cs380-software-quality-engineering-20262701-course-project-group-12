package ui;

import domain.Game;
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
    void GameStatsController_NullGame_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new GameStatsController(null));
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
