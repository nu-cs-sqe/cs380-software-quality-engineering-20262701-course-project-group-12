package ui;

import domain.Game;
import org.junit.jupiter.api.Test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JPanel;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class GameFrameTests {

    @Test
    void Arrange_StatsAndBoard_PlacesStatsAboveBoard() {
        GameStatsView stats = new GameStatsView(
                "Player 1: Ada (White)",
                "Player 2: Bo (Black)",
                "Current player: Ada");
        Game game = startedGame();
        BoardView board = new BoardView(new BoardController(game.board(), game));
        JPanel content = new JPanel();

        GameFrame.arrange(content, stats, board);

        BorderLayout layout = (BorderLayout) content.getLayout();
        assertSame(stats, layout.getLayoutComponent(BorderLayout.NORTH));
        assertSame(board, layout.getLayoutComponent(BorderLayout.CENTER));
    }

    @Test
    public void AddEndGameControls_WithOfferDraw_PlacesItInSouth() {
        Game game = startedGame();
        BoardController boardController = new BoardController(game.board(), game);
        DrawClaimView drawClaim = new DrawClaimView(boardController, () -> { });
        OfferDrawView offerDraw = new OfferDrawView(boardController, () -> { });
        JPanel content = new JPanel(new BorderLayout());

        GameFrame.addEndGameControls(
                content, boardController, drawClaim, offerDraw, () -> { });

        assertNotNull(findOfferDraw(content));
    }

    private static OfferDrawView findOfferDraw(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof OfferDrawView) {
                return (OfferDrawView) component;
            }
            if (component instanceof Container) {
                OfferDrawView nested = findOfferDraw((Container) component);
                if (nested != null) {
                    return nested;
                }
            }
        }
        return null;
    }

    private static Game startedGame() {
        Game game = new Game();
        game.start("Ada", "Bo");
        return game;
    }
}
