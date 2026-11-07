package ui;

import domain.Game;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTests {
    @Test
    public void CreateWelcomeView_WithController_ReturnsConnectedView() {
        Game game = new Game();
        WelcomeController controller = new WelcomeController(game);

        AtomicBoolean windowClosed = new AtomicBoolean();
        WelcomeView view = Main.createWelcomeView(controller, () -> windowClosed.set(true));

        controller.setView(view);
        view.playerOneField().setText("Alice");
        view.playerTwoField().setText("Bob");
        view.startButton().doClick();
        assertTrue(game.isStarted());
        assertTrue(windowClosed.get());
    }
}
