package ui;

import javax.swing.JLabel;

public class GameStatsView extends JLabel {
    public GameStatsView(String text) {
        super(text.replace("\n", "<br>"), javax.swing.SwingConstants.CENTER);
    }

    public String text() {
        return getText().replace("<html>", "").replace("</html>", "").replace("<br>", "\n");
    }
}
