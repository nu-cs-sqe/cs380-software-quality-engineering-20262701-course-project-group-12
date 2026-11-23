package ui;

import domain.Game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame {
    private static final int PANELS_HEIGHT = 100;

    public GameFrame(Game game) {
        GameStatsController stats = new GameStatsController(game);
        BoardController boardController = new BoardController(game.board(), game);
        GameOverController gameOver =
                new GameOverController(game, stats, new GameOverView(this));
        DrawClaimView drawClaim = new DrawClaimView(boardController, () -> {
            stats.refreshStatus();
            gameOver.showIfOver();
        });
        Runnable afterAction = () -> {
            stats.refreshStatus();
            drawClaim.refresh();
            gameOver.showIfOver();
        };
        arrange(getContentPane(), stats.createView(), new BoardView(boardController, afterAction));
        JPanel endGameControls = new JPanel();
        endGameControls.add(drawClaim);
        endGameControls.add(new ResignView(boardController, afterAction));
        getContentPane().add(endGameControls, BorderLayout.SOUTH);
        setTitle(Messages.text("game.window.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fitToGameWindow(this);
        setLocationRelativeTo(null);
    }

    static void fitToGameWindow(JFrame window) {
        window.getContentPane().setPreferredSize(
                new Dimension(BoardView.SIDE_LENGTH, BoardView.SIDE_LENGTH + PANELS_HEIGHT));
        window.pack();
    }

    static void arrange(Container container, GameStatsView statsView, BoardView boardView) {
        container.setLayout(new BorderLayout());
        container.add(statsView, BorderLayout.NORTH);
        container.add(boardView, BorderLayout.CENTER);
    }
}
