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
    private boolean promotionPending;

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
        return promotionPending;
    }

    public void clickSquare(Location square) {
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
        if (movingPiece.canMove(from, to, board)) {
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
            promotionPending = true;
        } else {
            game.passTurn();
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
