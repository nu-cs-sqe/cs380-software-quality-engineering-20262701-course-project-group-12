package domain;

import domain.piece.PieceColor;

import java.util.Optional;

public class Game {
    private boolean started;
    private Player playerOne;
    private Player playerTwo;
    private Player playerToMove;
    private Board board;
    private Optional<Player> winner = Optional.empty();

    public void start(String player1Name, String player2Name) {
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

    public void passTurn() {
        if (!started) {
            throw new IllegalStateException(
                    "Cannot pass the turn before the game has started");
        }
        if (playerToMove == playerOne) {
            playerToMove = playerTwo;
        } else {
            playerToMove = playerOne;
        }
    }

    public boolean isStarted() {
        return started;
    }

    public void endByCheckmate() {
        winner = Optional.of(playerWaiting());
    }

    private Player playerWaiting() {
        if (playerToMove == playerOne) {
            return playerTwo;
        }
        return playerOne;
    }

    public boolean isOver() {
        return winner.isPresent();
    }

    public String winnerName() {
        return winner.orElseThrow().getName();
    }

    public PieceColor winnerColor() {
        return winner.orElseThrow().getColor();
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

    public PieceColor colorToMove() {
        return playerToMove().getColor();
    }

    private void requireStarted(String request) {
        if (!started) {
            throw new IllegalStateException(
                    "Cannot read " + request + " before the game has started");
        }
    }
}
