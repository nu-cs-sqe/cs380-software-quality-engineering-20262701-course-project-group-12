package ui;

import domain.Game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            WelcomeController controller = new WelcomeController(game);
            JFrame window = new JFrame(Messages.text("welcome.window.title"));
            WelcomeView view = createWelcomeView(controller, window::dispose);
            controller.setView(view);
            window.add(view);
            window.pack();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }

    static WelcomeView createWelcomeView(WelcomeController controller, Runnable closeWindow) {
        return new WelcomeView(controller, closeWindow);
    }
}
