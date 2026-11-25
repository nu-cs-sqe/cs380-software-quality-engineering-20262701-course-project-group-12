package ui;

import domain.Game;

import java.util.Locale;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    private static final String NO_NAME = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            applyChosenLanguage();
            openWelcomeWindow(NO_NAME, NO_NAME);
        });
    }

    static void openWelcomeWindow(String playerOneName, String playerTwoName) {
        Game game = new Game();
        WelcomeController controller = new WelcomeController(game);
        JFrame window = new JFrame(Messages.text("welcome.window.title"));
        WelcomeView view = createWelcomeView(controller, window::dispose);
        view.fillNames(playerOneName, playerTwoName);
        controller.setView(view);
        window.add(view);
        GameFrame.fitToGameWindow(window);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        view.playerOneField().requestFocusInWindow();
    }

    static WelcomeView createWelcomeView(WelcomeController controller, Runnable closeWindow) {
        return new WelcomeView(controller, closeWindow);
    }

    private static void applyChosenLanguage() {
        Locale locale = LanguageView.choose();
        Locale.setDefault(locale);
        JComponent.setDefaultLocale(locale);
        Messages.useLocale(locale);
    }
}
