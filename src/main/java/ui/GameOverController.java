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
        if (game.isOver() && !dialogOpened) {
            dialogOpened = true;
            GameOverChoice choice = view.askNextStep(stats.resultLine());
            if (choice == GameOverChoice.NEW_GAME) {
                view.returnToWelcome(game.playerOneName(), game.playerTwoName());
            }
        }
    }
}
