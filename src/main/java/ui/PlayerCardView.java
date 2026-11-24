package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class PlayerCardView extends JPanel {
    private static final int ICON_SIZE = 44;
    private static final int PADDING = 10;
    private static final int OUTLINE_THICKNESS = 3;
    private static final float NAME_FONT_SIZE = 20f;
    private static final float CAPTION_FONT_SIZE = 12f;
    private static final float CLOCK_FONT_SIZE = 22f;
    private static final Color CARD_BACKGROUND = new Color(252, 248, 240);
    private static final Color CAPTION_INK = new Color(110, 95, 80);

    private final JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, PADDING, 0));
    private final JLabel remainingTime =
            label("", Font.BOLD, CLOCK_FONT_SIZE, BoardView.LABEL_INK);

    public PlayerCardView(String name, String caption, Image pieceImage) {
        setLayout(new BorderLayout(PADDING, PADDING));
        add(iconOf(pieceImage), BorderLayout.WEST);
        add(namePanel(name, caption), BorderLayout.CENTER);
        add(remainingTime, BorderLayout.EAST);
        controls.setOpaque(false);
        add(controls, BorderLayout.SOUTH);
        clearHighlight();
    }

    private static JLabel iconOf(Image pieceImage) {
        Image scaled = pieceImage.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaled));
    }

    private static JPanel namePanel(String name, String caption) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());
        panel.add(label(name, Font.BOLD, NAME_FONT_SIZE, BoardView.LABEL_INK));
        panel.add(label(caption, Font.PLAIN, CAPTION_FONT_SIZE, CAPTION_INK));
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private static JLabel label(String text, int style, float size, Color ink) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(style, size));
        label.setForeground(ink);
        return label;
    }

    public void addControl(JComponent control) {
        controls.add(control);
    }

    public void showRemainingTime(String time) {
        remainingTime.setText(time);
    }

    public String remainingTime() {
        return remainingTime.getText();
    }

    public void highlight() {
        setBackground(BoardView.LIGHT_SQUARE);
        setBorder(outlined(BoardView.DARK_SQUARE));
    }

    public void clearHighlight() {
        setBackground(CARD_BACKGROUND);
        setBorder(outlined(GameStatsView.BACKGROUND));
    }

    private static Border outlined(Color outline) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(outline, OUTLINE_THICKNESS),
                BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }
}
