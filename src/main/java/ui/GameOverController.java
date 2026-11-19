package ui;

import domain.Game;

public class GameOverController {
    private final Game game;
    private final GameStatsController stats;
    private final GameOverView view;

    public GameOverController(Game game, GameStatsController stats, GameOverView view) {
        this.game = game;
        this.stats = stats;
        this.view = view;
    }

    public void showIfOver() {
    }
}
