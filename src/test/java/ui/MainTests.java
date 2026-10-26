package ui;

import domain.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MainTests {
    @Test
    public void CreateWelcomeView_ReturnsViewConnectedToController() {
        Game game = new Game();
        WelcomeController controller = new WelcomeController(game);

        WelcomeView view = Main.createWelcomeView(controller);

        assertNotNull(view);
        controller.setView(view);
        view.playerOneField().setText("Alice");
        view.playerTwoField().setText("Bob");
        view.startButton().doClick();
        org.junit.jupiter.api.Assertions.assertTrue(game.isStarted());
    }
}
