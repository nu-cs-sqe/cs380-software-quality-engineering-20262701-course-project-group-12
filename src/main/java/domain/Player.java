package domain;

import domain.piece.PieceColor;

public class Player {
    private final String name;
    private final PieceColor color;

    public Player(String name, PieceColor color) {
        this.name = name.trim();
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public PieceColor getColor() {
        return color;
    }
}
