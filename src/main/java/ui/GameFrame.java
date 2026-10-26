package ui;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Container;

public class GameFrame extends JFrame {
    public GameFrame(GameStatsView statsView, BoardView boardView) {
        arrange(getContentPane(), statsView, boardView);
    }

    static void arrange(Container container, GameStatsView statsView, BoardView boardView) {
        container.setLayout(new BorderLayout());
        container.add(statsView, BorderLayout.NORTH);
        container.add(boardView, BorderLayout.CENTER);
    }
}
