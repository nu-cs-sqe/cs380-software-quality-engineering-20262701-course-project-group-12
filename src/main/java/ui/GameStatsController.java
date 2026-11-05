package ui;

import domain.Game;

public class GameStatsController {
    private final Game game;

    public GameStatsController(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Cannot show game stats without a game");
        }
        if (!game.isStarted()) {
            throw new IllegalArgumentException(
                    "Cannot show game stats before the game has started");
        }
        this.game = game;
    }

    public GameStatsView createView() {
        return new GameStatsView(
                Messages.format("stats.playerOne", game.playerOneName()),
                Messages.format("stats.playerTwo", game.playerTwoName()),
                Messages.format("stats.currentPlayer", game.playerToMoveName()));
    }
}
