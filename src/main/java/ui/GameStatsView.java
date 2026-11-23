package ui;

import domain.piece.PieceColor;

import java.util.Optional;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameStatsView extends JPanel {
    public enum StatusTone {
        TURN, CHECK, RESULT
    }

    private final String whiteName;
    private final String blackName;
    private final JLabel currentPlayerLabel;
    private String currentPlayerLine;
    private Optional<PieceColor> highlightedColor = Optional.empty();
    private StatusTone statusTone = StatusTone.TURN;

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

    public void showTurn(PieceColor colorToMove, String turnLine) {
        showCurrentPlayer(turnLine);
        highlightedColor = Optional.of(colorToMove);
        statusTone = StatusTone.TURN;
    }

    public void showCheck(PieceColor colorToMove, String checkLine) {
        showCurrentPlayer(checkLine);
        highlightedColor = Optional.of(colorToMove);
        statusTone = StatusTone.CHECK;
    }

    public void showResult(String resultLine) {
        showCurrentPlayer(resultLine);
        highlightedColor = Optional.empty();
        statusTone = StatusTone.RESULT;
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

    public Optional<PieceColor> highlightedColor() {
        return highlightedColor;
    }

    public StatusTone statusTone() {
        return statusTone;
    }
}
