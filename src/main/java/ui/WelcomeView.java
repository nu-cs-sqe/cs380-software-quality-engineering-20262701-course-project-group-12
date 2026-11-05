package ui;

import domain.Game;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.Optional;

public class WelcomeView extends JPanel {
    private final JTextField playerOneField = new JTextField();
    private final JTextField playerTwoField = new JTextField();
    private final JButton startButton = new JButton(Messages.text("welcome.start"));
    private final JLabel errorLabel = new JLabel();

    public WelcomeView(WelcomeController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("Cannot create the welcome view without a controller");
        }
        setLayout(new GridLayout(0, 2));
        add(new JLabel(Messages.text("welcome.playerOneName")));
        add(playerOneField);
        add(new JLabel(Messages.text("welcome.playerTwoName")));
        add(playerTwoField);
        add(new JLabel());
        add(startButton);
        add(new JLabel());
        add(errorLabel);
        startButton.addActionListener(event ->
                controller.startGame(playerOneField.getText(), playerTwoField.getText()));
    }

    public void showNamesRequired() {
        errorLabel.setText(Messages.text("welcome.namesRequired"));
    }

    public void close() {
        Optional.ofNullable(SwingUtilities.getWindowAncestor(this)).ifPresent(Window::dispose);
    }

    public void showGame(Game game) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
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
