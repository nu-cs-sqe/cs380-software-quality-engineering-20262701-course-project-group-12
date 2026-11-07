package ui;

import domain.Board;
import domain.Location;
import domain.piece.Piece;

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

    public void clickSquare(Location square) {
        selectedSquare.ifPresentOrElse(
                from -> moveSelectedPiece(from, square),
                () -> select(square));
    }

    private void select(Location square) {
        if (board.isOccupied(square)) {
            selectedSquare = Optional.of(square);
        }
    }

    private void moveSelectedPiece(Location from, Location to) {
        if (board.pieceAt(from).canMove(from, to, board)) {
            board.move(from, to);
        }
    }
}
