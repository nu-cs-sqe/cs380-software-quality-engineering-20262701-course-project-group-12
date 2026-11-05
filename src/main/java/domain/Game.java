package domain;

import domain.piece.PieceColor;

public class Game {
    private boolean started;
    private Player playerOne;
    private Player playerTwo;
    private Player playerToMove;
    private Board board;

    public boolean start(String player1Name, String player2Name) {
        if (player1Name.trim().isEmpty() || player2Name.trim().isEmpty()) {
            return false;
        }

        playerOne = new Player(player1Name, PieceColor.WHITE);
        playerTwo = new Player(player2Name, PieceColor.BLACK);
        playerToMove = playerOne;
        board = new Board();
        started = true;
        return true;
    }

    public boolean isStarted() {
        return started;
    }

    public Player playerOne() {
        requireStarted("player one");
        return playerOne;
    }

    public Player playerTwo() {
        requireStarted("player two");
        return playerTwo;
    }

    public Player playerToMove() {
        requireStarted("the player to move");
        return playerToMove;
    }

    public Board board() {
        requireStarted("the board");
        return board;
    }

    public String playerOneName() {
        return playerOne().getName();
    }

    public String playerTwoName() {
        return playerTwo().getName();
    }

    public String playerToMoveName() {
        return playerToMove().getName();
    }

    private void requireStarted(String request) {
        if (!started) {
            throw new IllegalStateException(
                    "Cannot read " + request + " before the game has started");
        }
    }
}
