package ui;

import domain.Game;

public class WelcomeController {
    private final Game game;

    public WelcomeController(Game game) {
        if (game.isStarted()) {
            throw new IllegalStateException();
        }
        this.game = game;
    }
}
