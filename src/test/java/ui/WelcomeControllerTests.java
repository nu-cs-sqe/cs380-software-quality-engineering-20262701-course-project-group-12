package ui;

import domain.Game;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WelcomeControllerTests {
    private static final String NAMES_REQUIRED_MESSAGE = "Both player names are required.";

    @Test
    public void Constructor_WithStartedGame_RejectsController() {
        Game game = new Game();
        game.start("Alice", "Bob");

        assertThrows(IllegalStateException.class, () -> new WelcomeController(game));
    }

    @Test
    public void WelcomeController_NullGame_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WelcomeController(null));
    }

    @Test
    public void StartGame_WithMissingPlayerName_ShowsRequiredNames() {
        Game game = new Game();
        WelcomeController controller = new WelcomeController(game);
        RecordingWelcomeView view = new RecordingWelcomeView();
        controller.setView(view);

        controller.startGame("", "Bob");

        assertTrue(view.namesRequiredShown);
        assertFalse(game.isStarted());
    }

    @Test
    public void StartGame_WithMissingPlayerTwoName_ShowsRequiredNames() {
        Game game = new Game();
        WelcomeController controller = new WelcomeController(game);
        RecordingWelcomeView view = new RecordingWelcomeView();
        controller.setView(view);

        controller.startGame("Alice", "");

        assertTrue(view.namesRequiredShown);
        assertFalse(game.isStarted());
    }

    @Test
    public void WelcomeView_ShowNamesRequired_DisplaysRequiredNamesMessage() {
        WelcomeView view = new WelcomeView(controllerThatIsNeverCalled());

        view.showNamesRequired();

        assertEquals(NAMES_REQUIRED_MESSAGE, view.errorMessage());
    }

    @Test
    public void StartGame_WithValidNames_ClosesWelcomeView() {
        Game game = new Game();
        WelcomeController controller = new WelcomeController(game);
        RecordingWelcomeView view = new RecordingWelcomeView();
        controller.setView(view);

        controller.startGame("Alice", "Bob");

        assertTrue(view.closed);
        assertTrue(game.isStarted());
        assertTrue(view.gameShown);
    }

    @Test
    public void StartGame_WithSurroundingWhitespace_PassesNamesToGame() {
        Game game = new Game();
        WelcomeController controller = new WelcomeController(game);
        RecordingWelcomeView view = new RecordingWelcomeView();
        controller.setView(view);

        controller.startGame(" Alice ", "\tBob\n");

        assertTrue(game.isStarted());
        assertTrue(view.closed);
        assertEquals("Alice", game.playerOne().getName());
        assertEquals("Bob", game.playerTwo().getName());
    }

    @Test
    public void StartButton_WithValidNames_StartsGame() {
        Game game = new Game();
        WelcomeController controller = new WelcomeController(game);
        WelcomeView view = new WelcomeView(controller);
        controller.setView(view);

        view.playerOneField().setText("Alice");
        view.playerTwoField().setText("Bob");
        view.startButton().doClick();

        assertTrue(game.isStarted());
    }

    @Test
    public void StartButton_WithMissingName_ShowsRequiredNames() {
        Game game = new Game();
        WelcomeController controller = new WelcomeController(game);
        WelcomeView view = new WelcomeView(controller);
        controller.setView(view);

        view.playerOneField().setText("Alice");
        view.startButton().doClick();

        assertEquals(NAMES_REQUIRED_MESSAGE, view.errorMessage());
        assertFalse(game.isStarted());
    }

    private static WelcomeController controllerThatIsNeverCalled() {
        WelcomeController controller = mock(WelcomeController.class);
        replay(controller);
        return controller;
    }

    private static class RecordingWelcomeView extends WelcomeView {
        private boolean namesRequiredShown;
        private boolean closed;
        private boolean gameShown;

        RecordingWelcomeView() {
            super(controllerThatIsNeverCalled());
        }

        @Override
        public void showNamesRequired() {
            namesRequiredShown = true;
        }

        @Override
        public void close() {
            closed = true;
        }

        @Override
        public void showGame(Game game) {
            gameShown = true;
        }
    }
}
