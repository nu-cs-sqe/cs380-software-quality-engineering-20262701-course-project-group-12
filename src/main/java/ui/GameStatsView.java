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
    private final JLabel statusLabel;
    private String statusLine;
    private Optional<PieceColor> highlightedColor = Optional.empty();
    private StatusTone statusTone = StatusTone.TURN;

    public GameStatsView(String whiteName, String blackName) {
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.statusLine = "";
        this.statusLabel = new JLabel();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel(blackName));
        add(statusLabel);
        add(new JLabel(whiteName));
    }

    private void showStatusLine(String statusLine) {
        this.statusLine = statusLine;
        statusLabel.setText(statusLine);
    }

    public void showTurn(PieceColor colorToMove, String turnLine) {
        showStatusLine(turnLine);
        highlightedColor = Optional.of(colorToMove);
        statusTone = StatusTone.TURN;
    }

    public void showCheck(PieceColor colorToMove, String checkLine) {
        showStatusLine(checkLine);
        highlightedColor = Optional.of(colorToMove);
        statusTone = StatusTone.CHECK;
    }

    public void showResult(String resultLine) {
        showStatusLine(resultLine);
        highlightedColor = Optional.empty();
        statusTone = StatusTone.RESULT;
    }

    public String whiteName() {
        return whiteName;
    }

    public String blackName() {
        return blackName;
    }

    public String statusLine() {
        return statusLine;
    }

    public Optional<PieceColor> highlightedColor() {
        return highlightedColor;
    }

    public StatusTone statusTone() {
        return statusTone;
    }
}
