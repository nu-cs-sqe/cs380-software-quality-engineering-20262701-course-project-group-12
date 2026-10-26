package ui;

import domain.Game;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
}
