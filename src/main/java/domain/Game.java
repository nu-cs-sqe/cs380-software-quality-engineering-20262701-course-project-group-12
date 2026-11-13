package domain;

import domain.piece.PieceColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game {
    private static final int THREEFOLD = 3;
    private static final int FIVEFOLD = 5;
    private static final int ONE_MORE_OCCURRENCE = 1;
    private static final int MOVES_BY_BOTH_PLAYERS = 2;

    private final List<Position> positions = new ArrayList<>();
    private boolean started;
    private Player playerOne;
    private Player playerTwo;
    private Player playerToMove;
    private Board board;
    private Optional<Player> winner = Optional.empty();
    private boolean drawnByRepetition;
    private boolean wonByResignation;
    private int movesMade;
    private Optional<PieceColor> drawOfferedBy = Optional.empty();
    private boolean drawnByAgreement;

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
        recordPosition();
    }

    public void passTurn() {
        requireStartedTo("pass the turn");
        keepDrawOfferOnlyIfMadeBy(playerToMove.getColor());
        playerToMove = playerWaiting();
        movesMade++;
        recordPosition();
    }

    private void keepDrawOfferOnlyIfMadeBy(PieceColor movingColor) {
        drawOfferedBy = drawOfferedBy.filter(movingColor::equals);
    }

    private void recordPosition() {
        positions.add(board.position(playerToMove.getColor()));
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

    public boolean canClaimDrawByRepetition() {
        return occurrencesOf(currentPosition()) >= THREEFOLD;
    }

    public boolean canClaimDrawByFiftyMove() {
        return false;
    }

    public boolean canClaimDrawByRepetitionWith(Location from, Location to) {
        Position positionAfterMove =
                board.positionAfterMove(from, to, playerWaiting().getColor());
        return occurrencesOf(positionAfterMove) + ONE_MORE_OCCURRENCE >= THREEFOLD;
    }

    public void endByRepetition() {
        requireStartedTo("end the game");
        drawnByRepetition = true;
    }

    public boolean isDrawnByRepetition() {
        return drawnByRepetition;
    }

    public boolean isFivefoldRepetition() {
        return occurrencesOf(currentPosition()) >= FIVEFOLD;
    }

    private Position currentPosition() {
        return positions.get(positions.size() - 1);
    }

    private long occurrencesOf(Position position) {
        return positions.stream().filter(position::isSameAs).count();
    }

    public void resign(PieceColor resigningColor) {
        requireStartedTo("resign");
        if (isOver()) {
            return;
        }
        winner = Optional.of(playerOtherThan(resigningColor));
        wonByResignation = true;
    }

    private Player playerOtherThan(PieceColor color) {
        if (playerOne.getColor() == color) {
            return playerTwo;
        }
        return playerOne;
    }

    public boolean isWonByResignation() {
        return wonByResignation;
    }

    public boolean canOfferDraw() {
        return movesMade >= MOVES_BY_BOTH_PLAYERS && !isOver() && !isDrawOffered();
    }

    public void offerDraw(PieceColor offeringColor) {
        requireStartedTo("offer a draw");
        if (canOfferDraw()) {
            drawOfferedBy = Optional.of(offeringColor);
        }
    }

    public boolean isDrawOffered() {
        return drawOfferedBy.isPresent() && !isOver();
    }

    public void acceptDrawOffer() {
        if (isDrawOffered()) {
            drawnByAgreement = true;
        }
    }

    public void declineDrawOffer() {
        drawOfferedBy = Optional.empty();
    }

    public boolean isDrawnByAgreement() {
        return drawnByAgreement;
    }

    public boolean isOver() {
        return winner.isPresent() || drawnByRepetition || drawnByAgreement;
    }

    public boolean isPlayerToMoveInCheck() {
        return board().isInCheck(colorToMove());
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
