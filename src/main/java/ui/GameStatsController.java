package ui;

import domain.Game;

public class GameStatsController {
    public GameStatsController(Game game) {
        if (game == null || !game.isStarted()) {
            throw new IllegalArgumentException();
        }
    }
}
