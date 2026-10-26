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
}
