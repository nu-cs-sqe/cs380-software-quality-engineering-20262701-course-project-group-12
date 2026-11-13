package ui;

import domain.Board;
import domain.Game;
import domain.Location;
import domain.piece.Piece;
import domain.piece.PieceType;

import java.util.Optional;

public class BoardController {
    private final Board board;
    private final Game game;
    private Optional<Location> selectedSquare = Optional.empty();
    private Optional<Location> promotionSquare = Optional.empty();
    private Optional<WaitingMove> moveWaitingForClaim = Optional.empty();

    public BoardController(Board board, Game game) {
        this.board = board;
        this.game = game;
    }

    public boolean isOccupied(Location location) {
        return board.isOccupied(location);
    }

    public Piece pieceAt(Location location) {
        return board.pieceAt(location);
    }

    public boolean isSelected(Location square) {
        return selectedSquare.filter(square::isSameSquareAs).isPresent();
    }

    public boolean isPromotionPending() {
        return promotionSquare.isPresent();
    }

    public void choosePromotion(PieceType type) {
        promotionSquare.ifPresent(square -> finishPromotion(square, type));
    }

    private void finishPromotion(Location square, PieceType type) {
        board.promote(square, type);
        game.passTurn();
        endGameIfOver();
        promotionSquare = Optional.empty();
    }

    public boolean canClaimDraw() {
        return game.canClaimDrawByRepetition() || isDrawClaimWithMovePending();
    }

    public boolean isDrawClaimWithMovePending() {
        return moveWaitingForClaim.isPresent();
    }

    public void declineDrawClaim() {
        moveWaitingForClaim.ifPresent(this::makeWaitingMove);
    }

    private void makeWaitingMove(WaitingMove move) {
        moveWaitingForClaim = Optional.empty();
        makeMove(move.piece, move.from, move.to);
    }

    public void claimDraw() {
        if (canClaimDraw()) {
            game.endByRepetition();
        }
    }

    public void clickSquare(Location square) {
        if (game.isOver() || isPromotionPending() || isDrawClaimWithMovePending()) {
            return;
        }
        selectedSquare.ifPresentOrElse(
                selected -> handleClickWithSelection(selected, square),
                () -> select(square));
    }

    private void handleClickWithSelection(Location selected, Location square) {
        if (square.isSameSquareAs(selected)) {
            clearSelection();
        } else {
            moveOrChangeSelection(selected, square);
        }
    }

    private void moveOrChangeSelection(Location from, Location to) {
        if (board.isLegalMove(from, to)) {
            move(from, to);
        } else {
            select(to);
        }
    }

    private void move(Location from, Location to) {
        Piece movingPiece = board.pieceAt(from);
        moveUnlessItCanBeClaimed(movingPiece, from, to);
        clearSelection();
    }

    private void moveUnlessItCanBeClaimed(Piece movingPiece, Location from, Location to) {
        if (game.canClaimDrawByRepetitionWith(from, to)) {
            moveWaitingForClaim = Optional.of(new WaitingMove(movingPiece, from, to));
        } else {
            makeMove(movingPiece, from, to);
        }
    }

    private void makeMove(Piece movingPiece, Location from, Location to) {
        board.move(from, to);
        passTurnUnlessPromoting(movingPiece, to);
    }

    private void passTurnUnlessPromoting(Piece movingPiece, Location arrival) {
        if (movingPiece.promotesOn(arrival)) {
            promotionSquare = Optional.of(arrival);
        } else {
            game.passTurn();
            endGameIfOver();
        }
    }

    private void endGameIfOver() {
        if (board.isCheckmated(game.colorToMove())) {
            game.endByCheckmate();
        } else if (board.isDeadPosition()) {
            game.endByDeadPosition();
        } else if (game.isFivefoldRepetition()) {
            game.endByRepetition();
        }
    }

    private void select(Location square) {
        if (holdsPieceToMove(square)) {
            selectedSquare = Optional.of(square);
        }
    }

    private boolean holdsPieceToMove(Location square) {
        if (!board.isOccupied(square)) {
            return false;
        }
        Piece occupant = board.pieceAt(square);
        return occupant.getColor() == game.colorToMove();
    }

    private void clearSelection() {
        selectedSquare = Optional.empty();
    }

    private static final class WaitingMove {
        private final Piece piece;
        private final Location from;
        private final Location to;

        private WaitingMove(Piece piece, Location from, Location to) {
            this.piece = piece;
            this.from = from;
            this.to = to;
        }
    }
}
