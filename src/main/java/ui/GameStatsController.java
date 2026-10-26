package ui;

import domain.Game;

public class GameStatsController {
    private final Game game;

    public GameStatsController(Game game) {
        if (!game.isStarted()) {
            throw new IllegalStateException();
        }
        this.game = game;
    }

    public GameStatsView createView() {
        return new GameStatsView(
                "Player 1: " + game.playerOne().getName() + " (White)\n"
                        + "Player 2: " + game.playerTwo().getName() + " (Black)\n"
                        + "Current player: " + game.playerToMove().getName());
    }
}
