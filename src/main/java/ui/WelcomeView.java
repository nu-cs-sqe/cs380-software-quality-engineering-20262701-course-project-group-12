package ui;

import domain.Game;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Window;

public class WelcomeView extends JPanel {
    private static final String REQUIRED_NAMES_MESSAGE = "Both player names are required.";

    private final JTextField playerOneField = new JTextField();
    private final JTextField playerTwoField = new JTextField();
    private final JButton startButton = new JButton("Start Game");
    private final JLabel errorLabel = new JLabel();

    public WelcomeView() {
        this(null);
    }

    public WelcomeView(WelcomeController controller) {
        setLayout(new GridLayout(0, 2));
        add(new JLabel("Player 1 name"));
        add(playerOneField);
        add(new JLabel("Player 2 name"));
        add(playerTwoField);
        add(new JLabel());
        add(startButton);
        add(new JLabel());
        add(errorLabel);
        startButton.addActionListener(event -> {
            if (controller != null) {
                controller.startGame(playerOneField.getText(), playerTwoField.getText());
            }
        });
    }

    public void showNamesRequired() {
        errorLabel.setText(REQUIRED_NAMES_MESSAGE);
    }

    public void close() {
        Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }

    public void showGame(Game game) {
        new GameFrame(game).setVisible(true);
    }

    public String errorMessage() {
        return errorLabel.getText();
    }

    JTextField playerOneField() {
        return playerOneField;
    }

    JTextField playerTwoField() {
        return playerTwoField;
    }

    JButton startButton() {
        return startButton;
    }
}
