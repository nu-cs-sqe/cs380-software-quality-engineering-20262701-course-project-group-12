package domain;

import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTests {
    @Test
    public void NewGame_IsNotStarted() {
        Game game = new Game();

        assertFalse(game.isStarted());
    }

    @Test
    public void Start_WithEmptyPlayerOneName_RejectsStart() {
        Game game = new Game();

        assertFalse(game.start("", "Bob"));
        assertFalse(game.isStarted());
    }

    @Test
    public void Start_WithBothNamesEmpty_RejectsStart() {
        Game game = new Game();

        assertFalse(game.start("", ""));
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
    public void Start_WithWhitespaceOnlyPlayerTwoName_RejectsStart() {
        Game game = new Game();

        assertFalse(game.start("Alice", "   "));
        assertFalse(game.isStarted());
    }

    @Test
    public void Start_WithTwoValidNames_StartsGame() {
        Game game = new Game();

        assertTrue(game.start("Alice", "Bob"));
        assertTrue(game.isStarted());
    }

    @Test
    public void Start_WithOneCharacterNames_StartsGame() {
        Game game = new Game();

        assertTrue(game.start("A", "B"));
        assertTrue(game.isStarted());
    }

    @Test
    public void NewGame_AccessorsRejectAccessBeforeStart() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::playerOne);
        assertThrows(IllegalStateException.class, game::playerTwo);
        assertThrows(IllegalStateException.class, game::playerToMove);
        assertThrows(IllegalStateException.class, game::board);
    }

    @Test
    public void StartedGame_AccessorsReturnSetupState() {
        Game game = new Game();

        assertTrue(game.start("Alice", "Bob"));

        assertEquals("Alice", game.playerOne().getName());
        assertEquals("Bob", game.playerTwo().getName());
        assertEquals(game.playerOne(), game.playerToMove());
        assertTrue(game.board() != null);
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

    @Test
    public void Start_WithValidNames_SetsPlayerOneToMove() {
        Game game = new Game();

        game.start("Alice", "Bob");

        assertEquals(game.playerOne(), game.playerToMove());
    }

    @Test
    public void Start_WithValidNames_CreatesBoard() {
        Game game = new Game();

        game.start("Alice", "Bob");

        assertTrue(game.board() != null);
    }

    @Test
    public void Start_WithDuplicateNames_AllowsStart() {
        Game game = new Game();

        assertTrue(game.start("Alex", "Alex"));
        assertEquals("Alex", game.playerOne().getName());
        assertEquals("Alex", game.playerTwo().getName());
    }
}
