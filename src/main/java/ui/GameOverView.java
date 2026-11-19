package ui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class GameOverView {
    private static final int NEW_GAME_BUTTON = 0;
    private static final int CLOSE_BUTTON = 1;

    private final JFrame gameWindow;

    public GameOverView(JFrame gameWindow) {
        this.gameWindow = gameWindow;
    }

    public GameOverChoice askNextStep(String resultLine) {
        Object[] buttons = {Messages.text("gameOver.newGame"), Messages.text("gameOver.close")};
        int chosen = JOptionPane.showOptionDialog(
                gameWindow,
                resultLine,
                Messages.text("gameOver.title"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                UIManager.getIcon("OptionPane.informationIcon"),
                buttons,
                buttons[NEW_GAME_BUTTON]);
        if (chosen == NEW_GAME_BUTTON) {
            return GameOverChoice.NEW_GAME;
        }
        if (chosen == CLOSE_BUTTON) {
            return GameOverChoice.CLOSE;
        }
        return GameOverChoice.KEEP_VIEWING;
    }
}
