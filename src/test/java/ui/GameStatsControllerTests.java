package ui;

import domain.Game;
import domain.Player;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameStatsControllerTests {

    @Test
    void startedGameCreatesController() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        replay(game);

        assertDoesNotThrow(() -> new GameStatsController(game));
    }

    @Test
    void unstartedGameIsRejected() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(false);
        replay(game);

        assertThrows(IllegalArgumentException.class, () -> new GameStatsController(game));
    }

    @Test
    void nullGameIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new GameStatsController(null));
    }

    @Test
    void createViewShowsSetupLines() {
        Player ada = createMock(Player.class);
        Player bo = createMock(Player.class);
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOne()).andStubReturn(ada);
        expect(game.playerTwo()).andStubReturn(bo);
        expect(game.playerToMove()).andStubReturn(ada);
        expect(ada.getName()).andStubReturn("Ada");
        expect(bo.getName()).andStubReturn("Bo");
        replay(game, ada, bo);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Player 1: Ada (White)", view.playerOneLine());
        assertEquals("Player 2: Bo (Black)", view.playerTwoLine());
        assertEquals("Current player: Ada", view.currentPlayerLine());
    }

    @Test
    void createViewShowsOneCharacterNames() {
        Player a = createMock(Player.class);
        Player b = createMock(Player.class);
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOne()).andStubReturn(a);
        expect(game.playerTwo()).andStubReturn(b);
        expect(game.playerToMove()).andStubReturn(a);
        expect(a.getName()).andStubReturn("A");
        expect(b.getName()).andStubReturn("B");
        replay(game, a, b);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Player 1: A (White)", view.playerOneLine());
        assertEquals("Player 2: B (Black)", view.playerTwoLine());
        assertEquals("Current player: A", view.currentPlayerLine());
    }
}
