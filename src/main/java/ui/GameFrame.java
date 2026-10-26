package ui;

import domain.Game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class GameFrame extends JFrame {
    public GameFrame(Game game) {
        super("Chess");
        setLayout(new BorderLayout());
        add(new GameStatsController(game).createView(), BorderLayout.NORTH);
        add(new BoardView(new BoardController(game.board())), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
}
