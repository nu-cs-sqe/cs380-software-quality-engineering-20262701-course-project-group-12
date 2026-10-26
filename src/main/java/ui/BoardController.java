package ui;

import domain.Board;

public class BoardController {
    public BoardController(Board board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
    }
}
