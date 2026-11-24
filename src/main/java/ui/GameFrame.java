package ui;

import domain.Game;
import domain.piece.PieceColor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.Timer;

public class GameFrame extends JFrame {
    private static final int CLOCK_TICK_MILLIS = 1000;

    public GameFrame(Game game) {
        GameStatsController stats = new GameStatsController(game);
        BoardController boardController = new BoardController(game.board(), game);
        GameOverController gameOver =
                new GameOverController(game, stats, new GameOverView(this));
        DrawClaimView drawClaim = new DrawClaimView(boardController, () -> {
            stats.refreshStatus();
            gameOver.showIfOver();
        });
        OfferDrawView offerDraw = new OfferDrawView(boardController, () -> {
            stats.refreshStatus();
            drawClaim.refresh();
            gameOver.showIfOver();
        });
        Runnable afterAction = () -> {
            stats.refreshStatus();
            drawClaim.refresh();
            offerDraw.refresh();
            gameOver.showIfOver();
        };
        GameStatsView statsView = stats.createView();
        ResignView resign = new ResignView(boardController, afterAction);
        addPlayerControls(statsView, resign, offerDraw);
        statsView.addStatusControl(drawClaim);
        arrange(getContentPane(), statsView, new BoardView(boardController, afterAction));
        startClockTicker(boardController, afterAction);
        setTitle(Messages.text("game.window.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fitToGameWindow(this);
        setLocationRelativeTo(null);
    }

    static void onClockTick(BoardController boardController, Runnable afterTick) {
        boardController.checkClock();
        afterTick.run();
    }

    private static void startClockTicker(BoardController boardController, Runnable afterTick) {
        Timer ticker = new Timer(CLOCK_TICK_MILLIS,
                event -> onClockTick(boardController, afterTick));
        ticker.start();
    }

    static void fitToGameWindow(JFrame window) {
        window.getContentPane().setPreferredSize(
                new Dimension(
                        BoardView.SIDE_LENGTH + GameStatsView.COLUMN_WIDTH, BoardView.SIDE_LENGTH));
        window.pack();
    }

    static void arrange(Container container, GameStatsView statsView, BoardView boardView) {
        container.setLayout(new BorderLayout());
        container.add(statsView, BorderLayout.EAST);
        container.add(boardView, BorderLayout.CENTER);
    }

    static void addPlayerControls(
            GameStatsView statsView, ResignView resign, OfferDrawView offerDraw) {
        for (PieceColor color : PieceColor.values()) {
            statsView.addPlayerControl(color, resign.buttonFor(color));
            statsView.addPlayerControl(color, offerDraw.buttonFor(color));
        }
    }
}
