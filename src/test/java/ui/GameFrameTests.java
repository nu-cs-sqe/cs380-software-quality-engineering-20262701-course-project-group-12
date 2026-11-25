package ui;

import domain.Game;
import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
    public void AddPlayerControls_WhiteAndBlack_PutsEachOfferDrawButtonOnItsOwnCard() {
        Game game = startedGame();
        BoardController boardController = new BoardController(game.board(), game);
        ResignView resign = new ResignView(boardController, () -> { });
        OfferDrawView offerDraw = new OfferDrawView(boardController, () -> { });
        GameStatsView stats = new GameStatsView("Ada", "Bo");

        GameFrame.addPlayerControls(stats, resign, offerDraw);

        BorderLayout layout = (BorderLayout) stats.getLayout();
        assertSame(layout.getLayoutComponent(BorderLayout.SOUTH),
                cardHolding(offerDraw.buttonFor(PieceColor.WHITE)));
        assertSame(layout.getLayoutComponent(BorderLayout.NORTH),
                cardHolding(offerDraw.buttonFor(PieceColor.BLACK)));
    }

    @Test
    public void AddPlayerControls_WhiteAndBlack_PutsEachResignButtonOnItsOwnCard() {
        Game game = startedGame();
        BoardController boardController = new BoardController(game.board(), game);
        ResignView resign = new ResignView(boardController, () -> { });
        OfferDrawView offerDraw = new OfferDrawView(boardController, () -> { });
        GameStatsView stats = new GameStatsView("Ada", "Bo");

        GameFrame.addPlayerControls(stats, resign, offerDraw);

        BorderLayout layout = (BorderLayout) stats.getLayout();
        assertSame(layout.getLayoutComponent(BorderLayout.SOUTH),
                cardHolding(resign.buttonFor(PieceColor.WHITE)));
        assertSame(layout.getLayoutComponent(BorderLayout.NORTH),
                cardHolding(resign.buttonFor(PieceColor.BLACK)));
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

    private static Component cardHolding(Component control) {
        return SwingUtilities.getAncestorOfClass(PlayerCardView.class, control);
    }

    private static Game startedGame() {
        Game game = new Game();
        game.start("Ada", "Bo");
        return game;
    }
}
