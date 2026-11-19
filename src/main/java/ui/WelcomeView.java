package ui;

import domain.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class WelcomeView extends JPanel {
    private static final int NAME_COLUMNS = 16;
    private static final int TITLE_FONT_SIZE = 36;
    private static final Color ERROR_INK = new Color(180, 30, 30);
    private static final int GAP = 8;
    private static final int TITLE_GAP_BELOW = 24;
    private static final int LABEL_COLUMN = 0;
    private static final int FIELD_COLUMN = 1;
    private static final int FULL_ROW_WIDTH = 2;
    private static final int TITLE_ROW = 0;
    private static final int PLAYER_ONE_ROW = 1;
    private static final int PLAYER_TWO_ROW = 2;
    private static final int START_ROW = 3;
    private static final int ERROR_ROW = 4;

    private final JTextField playerOneField = new JTextField(NAME_COLUMNS);
    private final JTextField playerTwoField = new JTextField(NAME_COLUMNS);
    private final JButton startButton = new JButton(Messages.text("welcome.start"));
    private final JLabel errorLabel = new JLabel();
    private final Runnable closeWindow;
    private final JPanel form = new JPanel(new GridBagLayout());

    public WelcomeView(WelcomeController controller, Runnable closeWindow) {
        this.closeWindow = closeWindow;
        ActionListener startGame = event ->
                controller.startGame(playerOneField.getText(), playerTwoField.getText());
        startButton.addActionListener(startGame);
        playerOneField.addActionListener(startGame);
        playerTwoField.addActionListener(startGame);
        errorLabel.setForeground(ERROR_INK);
        errorLabel.setVisible(false);
        arrangeForm();
        setLayout(new GridBagLayout());
        add(form);
    }

    private void arrangeForm() {
        JLabel title = new JLabel(Messages.text("welcome.heading"));
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        addFullRow(TITLE_ROW, title, new Insets(0, 0, TITLE_GAP_BELOW, 0));
        addNameRow(PLAYER_ONE_ROW, Messages.text("welcome.playerOneName"), playerOneField);
        addNameRow(PLAYER_TWO_ROW, Messages.text("welcome.playerTwoName"), playerTwoField);
        addFullRow(START_ROW, startButton, new Insets(GAP, 0, 0, 0));
        addFullRow(ERROR_ROW, errorLabel, new Insets(GAP, 0, 0, 0));
    }

    private void addNameRow(int row, String labelText, JTextField field) {
        GridBagConstraints label = cell(row, LABEL_COLUMN, new Insets(GAP, 0, 0, GAP));
        label.anchor = GridBagConstraints.LINE_END;
        form.add(new JLabel(labelText), label);
        form.add(field, cell(row, FIELD_COLUMN, new Insets(GAP, 0, 0, 0)));
    }

    private void addFullRow(int row, JComponent component, Insets insets) {
        GridBagConstraints wholeRow = cell(row, LABEL_COLUMN, insets);
        wholeRow.gridwidth = FULL_ROW_WIDTH;
        form.add(component, wholeRow);
    }

    private static GridBagConstraints cell(int row, int column, Insets insets) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.insets = insets;
        return constraints;
    }

    public void showNamesRequired() {
        showError(Messages.text("welcome.namesRequired"));
    }

    public void showDuplicateNames() {
        showError(Messages.text("welcome.duplicateNames"));
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        form.revalidate();
    }

    public void close() {
        closeWindow.run();
    }

    public void showGame(Game game) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        GameFrame gameFrame = new GameFrame(game);
        gameFrame.setLocation(SwingUtilities.getWindowAncestor(this).getLocation());
        gameFrame.setVisible(true);
    }

    public String errorMessage() {
        return errorLabel.getText();
    }

    boolean isErrorShown() {
        return errorLabel.isVisible();
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
