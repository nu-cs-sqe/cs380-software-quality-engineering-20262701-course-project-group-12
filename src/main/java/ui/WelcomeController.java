package ui;

import domain.Game;

public class WelcomeController {
    private final Game game;
    private WelcomeView view;

    public WelcomeController(Game game) {
        if (game.isStarted()) {
            throw new IllegalStateException();
        }
        this.game = game;
    }

    public void setView(WelcomeView view) {
        this.view = view;
    }

    public void startGame(String player1Name, String player2Name) {
        if (!game.start(player1Name, player2Name)) {
            view.showNamesRequired();
            return;
        }
        view.close();
        view.showGame(game);
    }
}
