package ui;

import domain.Game;
import domain.piece.PieceColor;

import java.util.Map;

public class GameStatsController {
    private static final Map<PieceColor, String> CHECKMATE_KEYS = Map.of(
            PieceColor.WHITE, "stats.checkmate.white",
            PieceColor.BLACK, "stats.checkmate.black");

    private final Game game;
    private GameStatsView view;

    public GameStatsController(Game game) {
        if (!game.isStarted()) {
            throw new IllegalArgumentException(
                    "Cannot show game stats before the game has started");
        }
        this.game = game;
    }

    public GameStatsView createView() {
        view = new GameStatsView(
                Messages.format("stats.playerOne", game.playerOneName()),
                Messages.format("stats.playerTwo", game.playerTwoName()),
                currentPlayerLine());
        return view;
    }

    public void refreshStatus() {
        view.showCurrentPlayer(statusLine());
    }

    private String statusLine() {
        if (game.isOver()) {
            return resultLine();
        }
        if (game.isPlayerToMoveInCheck()) {
            return Messages.format("stats.check", game.playerToMoveName());
        }
        return currentPlayerLine();
    }

    private String resultLine() {
        if (game.isDrawnByRepetition()) {
            return Messages.text("stats.drawByRepetition");
        }
        return Messages.format(CHECKMATE_KEYS.get(game.winnerColor()), game.winnerName());
    }

    private String currentPlayerLine() {
        return Messages.format("stats.currentPlayer", game.playerToMoveName());
    }
}
