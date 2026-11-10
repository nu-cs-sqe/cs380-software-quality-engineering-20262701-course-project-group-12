package ui;

import domain.Location;
import domain.piece.Piece;
import domain.piece.PieceType;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BoardView extends JPanel {
    private static final int BOARD_SIZE = Location.MAX_FILE - Location.MIN_FILE + 1;
    private static final int FIRST_FILE = Location.MIN_FILE;
    private static final int LAST_FILE = Location.MAX_FILE;
    private static final int FIRST_RANK = Location.MIN_RANK;
    private static final int LAST_RANK = Location.MAX_RANK;
    private static final int TILE_SIZE = 80;
    private static final int BOARD_LENGTH = BOARD_SIZE * TILE_SIZE;
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);
    private static final Color SELECTED_SQUARE = new Color(186, 202, 68);
    private static final int LABEL_MARGIN = TILE_SIZE / 2;
    private static final char FIRST_FILE_LETTER = 'a';
    private static final Color LABEL_INK = Color.DARK_GRAY;
    private static final Font LABEL_FONT = new Font(Font.SANS_SERIF, Font.BOLD, TILE_SIZE / 5);
    private static final List<PieceType> PROMOTION_CHOICES =
            List.of(PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT);

    private final BoardController boardController;
    private final Runnable afterClick;
    private final PieceImages pieceImages = new PieceImages();

    public BoardView(BoardController boardController) {
        this(boardController, () -> {
        });
    }

    public BoardView(BoardController boardController, Runnable afterClick) {
        this.boardController = boardController;
        this.afterClick = afterClick;
        int sideLength = BOARD_LENGTH + LABEL_MARGIN;
        setPreferredSize(new Dimension(sideLength, sideLength));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                clickAt(event.getPoint());
            }
        });
    }

    private void clickAt(Point point) {
        if (isOnBoard(point)) {
            boardController.clickSquare(squareAt(point));
            repaint();
            askForPromotionWhilePending();
            afterClick.run();
            repaint();
        }
    }

    private void askForPromotionWhilePending() {
        while (boardController.isPromotionPending()) {
            askForPromotion();
        }
    }

    private void askForPromotion() {
        Object[] labels = PROMOTION_CHOICES.stream().map(BoardView::labelOf).toArray();
        int choice = JOptionPane.showOptionDialog(this, Messages.text("promotion.question"),
                Messages.text("promotion.title"), JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, labels, labels[0]);
        if (choice != JOptionPane.CLOSED_OPTION) {
            boardController.choosePromotion(PROMOTION_CHOICES.get(choice));
        }
    }

    private static String labelOf(PieceType type) {
        return Messages.text("promotion." + type.name().toLowerCase(Locale.ROOT));
    }

    private boolean isOnBoard(Point point) {
        boolean isRightOfRankLabels =
                point.x >= LABEL_MARGIN && point.x < LABEL_MARGIN + BOARD_LENGTH;
        boolean isAboveFileLabels = point.y >= 0 && point.y < BOARD_LENGTH;
        return isRightOfRankLabels && isAboveFileLabels;
    }

    private Location squareAt(Point point) {
        int file = FIRST_FILE + (point.x - LABEL_MARGIN) / TILE_SIZE;
        int rank = LAST_RANK - point.y / TILE_SIZE;
        return new Location(file, rank);
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
            for (int file = FIRST_FILE; file <= LAST_FILE; file++) {
                paintSquare(graphics, new Location(file, rank));
            }
        }
    }

    private void paintSquare(Graphics graphics, Location location) {
        graphics.setColor(colorOf(location));
        graphics.fillRect(leftEdgeOf(location.getFile()), topEdgeOf(location.getRank()),
                TILE_SIZE, TILE_SIZE);
        if (boardController.isOccupied(location)) {
            drawPiece(graphics, location);
        }
    }

    private Color colorOf(Location square) {
        if (boardController.isSelected(square)) {
            return SELECTED_SQUARE;
        }
        boolean isLightSquare = (square.getFile() + square.getRank()) % 2 == 0;
        return isLightSquare ? LIGHT_SQUARE : DARK_SQUARE;
    }

    private void drawPiece(Graphics graphics, Location location) {
        Piece piece = boardController.pieceAt(location);
        BufferedImage image = pieceImages.imageOf(piece);
        int left = leftEdgeOf(location.getFile());
        int top = topEdgeOf(location.getRank());
        graphics.drawImage(image, left, top, TILE_SIZE, TILE_SIZE, this);
    }

    private void paintLabels(Graphics graphics) {
        graphics.setFont(LABEL_FONT);
        graphics.setColor(LABEL_INK);
        paintFileLabels(graphics);
        paintRankLabels(graphics);
    }

    private void paintFileLabels(Graphics graphics) {
        for (int file = FIRST_FILE; file <= LAST_FILE; file++) {
            String fileLetter = String.valueOf((char) (FIRST_FILE_LETTER + file - FIRST_FILE));
            Rectangle belowFile =
                    new Rectangle(leftEdgeOf(file), BOARD_LENGTH, TILE_SIZE, LABEL_MARGIN);
            drawCentered(graphics, fileLetter, belowFile);
        }
    }

    private void paintRankLabels(Graphics graphics) {
        for (int rank = FIRST_RANK; rank <= LAST_RANK; rank++) {
            Rectangle besideRank = new Rectangle(0, topEdgeOf(rank), LABEL_MARGIN, TILE_SIZE);
            drawCentered(graphics, String.valueOf(rank), besideRank);
        }
    }

    private void drawCentered(Graphics graphics, String text, Rectangle area) {
        FontMetrics metrics = graphics.getFontMetrics();
        int textX = area.x + (area.width - metrics.stringWidth(text)) / 2;
        int textY = area.y + (area.height - metrics.getHeight()) / 2 + metrics.getAscent();
        graphics.drawString(text, textX, textY);
    }

    private int leftEdgeOf(int file) {
        return LABEL_MARGIN + (file - FIRST_FILE) * TILE_SIZE;
    }

    private int topEdgeOf(int rank) {
        return (LAST_RANK - rank) * TILE_SIZE;
    }
}
