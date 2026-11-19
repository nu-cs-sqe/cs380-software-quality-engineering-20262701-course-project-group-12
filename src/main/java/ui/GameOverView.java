package ui;

import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameOverView {
    private final JFrame gameWindow;

    public GameOverView(JFrame gameWindow) {
        this.gameWindow = gameWindow;
    }

    public GameOverChoice askNextStep(String resultLine) {
        String newGame = Messages.text("gameOver.newGame");
        String close = Messages.text("gameOver.close");
        JOptionPane pane = new JOptionPane(resultLine, JOptionPane.PLAIN_MESSAGE);
        pane.setOptions(new Object[] {newGame, close});
        pane.setInitialValue(newGame);
        pane.createDialog(gameWindow, Messages.text("gameOver.title")).setVisible(true);
        if (newGame.equals(pane.getValue())) {
            return GameOverChoice.NEW_GAME;
        }
        if (close.equals(pane.getValue())) {
            return GameOverChoice.CLOSE;
        }
        return GameOverChoice.KEEP_VIEWING;
    }

    public void returnToWelcome(String playerOneName, String playerTwoName) {
        gameWindow.dispose();
        Main.openWelcomeWindow(playerOneName, playerTwoName);
    }

    public void exitApplication() {
        gameWindow.dispatchEvent(new WindowEvent(gameWindow, WindowEvent.WINDOW_CLOSING));
    }
}
