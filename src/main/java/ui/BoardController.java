package ui;

import domain.Board;
import domain.Location;
import domain.piece.Piece;
import domain.piece.PieceType;

import java.util.Optional;

public class BoardController {
    private final Board board;
    private Optional<Location> selectedSquare = Optional.empty();

    public BoardController(Board board) {
        this.board = board;
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
        if (board.pieceAt(from).canMove(from, to, board) && !holdsKing(to)) {
            board.move(from, to);
            clearSelection();
        } else {
            select(to);
        }
    }

    private boolean holdsKing(Location square) {
        return board.isOccupied(square) && board.pieceAt(square).getType() == PieceType.KING;
    }

    private void select(Location square) {
        if (board.isOccupied(square)) {
            selectedSquare = Optional.of(square);
        }
    }

    private void clearSelection() {
        selectedSquare = Optional.empty();
    }
}
