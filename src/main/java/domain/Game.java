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
        requireStartedTo("pass the turn");
        playerToMove = playerWaiting();
    }

    public boolean isStarted() {
        return started;
    }

    public void endByCheckmate() {
        requireStartedTo("end the game");
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

    public boolean isStalemate() {
        return false;
    }

    public String winnerName() {
        return winner().getName();
    }

    public PieceColor winnerColor() {
        return winner().getColor();
    }

    private Player winner() {
        return winner.orElseThrow(() -> new IllegalStateException(
                "Cannot read the winner before the game is over"));
    }

    public Player playerOne() {
        requireStartedTo("read player one");
        return playerOne;
    }

    public Player playerTwo() {
        requireStartedTo("read player two");
        return playerTwo;
    }

    public Player playerToMove() {
        requireStartedTo("read the player to move");
        return playerToMove;
    }

    public Board board() {
        requireStartedTo("read the board");
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

    private void requireStartedTo(String action) {
        if (!started) {
            throw new IllegalStateException(
                    "Cannot " + action + " before the game has started");
        }
    }
}
