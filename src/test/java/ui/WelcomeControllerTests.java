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

    private static class RecordingWelcomeView extends WelcomeView {
        private boolean namesRequiredShown;

        @Override
        public void showNamesRequired() {
            namesRequiredShown = true;
        }
    }
}
