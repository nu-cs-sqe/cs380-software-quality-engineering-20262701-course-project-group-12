package ui;

public class GameStatsView {
    private final String playerOneLine;
    private final String playerTwoLine;
    private final String currentPlayerLine;

    public GameStatsView(String playerOneLine, String playerTwoLine, String currentPlayerLine) {
        this.playerOneLine = playerOneLine;
        this.playerTwoLine = playerTwoLine;
        this.currentPlayerLine = currentPlayerLine;
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
