package ui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameStatsView extends JPanel {
    private final String whiteName;
    private final String blackName;
    private final JLabel currentPlayerLabel;
    private String currentPlayerLine;

    public GameStatsView(String whiteName, String blackName) {
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.currentPlayerLine = "";
        this.currentPlayerLabel = new JLabel();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel(blackName));
        add(currentPlayerLabel);
        add(new JLabel(whiteName));
    }

    public void showCurrentPlayer(String currentPlayerLine) {
        this.currentPlayerLine = currentPlayerLine;
        currentPlayerLabel.setText(currentPlayerLine);
    }

    public String whiteName() {
        return whiteName;
    }

    public String blackName() {
        return blackName;
    }

    public String currentPlayerLine() {
        return currentPlayerLine;
    }
}
