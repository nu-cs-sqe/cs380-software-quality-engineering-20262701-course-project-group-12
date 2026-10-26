package ui;

import domain.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WelcomeControllerTests {
    @Test
    public void Constructor_WithStartedGame_RejectsController() {
        Game game = new Game();
        game.start("Alice", "Bob");

        assertThrows(IllegalStateException.class, () -> new WelcomeController(game));
    }
}
