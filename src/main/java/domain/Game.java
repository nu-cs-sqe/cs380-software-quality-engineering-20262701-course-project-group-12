package domain;

import domain.piece.PieceColor;

public class Game {
    private boolean started;
    private Player playerOne;
    private Player playerTwo;

    public boolean start(String player1Name, String player2Name) {
        if (player1Name.trim().isEmpty() || player2Name.trim().isEmpty()) {
            return false;
        }

        playerOne = new Player(player1Name, PieceColor.WHITE);
        playerTwo = new Player(player2Name, PieceColor.BLACK);
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

    public Player playerTwo() {
        if (!started) {
            throw new IllegalStateException();
        }
        return playerTwo;
    }
}
