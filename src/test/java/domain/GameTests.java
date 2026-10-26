package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameTests {
    @Test
    public void Start_WithEmptyPlayerOneName_RejectsStart() {
        Game game = new Game();

        assertFalse(game.start("", "Bob"));
        assertFalse(game.isStarted());
    }

    @Test
    public void Start_WithEmptyPlayerTwoName_RejectsStart() {
        Game game = new Game();

        assertFalse(game.start("Alice", ""));
        assertFalse(game.isStarted());
    }

    @Test
    public void Start_WithWhitespaceOnlyPlayerOneName_RejectsStart() {
        Game game = new Game();

        assertFalse(game.start("   ", "Bob"));
        assertFalse(game.isStarted());
    }
}
