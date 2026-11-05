package ui;

import domain.Game;

public class GameStatsController {
    private final Game game;

    public GameStatsController(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Cannot show game stats without a game");
        }
        if (!game.isStarted()) {
            throw new IllegalArgumentException("Cannot show game stats before the game has started");
        }
        this.game = game;
    }

    public GameStatsView createView() {
        return new GameStatsView(
                "Player 1: " + game.playerOneName() + " (White)",
                "Player 2: " + game.playerTwoName() + " (Black)",
                "Current player: " + game.playerToMoveName());
    }
}
