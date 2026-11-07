package ui;

import domain.Board;
import domain.Location;
import domain.piece.Piece;

public class BoardController {
    private final Board board;

    public BoardController(Board board) {
        this.board = board;
    }

    public boolean isOccupied(Location location) {
        return board.isOccupied(location);
    }

    public Piece pieceAt(Location location) {
        return board.pieceAt(location);
    }
}
