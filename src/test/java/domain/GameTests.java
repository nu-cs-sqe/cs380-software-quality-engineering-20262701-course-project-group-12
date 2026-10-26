package domain;

import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void Start_WithTwoValidNames_StartsGame() {
        Game game = new Game();

        assertTrue(game.start("Alice", "Bob"));
        assertTrue(game.isStarted());
    }

    @Test
    public void Start_WithValidNames_CreatesWhitePlayerOne() {
        Game game = new Game();

        game.start(" Alice ", "Bob");

        assertEquals("Alice", game.playerOne().getName());
        assertEquals(PieceColor.WHITE, game.playerOne().getColor());
    }

    @Test
    public void Start_WithValidNames_CreatesBlackPlayerTwo() {
        Game game = new Game();

        game.start("Alice", " Bob ");

        assertEquals("Bob", game.playerTwo().getName());
        assertEquals(PieceColor.BLACK, game.playerTwo().getColor());
    }
}
