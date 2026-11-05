package ui;

import domain.Location;
import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

public class BoardView extends JPanel {
    private static final int BOARD_SIZE = Location.MAX_FILE - Location.MIN_FILE + 1;
    private static final int FIRST_RANK = Location.MIN_RANK;
    private static final int LAST_RANK = Location.MAX_RANK;
    private static final int TILE_SIZE = 80;
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);

    private final BoardController boardController;

    public BoardView(BoardController boardController) {
        if (boardController == null) {
            throw new IllegalArgumentException("Cannot create the board view without a board controller");
        }
        this.boardController = boardController;
        setPreferredSize(new Dimension(BOARD_SIZE * TILE_SIZE, BOARD_SIZE * TILE_SIZE));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        for (int rank = LAST_RANK; rank >= FIRST_RANK; rank--) {
            for (int file = 0; file < BOARD_SIZE; file++) {
                paintSquare(graphics, new Location(file, rank));
            }
        }
    }

    private void paintSquare(Graphics graphics, Location location) {
        boolean isLightSquare = (location.getFile() + location.getRank()) % 2 == 0;
        graphics.setColor(isLightSquare ? LIGHT_SQUARE : DARK_SQUARE);
        graphics.fillRect(xOf(location), yOf(location), TILE_SIZE, TILE_SIZE);
        if (boardController.isOccupied(location)) {
            drawPiece(graphics, location);
        }
    }

    private void drawPiece(Graphics graphics, Location location) {
        Piece piece = boardController.pieceAt(location);
        String letter = symbol(piece.getType());
        int textX = xOf(location) + TILE_SIZE / 4;
        int textY = yOf(location) + TILE_SIZE * 2 / 3;
        graphics.setFont(new Font("SansSerif", Font.BOLD, TILE_SIZE / 2));
        graphics.setColor(piece.getColor() == PieceColor.WHITE ? Color.BLACK : Color.WHITE);
        graphics.drawString(letter, textX + 1, textY + 1);
        graphics.setColor(piece.getColor() == PieceColor.WHITE ? Color.WHITE : Color.BLACK);
        graphics.drawString(letter, textX, textY);
    }

    private int xOf(Location location) {
        return location.getFile() * TILE_SIZE;
    }

    private int yOf(Location location) {
        return (LAST_RANK - location.getRank()) * TILE_SIZE;
    }

    private String symbol(PieceType type) {
        switch (type) {
            case KING:
                return "K";
            case QUEEN:
                return "Q";
            case ROOK:
                return "R";
            case BISHOP:
                return "B";
            case KNIGHT:
                return "N";
            default:
                return "P";
        }
    }
}
