package ui;

import domain.Location;
import domain.piece.Piece;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class BoardView extends JPanel {
    private static final int BOARD_SIZE = 8;
    private static final int FIRST_RANK = 1;
    private static final int LAST_RANK = BOARD_SIZE;
    private static final int TILE_SIZE = 80;
    private static final int BOARD_LENGTH = BOARD_SIZE * TILE_SIZE;
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);
    private static final int LABEL_MARGIN = TILE_SIZE / 2;
    private static final char FIRST_FILE_LETTER = 'a';
    private static final Color LABEL_INK = Color.DARK_GRAY;
    private static final Font LABEL_FONT = new Font(Font.SANS_SERIF, Font.BOLD, TILE_SIZE / 5);

    private final BoardController boardController;
    private final PieceImages pieceImages = new PieceImages();

    public BoardView(BoardController boardController) {
        if (boardController == null) {
            throw new IllegalArgumentException("Cannot create the board view without a board controller");
        }
        this.boardController = boardController;
        int sideLength = BOARD_LENGTH + LABEL_MARGIN;
        setPreferredSize(new Dimension(sideLength, sideLength));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        smoothScaledImages(graphics);
        paintSquares(graphics);
        paintLabels(graphics);
    }

    private void smoothScaledImages(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    private void paintSquares(Graphics graphics) {
        for (int rank = LAST_RANK; rank >= FIRST_RANK; rank--) {
            for (int file = 0; file < BOARD_SIZE; file++) {
                paintSquare(graphics, new Location(file, rank));
            }
        }
    }

    private void paintSquare(Graphics graphics, Location location) {
        boolean isLightSquare = (location.getFile() + location.getRank()) % 2 == 0;
        graphics.setColor(isLightSquare ? LIGHT_SQUARE : DARK_SQUARE);
        graphics.fillRect(xOf(location.getFile()), yOf(location.getRank()), TILE_SIZE, TILE_SIZE);
        if (boardController.isOccupied(location)) {
            drawPiece(graphics, location);
        }
    }

    private void drawPiece(Graphics graphics, Location location) {
        Piece piece = boardController.pieceAt(location);
        BufferedImage image = pieceImages.imageOf(piece);
        int x = xOf(location.getFile());
        int y = yOf(location.getRank());
        graphics.drawImage(image, x, y, TILE_SIZE, TILE_SIZE, this);
    }

    private void paintLabels(Graphics graphics) {
        graphics.setFont(LABEL_FONT);
        graphics.setColor(LABEL_INK);
        paintFileLabels(graphics);
        paintRankLabels(graphics);
    }

    private void paintFileLabels(Graphics graphics) {
        for (int file = 0; file < BOARD_SIZE; file++) {
            String fileLetter = String.valueOf((char) (FIRST_FILE_LETTER + file));
            Rectangle belowFile = new Rectangle(xOf(file), BOARD_LENGTH, TILE_SIZE, LABEL_MARGIN);
            drawCentered(graphics, fileLetter, belowFile);
        }
    }

    private void paintRankLabels(Graphics graphics) {
        for (int rank = FIRST_RANK; rank <= LAST_RANK; rank++) {
            Rectangle besideRank = new Rectangle(0, yOf(rank), LABEL_MARGIN, TILE_SIZE);
            drawCentered(graphics, String.valueOf(rank), besideRank);
        }
    }

    private void drawCentered(Graphics graphics, String text, Rectangle area) {
        FontMetrics metrics = graphics.getFontMetrics();
        int textX = area.x + (area.width - metrics.stringWidth(text)) / 2;
        int textY = area.y + (area.height - metrics.getHeight()) / 2 + metrics.getAscent();
        graphics.drawString(text, textX, textY);
    }

    private int xOf(int file) {
        return LABEL_MARGIN + file * TILE_SIZE;
    }

    private int yOf(int rank) {
        return (LAST_RANK - rank) * TILE_SIZE;
    }
}
