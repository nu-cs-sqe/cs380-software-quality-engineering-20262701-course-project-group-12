package domain;

import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTests {
    @Test
    public void Constructor_WithWhitePlayer_StoresNameAndColor() {
        Player player = new Player("A", PieceColor.WHITE);

        assertEquals("A", player.getName());
        assertEquals(PieceColor.WHITE, player.getColor());
    }

    @Test
    public void Constructor_WithBlackPlayer_StoresNameAndColor() {
        Player player = new Player("B", PieceColor.BLACK);

        assertEquals("B", player.getName());
        assertEquals(PieceColor.BLACK, player.getColor());
    }

    @Test
    public void Constructor_WithSurroundingWhitespace_TrimsName() {
        Player player = new Player(" Alice ", PieceColor.WHITE);

        assertEquals("Alice", player.getName());
    }
}
