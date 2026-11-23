package ui;

import domain.Game;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame {
    public GameFrame(Game game) {
        this(game, new GameStatsController(game));
    }

    private GameFrame(Game game, GameStatsController stats) {
        this(stats, new BoardController(game.board(), game));
    }

    private GameFrame(GameStatsController stats, BoardController boardController) {
        this(stats, boardController, new DrawClaimView(boardController, stats::refreshStatus));
    }

    private GameFrame(
            GameStatsController stats, BoardController boardController, DrawClaimView drawClaim) {
        this(stats, boardController, drawClaim, new StatusRefresh(stats, drawClaim));
    }

    private GameFrame(
            GameStatsController stats,
            BoardController boardController,
            DrawClaimView drawClaim,
            StatusRefresh statusRefresh) {
        this(
                stats,
                boardController,
                drawClaim,
                statusRefresh,
                new OfferDrawView(boardController, statusRefresh));
    }

    private GameFrame(
            GameStatsController stats,
            BoardController boardController,
            DrawClaimView drawClaim,
            StatusRefresh statusRefresh,
            OfferDrawView offerDraw) {
        this(stats.createView(), new BoardView(boardController, statusRefresh));
        statusRefresh.setOfferDraw(offerDraw);
        addEndGameControls(
                getContentPane(), boardController, drawClaim, offerDraw, statusRefresh);
        setTitle(Messages.text("game.window.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public GameFrame(GameStatsView statsView, BoardView boardView) {
        arrange(getContentPane(), statsView, boardView);
    }

    static void arrange(Container container, GameStatsView statsView, BoardView boardView) {
        container.setLayout(new BorderLayout());
        container.add(statsView, BorderLayout.NORTH);
        container.add(boardView, BorderLayout.CENTER);
    }

    static void addEndGameControls(
            Container container,
            BoardController boardController,
            DrawClaimView drawClaim,
            OfferDrawView offerDraw,
            Runnable statusRefresh) {
        JPanel endGameControls = new JPanel();
        endGameControls.add(drawClaim);
        endGameControls.add(offerDraw);
        endGameControls.add(new ResignView(boardController, statusRefresh));
        container.add(endGameControls, BorderLayout.SOUTH);
    }

    private static final class StatusRefresh implements Runnable {
        private final GameStatsController stats;
        private final DrawClaimView drawClaim;
        private OfferDrawView offerDraw;

        StatusRefresh(GameStatsController stats, DrawClaimView drawClaim) {
            this.stats = stats;
            this.drawClaim = drawClaim;
        }

        void setOfferDraw(OfferDrawView offerDraw) {
            this.offerDraw = offerDraw;
        }

        @Override
        public void run() {
            stats.refreshStatus();
            drawClaim.refresh();
            if (offerDraw != null) {
                offerDraw.refresh();
            }
        }
    }
}
