package ui;

import domain.Game;

public class GameStatsController {
    private final Game game;

    public GameStatsController(Game game) {
        if (game == null || !game.isStarted()) {
            throw new IllegalArgumentException();
        }
        this.game = game;
    }

    public GameStatsView createView() {
        return new GameStatsView(
                "Player 1: " + game.playerOne().getName() + " (White)",
                "Player 2: " + game.playerTwo().getName() + " (Black)",
                "Current player: " + game.playerToMove().getName());
    }
}
