package ui;

import domain.Game;
import org.junit.jupiter.api.Test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JPanel;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameFrameTests {

    @Test
    void Arrange_StatsAndBoard_PlacesStatsBesideBoard() {
        GameStatsView stats = new GameStatsView("Ada", "Bo");
        Game game = startedGame();
        BoardView board = new BoardView(new BoardController(game.board(), game));
        JPanel content = new JPanel();

        GameFrame.arrange(content, stats, board);

        BorderLayout layout = (BorderLayout) content.getLayout();
        assertSame(stats, layout.getLayoutComponent(BorderLayout.EAST));
        assertSame(board, layout.getLayoutComponent(BorderLayout.CENTER));
    }

    @Test
    public void AddOfferDrawControl_WithOfferDraw_IncludesOfferDraw() {
        Game game = startedGame();
        BoardController boardController = new BoardController(game.board(), game);
        OfferDrawView offerDraw = new OfferDrawView(boardController, () -> { });
        GameStatsView stats = new GameStatsView("Ada", "Bo");

        GameFrame.addOfferDrawControl(stats, offerDraw);

        assertTrue(containsOfferDraw(stats));
    }

    @Test
    public void OnClockTick_WhenCalled_ChecksTheClockThenRunsAfterTick() {
        BoardController boardController = createMock(BoardController.class);
        boardController.checkClock();
        replay(boardController);
        AtomicBoolean afterTickRan = new AtomicBoolean(false);

        GameFrame.onClockTick(boardController, () -> afterTickRan.set(true));

        assertTrue(afterTickRan.get());
        verify(boardController);
    }

    private static boolean containsOfferDraw(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof OfferDrawView) {
                return true;
            }
            if (component instanceof Container && containsOfferDraw((Container) component)) {
                return true;
            }
        }
        return false;
    }

    private static Game startedGame() {
        Game game = new Game();
        game.start("Ada", "Bo");
        return game;
    }
}
