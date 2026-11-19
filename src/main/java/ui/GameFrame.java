package ui;

import domain.Game;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame {
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
        pack();
        setLocationRelativeTo(null);
    }

    static void arrange(Container container, GameStatsView statsView, BoardView boardView) {
        container.setLayout(new BorderLayout());
        container.add(statsView, BorderLayout.NORTH);
        container.add(boardView, BorderLayout.CENTER);
    }
}
