package domain;

import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTests {
    private static final Location A1 = new Location(0, 1);

    @Test
    public void IsStarted_NewGame_ReturnsFalse() {
        Game game = new Game();

        assertFalse(game.isStarted());
    }

    @Test
    public void start_EmptyPlayerOneName_ThrowsMissingPlayerNameException() {
        Game game = new Game();

        assertThrows(MissingPlayerNameException.class, () -> game.start("", "Bob"));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_BothNamesEmpty_ThrowsMissingPlayerNameException() {
        Game game = new Game();

        assertThrows(MissingPlayerNameException.class, () -> game.start("", ""));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_EmptyPlayerTwoName_ThrowsMissingPlayerNameException() {
        Game game = new Game();

        assertThrows(MissingPlayerNameException.class, () -> game.start("Alice", ""));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_WhitespaceOnlyPlayerOneName_ThrowsMissingPlayerNameException() {
        Game game = new Game();

        assertThrows(MissingPlayerNameException.class, () -> game.start("   ", "Bob"));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_WhitespaceOnlyPlayerTwoName_ThrowsMissingPlayerNameException() {
        Game game = new Game();

        assertThrows(MissingPlayerNameException.class, () -> game.start("Alice", "   "));
        assertFalse(game.isStarted());
    }

    @Test
    public void Start_WithTwoValidNames_StartsGame() {
        Game game = new Game();

        game.start("Alice", "Bob");

        assertTrue(game.isStarted());
    }

    @Test
    public void Start_WithOneCharacterNames_StartsGame() {
        Game game = new Game();

        game.start("A", "B");

        assertTrue(game.isStarted());
    }

    @Test
    public void Accessors_UnstartedGame_ThrowIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::playerOne);
        assertThrows(IllegalStateException.class, game::playerTwo);
        assertThrows(IllegalStateException.class, game::playerToMove);
        assertThrows(IllegalStateException.class, game::board);
    }

    @Test
    public void Accessors_StartedGame_ReturnSetupState() {
        Game game = new Game();

        game.start("Alice", "Bob");

        assertEquals("Alice", game.playerOne().getName());
        assertEquals("Bob", game.playerTwo().getName());
        assertEquals(game.playerOne(), game.playerToMove());
        assertTrue(game.board().isOccupied(A1));
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

        assertTrue(game.board().isOccupied(A1));
    }

    @Test
    public void start_DuplicateNames_ThrowsDuplicatePlayerNameException() {
        Game game = new Game();

        assertThrows(DuplicatePlayerNameException.class, () -> game.start("Alex", "Alex"));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_SameNameDifferentCase_ThrowsDuplicatePlayerNameException() {
        Game game = new Game();

        assertThrows(DuplicatePlayerNameException.class, () -> game.start("Alice", "ALICE"));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_SameNameAfterTrim_ThrowsDuplicatePlayerNameException() {
        Game game = new Game();

        assertThrows(DuplicatePlayerNameException.class, () -> game.start(" Alice ", "Alice"));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_MixedCaseNames_KeepsEnteredCase() {
        Game game = new Game();

        game.start("ALICE", "bob");

        assertEquals("ALICE", game.playerOne().getName());
        assertEquals("bob", game.playerTwo().getName());
    }

    @Test
    public void PlayerOneName_StartedGame_ReturnsPlayerOneName() {
        Game game = new Game();
        game.start("Alice", "Bob");

        String name = game.playerOneName();

        assertEquals("Alice", name);
    }

    @Test
    public void PlayerOneName_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::playerOneName);
    }

    @Test
    public void PlayerTwoName_StartedGame_ReturnsPlayerTwoName() {
        Game game = new Game();
        game.start("Alice", "Bob");

        String name = game.playerTwoName();

        assertEquals("Bob", name);
    }

    @Test
    public void PlayerTwoName_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::playerTwoName);
    }

    @Test
    public void PlayerToMoveName_StartedGame_ReturnsPlayerOneName() {
        Game game = new Game();
        game.start("Alice", "Bob");

        String name = game.playerToMoveName();

        assertEquals("Alice", name);
    }

    @Test
    public void PlayerToMoveName_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::playerToMoveName);
    }

    @Test
    public void ColorToMove_StartedGame_ReturnsWhite() {
        Game game = new Game();
        game.start("Alice", "Bob");

        PieceColor color = game.colorToMove();

        assertEquals(PieceColor.WHITE, color);
    }

    @Test
    public void PassTurn_AfterStart_MakesPlayerTwoToMove() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.passTurn();

        assertSame(game.playerTwo(), game.playerToMove());
        assertEquals("Bob", game.playerToMoveName());
        assertEquals(PieceColor.BLACK, game.colorToMove());
    }

    @Test
    public void PassTurn_Twice_MakesPlayerOneToMove() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.passTurn();
        game.passTurn();

        assertSame(game.playerOne(), game.playerToMove());
        assertEquals(PieceColor.WHITE, game.colorToMove());
    }

    @Test
    public void PassTurn_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::passTurn);
    }
}
