package ui;

import domain.piece.King;
import domain.piece.PieceColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GameStatsView extends JPanel {
    public enum StatusTone {
        TURN, CHECK, RESULT
    }

    static final int COLUMN_WIDTH = 300;
    static final Color BACKGROUND = new Color(245, 236, 222);
    private static final int GAP = 12;
    private static final float STATUS_FONT_SIZE = 18f;
    private static final Map<StatusTone, Color> TONE_INK = Map.of(
            StatusTone.TURN, BoardView.LABEL_INK,
            StatusTone.CHECK, new Color(180, 30, 30),
            StatusTone.RESULT, new Color(30, 90, 150));

    private final String whiteName;
    private final String blackName;
    private final Map<PieceColor, PlayerCardView> cards = new EnumMap<>(PieceColor.class);
    private final JTextPane statusText = statusText();
    private final JPanel statusControls = new JPanel(new GridBagLayout());
    private String statusLine = "";
    private Optional<PieceColor> highlightedColor = Optional.empty();
    private StatusTone statusTone = StatusTone.TURN;

    public GameStatsView(String whiteName, String blackName) {
        this.whiteName = whiteName;
        this.blackName = blackName;
        PieceImages pieceImages = new PieceImages();
        cards.put(PieceColor.WHITE, new PlayerCardView(whiteName,
                Messages.text("stats.card.white"),
                pieceImages.imageOf(new King(PieceColor.WHITE))));
        cards.put(PieceColor.BLACK, new PlayerCardView(blackName,
                Messages.text("stats.card.black"),
                pieceImages.imageOf(new King(PieceColor.BLACK))));
        arrange();
    }

    private void arrange() {
        setLayout(new BorderLayout(0, GAP));
        setBackground(BACKGROUND);
        setPreferredSize(new Dimension(COLUMN_WIDTH, BoardView.SIDE_LENGTH));
        setBorder(BorderFactory.createEmptyBorder(0, GAP, BoardView.LABEL_MARGIN, GAP));
        add(cards.get(PieceColor.BLACK), BorderLayout.NORTH);
        add(statusArea(), BorderLayout.CENTER);
        add(cards.get(PieceColor.WHITE), BorderLayout.SOUTH);
    }

    private JPanel statusArea() {
        JPanel lineAndControls = new JPanel(new BorderLayout());
        lineAndControls.setOpaque(false);
        statusControls.setOpaque(false);
        lineAndControls.add(statusText, BorderLayout.CENTER);
        lineAndControls.add(statusControls, BorderLayout.SOUTH);
        JPanel centered = new JPanel(new GridBagLayout());
        centered.setOpaque(false);
        GridBagConstraints fullWidth = new GridBagConstraints();
        fullWidth.weightx = 1;
        fullWidth.fill = GridBagConstraints.HORIZONTAL;
        centered.add(lineAndControls, fullWidth);
        return centered;
    }

    private static JTextPane statusText() {
        JTextPane text = new JTextPane();
        text.setEditable(false);
        text.setFocusable(false);
        text.setOpaque(false);
        text.setFont(text.getFont().deriveFont(Font.BOLD, STATUS_FONT_SIZE));
        text.setSize(COLUMN_WIDTH - 2 * GAP, BoardView.SIDE_LENGTH);
        SimpleAttributeSet centered = new SimpleAttributeSet();
        StyleConstants.setAlignment(centered, StyleConstants.ALIGN_CENTER);
        StyledDocument document = text.getStyledDocument();
        document.setParagraphAttributes(0, document.getLength(), centered, false);
        return text;
    }

    public void addPlayerControl(PieceColor color, JComponent control) {
        cards.get(color).addControl(control);
    }

    public void addStatusControl(JComponent control) {
        GridBagConstraints ownRow = new GridBagConstraints();
        ownRow.gridx = 0;
        ownRow.weightx = 1;
        ownRow.fill = GridBagConstraints.HORIZONTAL;
        ownRow.insets = new Insets(GAP / 2, 0, 0, 0);
        statusControls.add(control, ownRow);
    }

    public void showRemainingTime(PieceColor color, String time) {
        cards.get(color).showRemainingTime(time);
    }

    public String remainingTime(PieceColor color) {
        return cards.get(color).remainingTime();
    }

    public void showTurn(PieceColor colorToMove, String turnLine) {
        show(turnLine, Optional.of(colorToMove), StatusTone.TURN);
    }

    public void showCheck(PieceColor colorToMove, String checkLine) {
        show(checkLine, Optional.of(colorToMove), StatusTone.CHECK);
    }

    public void showResult(String resultLine) {
        show(resultLine, Optional.empty(), StatusTone.RESULT);
    }

    private void show(String line, Optional<PieceColor> highlighted, StatusTone tone) {
        statusLine = line;
        highlightedColor = highlighted;
        statusTone = tone;
        statusText.setText(line);
        statusText.setForeground(TONE_INK.get(tone));
        cards.values().forEach(PlayerCardView::clearHighlight);
        highlighted.map(cards::get).ifPresent(PlayerCardView::highlight);
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
