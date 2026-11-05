package ui;

import domain.Game;
import domain.MissingPlayerNameException;

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
        try {
            openStartedGame(player1Name, player2Name);
        } catch (MissingPlayerNameException e) {
            view.showNamesRequired();
        }
    }

    private void openStartedGame(String player1Name, String player2Name) {
        game.start(player1Name, player2Name);
        view.close();
        view.showGame(game);
    }
}
