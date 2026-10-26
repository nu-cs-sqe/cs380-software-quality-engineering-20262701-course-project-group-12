package ui;

import domain.Board;
import domain.Location;
import domain.piece.Piece;

public class BoardController {
    private final Board board;

    public BoardController(Board board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
        this.board = board;
    }

    public Piece pieceAt(Location location) {
        return board.pieceAt(location);
    }
}
