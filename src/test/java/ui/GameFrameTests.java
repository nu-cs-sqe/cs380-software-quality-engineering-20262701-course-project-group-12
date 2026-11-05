package ui;

import domain.Board;
import org.junit.jupiter.api.Test;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import static org.junit.jupiter.api.Assertions.assertSame;

public class GameFrameTests {

    @Test
    void Arrange_StatsAndBoard_PlacesStatsAboveBoard() {
        GameStatsView stats = new GameStatsView(
                "Player 1: Ada (White)",
                "Player 2: Bo (Black)",
                "Current player: Ada");
        BoardView board = new BoardView(new BoardController(new Board()));
        JPanel content = new JPanel();

        GameFrame.arrange(content, stats, board);

        BorderLayout layout = (BorderLayout) content.getLayout();
        assertSame(stats, layout.getLayoutComponent(BorderLayout.NORTH));
        assertSame(board, layout.getLayoutComponent(BorderLayout.CENTER));
    }
}
