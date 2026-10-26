package domain;

import domain.piece.PieceColor;

public class Game {
    private boolean started;
    private Player playerOne;

    public boolean start(String player1Name, String player2Name) {
        if (player1Name.trim().isEmpty() || player2Name.trim().isEmpty()) {
            return false;
        }

        playerOne = new Player(player1Name, PieceColor.WHITE);
        started = true;
        return true;
    }

    public boolean isStarted() {
        return started;
    }

    public Player playerOne() {
        if (!started) {
            throw new IllegalStateException();
        }
        return playerOne;
    }
}
