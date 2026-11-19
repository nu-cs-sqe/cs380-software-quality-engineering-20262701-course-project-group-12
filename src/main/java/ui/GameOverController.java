package ui;

import domain.Game;

public class GameOverController {
    private final Game game;
    private final GameStatsController stats;
    private final GameOverView view;
    private boolean dialogOpened;

    public GameOverController(Game game, GameStatsController stats, GameOverView view) {
        this.game = game;
        this.stats = stats;
        this.view = view;
    }

    public void showIfOver() {
        if (!game.isOver() || dialogOpened) {
            return;
        }
        dialogOpened = true;
        actOn(view.askNextStep(stats.resultLine()));
    }

    private void actOn(GameOverChoice choice) {
        if (choice == GameOverChoice.NEW_GAME) {
            view.returnToWelcome(game.playerOneName(), game.playerTwoName());
        }
        if (choice == GameOverChoice.CLOSE) {
            view.exitApplication();
        }
    }
}
