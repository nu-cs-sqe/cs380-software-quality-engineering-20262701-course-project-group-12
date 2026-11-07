package ui;

import domain.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartGameIntegrationTest {
    private static final String NAMES_REQUIRED_MESSAGE = "Both player names are required.";
    private static final Runnable NO_WINDOW = () -> { };

    private Game game;
    private WelcomeView welcomeView;

    @BeforeEach
    public void openWelcomeScreen() {
        game = new Game();
        WelcomeController controller = new WelcomeController(game);
        welcomeView = new WelcomeView(controller, NO_WINDOW);
        controller.setView(welcomeView);
    }

    @Test
    public void StartButton_WithValidNames_StartsGame() {
        enterNames("Alice", "Bob");

        welcomeView.startButton().doClick();

        assertTrue(game.isStarted());
    }

    @Test
    public void StartButton_WithValidNames_ShowsPlayersInGameStats() {
        enterNames(" Alice ", "Bob");

        welcomeView.startButton().doClick();

        GameStatsView statsView = new GameStatsController(game).createView();
        assertEquals("Player 1: Alice (White)", statsView.playerOneLine());
        assertEquals("Player 2: Bob (Black)", statsView.playerTwoLine());
        assertEquals("Current player: Alice", statsView.currentPlayerLine());
    }

    @Test
    public void StartButton_WithMissingName_ShowsRequiredNames() {
        enterNames("Alice", "");

        welcomeView.startButton().doClick();

        assertEquals(NAMES_REQUIRED_MESSAGE, welcomeView.errorMessage());
        assertFalse(game.isStarted());
    }

    private void enterNames(String playerOneName, String playerTwoName) {
        welcomeView.playerOneField().setText(playerOneName);
        welcomeView.playerTwoField().setText(playerTwoName);
    }
}
