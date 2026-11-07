package domain;

import domain.piece.PieceColor;

public class Game {
    private boolean started;
    private Player playerOne;
    private Player playerTwo;
    private Player playerToMove;
    private Board board;

    public void start(String player1Name, String player2Name) {
        if (player1Name == null || player2Name == null) {
            throw new IllegalArgumentException(
                    "Failed to start the game because a player name is null.");
        }
        if (player1Name.trim().isEmpty() || player2Name.trim().isEmpty()) {
            throw new MissingPlayerNameException();
        }
        if (player1Name.trim().equalsIgnoreCase(player2Name.trim())) {
            throw new DuplicatePlayerNameException();
        }

        playerOne = new Player(player1Name, PieceColor.WHITE);
        playerTwo = new Player(player2Name, PieceColor.BLACK);
        playerToMove = playerOne;
        board = new Board();
        started = true;
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
