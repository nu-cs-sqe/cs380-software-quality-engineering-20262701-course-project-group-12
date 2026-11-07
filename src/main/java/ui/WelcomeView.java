package ui;

import domain.Game;

import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WelcomeView extends JPanel {
    private static final int ANY_NUMBER_OF_ROWS = 0;
    private static final int LABEL_AND_FIELD_COLUMNS = 2;

    private final JTextField playerOneField = new JTextField();
    private final JTextField playerTwoField = new JTextField();
    private final JButton startButton = new JButton(Messages.text("welcome.start"));
    private final JLabel errorLabel = new JLabel();
    private final Runnable closeWindow;

    public WelcomeView(WelcomeController controller, Runnable closeWindow) {
        this.closeWindow = closeWindow;
        setLayout(new GridLayout(ANY_NUMBER_OF_ROWS, LABEL_AND_FIELD_COLUMNS));
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

    public void showDuplicateNames() {
        errorLabel.setText(Messages.text("welcome.duplicateNames"));
    }

    public void close() {
        closeWindow.run();
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
