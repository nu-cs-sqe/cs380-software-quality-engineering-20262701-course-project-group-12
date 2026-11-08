package ui;

import domain.Game;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;

public class GameFrame extends JFrame {
    public GameFrame(Game game) {
        this(game, new GameStatsController(game));
    }

    private GameFrame(Game game, GameStatsController stats) {
        this(
                stats.createView(),
                new BoardView(
                        new BoardController(game.board(), game),
                        stats::refreshCurrentPlayer));
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
}
