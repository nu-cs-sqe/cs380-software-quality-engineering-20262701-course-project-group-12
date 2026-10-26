
package ui;

import domain.Game;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            WelcomeController controller = new WelcomeController(game);
            WelcomeView view = createWelcomeView(controller);
            controller.setView(view);

            JFrame window = new JFrame("Welcome to Chess");
            window.add(view);
            window.pack();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }

    static WelcomeView createWelcomeView(WelcomeController controller) {
        return new WelcomeView(controller);
    }
}
