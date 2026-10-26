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
    private static final int BOARD_SIZE = 8;
    private static final int TILE_SIZE = 80;
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);

    private final BoardController boardController;

    public BoardView(BoardController boardController) {
        this.boardController = boardController;
        setPreferredSize(new Dimension(BOARD_SIZE * TILE_SIZE, BOARD_SIZE * TILE_SIZE));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        for (int rank = 8; rank >= 1; rank--) {
            for (int file = 0; file < BOARD_SIZE; file++) {
                int x = file * TILE_SIZE;
                int y = (8 - rank) * TILE_SIZE;
                graphics.setColor((file + rank) % 2 == 0 ? LIGHT_SQUARE : DARK_SQUARE);
                graphics.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                drawPiece(graphics, boardController.pieceAt(new Location(file, rank)), x, y);
            }
        }
    }

    private void drawPiece(Graphics graphics, Piece piece, int x, int y) {
        if (piece == null) {
            return;
        }
        String letter = symbol(piece.getType());
        int textX = x + TILE_SIZE / 4;
        int textY = y + TILE_SIZE * 2 / 3;
        graphics.setFont(new Font("SansSerif", Font.BOLD, TILE_SIZE / 2));
        graphics.setColor(piece.getColor() == PieceColor.WHITE ? Color.BLACK : Color.WHITE);
        graphics.drawString(letter, textX + 1, textY + 1);
        graphics.setColor(piece.getColor() == PieceColor.WHITE ? Color.WHITE : Color.BLACK);
        graphics.drawString(letter, textX, textY);
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
