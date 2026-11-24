package ui;

import domain.Game;
import org.junit.jupiter.api.Test;

import java.awt.BorderLayout;
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
    public void OnClockTick_WhenCalled_ChecksTheClockThenRunsAfterTick() {
        BoardController boardController = createMock(BoardController.class);
        boardController.checkClock();
        replay(boardController);
        AtomicBoolean afterTickRan = new AtomicBoolean(false);

        GameFrame.onClockTick(boardController, () -> afterTickRan.set(true));

        assertTrue(afterTickRan.get());
        verify(boardController);
    }

    private static Game startedGame() {
        Game game = new Game();
        game.start("Ada", "Bo");
        return game;
    }
}
