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
        promotionSquare = Optional.empty();
    }

    public void clickSquare(Location square) {
        if (game.isOver() || isPromotionPending()) {
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
        Piece movingPiece = board.pieceAt(from);
        if (board.isLegalMove(from, to)) {
            moveUnlessCapturingKing(movingPiece, from, to);
        } else {
            select(to);
        }
    }

    private void moveUnlessCapturingKing(Piece movingPiece, Location from, Location to) {
        if (!holdsKing(to)) {
            board.move(from, to);
            passTurnUnlessPromoting(movingPiece, to);
            clearSelection();
        }
    }

    private void passTurnUnlessPromoting(Piece movingPiece, Location arrival) {
        if (movingPiece.promotesOn(arrival)) {
            promotionSquare = Optional.of(arrival);
        } else {
            game.passTurn();
            endGameIfCheckmated();
        }
    }

    private void endGameIfCheckmated() {
        if (board.isCheckmated(game.colorToMove())) {
            game.endByCheckmate();
        }
    }

    private boolean holdsKing(Location square) {
        return board.isOccupied(square) && board.pieceAt(square).getType() == PieceType.KING;
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
}
