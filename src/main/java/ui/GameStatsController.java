package ui;

import domain.Game;

public class GameStatsController {
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

    public void refreshCurrentPlayer() {
        view.showCurrentPlayer(currentPlayerLine());
    }

    private String currentPlayerLine() {
        return Messages.format("stats.currentPlayer", game.playerToMoveName());
    }
}
