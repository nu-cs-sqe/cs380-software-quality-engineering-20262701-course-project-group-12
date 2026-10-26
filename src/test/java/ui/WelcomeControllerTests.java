package ui;

import domain.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WelcomeControllerTests {
    @Test
    public void Constructor_WithStartedGame_RejectsController() {
        Game game = new Game();
        game.start("Alice", "Bob");

        assertThrows(IllegalStateException.class, () -> new WelcomeController(game));
    }

    @Test
    public void StartGame_WithMissingPlayerName_ShowsRequiredNames() {
        Game game = new Game();
        WelcomeController controller = new WelcomeController(game);
        RecordingWelcomeView view = new RecordingWelcomeView();
        controller.setView(view);

        controller.startGame("", "Bob");

        assertTrue(view.namesRequiredShown);
        assertTrue(!game.isStarted());
    }

    @Test
    public void StartGame_WithMissingPlayerTwoName_ShowsRequiredNames() {
        Game game = new Game();
        WelcomeController controller = new WelcomeController(game);
        RecordingWelcomeView view = new RecordingWelcomeView();
        controller.setView(view);

        controller.startGame("Alice", "");

        assertTrue(view.namesRequiredShown);
        assertTrue(!game.isStarted());
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
        org.junit.jupiter.api.Assertions.assertEquals("Alice", game.playerOne().getName());
        org.junit.jupiter.api.Assertions.assertEquals("Bob", game.playerTwo().getName());
    }

    private static class RecordingWelcomeView extends WelcomeView {
        private boolean namesRequiredShown;
        private boolean closed;

        @Override
        public void showNamesRequired() {
            namesRequiredShown = true;
        }

        @Override
        public void close() {
            closed = true;
        }
    }
}
