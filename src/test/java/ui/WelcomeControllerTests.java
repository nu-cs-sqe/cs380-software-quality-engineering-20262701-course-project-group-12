package ui;

import domain.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void WelcomeView_ShowNamesRequired_DisplaysRequiredNamesMessage() {
        WelcomeView view = new WelcomeView();

        view.showNamesRequired();

        assertEquals("Both player names are required.", view.errorMessage());
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
        org.junit.jupiter.api.Assertions.assertEquals("Alice", game.playerOne().getName());
        org.junit.jupiter.api.Assertions.assertEquals("Bob", game.playerTwo().getName());
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

        assertEquals("Both player names are required.", view.errorMessage());
        assertTrue(!game.isStarted());
    }

    private static class RecordingWelcomeView extends WelcomeView {
        private boolean namesRequiredShown;
        private boolean closed;
        private boolean gameShown;

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
