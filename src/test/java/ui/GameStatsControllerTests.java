package ui;

import domain.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameStatsControllerTests {
    @Test
    public void CreateView_WithStartedGame_ShowsPlayersAndCurrentTurn() {
        Game game = new Game();
        game.start("Alice", "Bob");

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals(
                "Player 1: Alice (White)\nPlayer 2: Bob (Black)\nCurrent player: Alice",
                view.text());
    }
}
