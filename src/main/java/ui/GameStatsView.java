package ui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameStatsView extends JPanel {
    private final String playerOneLine;
    private final String playerTwoLine;
    private final JLabel currentPlayerLabel;
    private String currentPlayerLine;

    public GameStatsView(String playerOneLine, String playerTwoLine, String currentPlayerLine) {
        this.playerOneLine = playerOneLine;
        this.playerTwoLine = playerTwoLine;
        this.currentPlayerLine = currentPlayerLine;
        this.currentPlayerLabel = new JLabel(currentPlayerLine);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel(playerOneLine));
        add(new JLabel(playerTwoLine));
        add(currentPlayerLabel);
    }

    public void showCurrentPlayer(String currentPlayerLine) {
        this.currentPlayerLine = currentPlayerLine;
        currentPlayerLabel.setText(currentPlayerLine);
    }

    public String playerOneLine() {
        return playerOneLine;
    }

    public String playerTwoLine() {
        return playerTwoLine;
    }

    public String currentPlayerLine() {
        return currentPlayerLine;
    }
}
