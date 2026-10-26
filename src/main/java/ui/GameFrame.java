package ui;

import domain.Game;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
    public GameFrame(Game game) {
        super("Chess");
        add(new BoardView(new BoardController(game.board())));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
}
