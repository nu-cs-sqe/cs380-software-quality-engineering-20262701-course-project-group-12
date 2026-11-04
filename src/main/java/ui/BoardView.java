package ui;

import domain.Location;
import domain.piece.Piece;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class BoardView extends JPanel {
    private static final int BOARD_SIZE = 8;
    private static final int FIRST_RANK = 1;
    private static final int LAST_RANK = BOARD_SIZE;
    private static final int TILE_SIZE = 80;
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);

    private final BoardController boardController;
    private final PieceImages pieceImages = new PieceImages();

    public BoardView(BoardController boardController) {
        this.boardController = boardController;
        setPreferredSize(new Dimension(BOARD_SIZE * TILE_SIZE, BOARD_SIZE * TILE_SIZE));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        smoothScaledImages(graphics);
        for (int rank = LAST_RANK; rank >= FIRST_RANK; rank--) {
            for (int file = 0; file < BOARD_SIZE; file++) {
                paintSquare(graphics, new Location(file, rank));
            }
        }
    }

    private void smoothScaledImages(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    private void paintSquare(Graphics graphics, Location location) {
        boolean isLightSquare = (location.getFile() + location.getRank()) % 2 == 0;
        graphics.setColor(isLightSquare ? LIGHT_SQUARE : DARK_SQUARE);
        graphics.fillRect(xOf(location), yOf(location), TILE_SIZE, TILE_SIZE);
        drawPiece(graphics, location);
    }

    private void drawPiece(Graphics graphics, Location location) {
        Piece piece = boardController.pieceAt(location);
        if (piece == null) {
            return;
        }
        BufferedImage image = pieceImages.imageOf(piece);
        graphics.drawImage(image, xOf(location), yOf(location), TILE_SIZE, TILE_SIZE, this);
    }

    private int xOf(Location location) {
        return location.getFile() * TILE_SIZE;
    }

    private int yOf(Location location) {
        return (LAST_RANK - location.getRank()) * TILE_SIZE;
    }
}
