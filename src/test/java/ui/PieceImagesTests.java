package ui;

import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PieceImagesTests {
    private static final String STANDARD_FOLDER = "/pieces/";

    @Test
    public void ImageOf_WhitePawn_ReturnsTheWhitePawnImage() throws IOException {
        assertImageOf(PieceType.PAWN, PieceColor.WHITE, "white-pawn.png");
    }

    @Test
    public void ImageOf_BlackPawn_ReturnsTheBlackPawnImage() throws IOException {
        assertImageOf(PieceType.PAWN, PieceColor.BLACK, "black-pawn.png");
    }

    @Test
    public void ImageOf_WhiteRook_ReturnsTheWhiteRookImage() throws IOException {
        assertImageOf(PieceType.ROOK, PieceColor.WHITE, "white-rook.png");
    }

    @Test
    public void ImageOf_BlackRook_ReturnsTheBlackRookImage() throws IOException {
        assertImageOf(PieceType.ROOK, PieceColor.BLACK, "black-rook.png");
    }

    @Test
    public void ImageOf_WhiteKnight_ReturnsTheWhiteKnightImage() throws IOException {
        assertImageOf(PieceType.KNIGHT, PieceColor.WHITE, "white-knight.png");
    }

    @Test
    public void ImageOf_BlackKnight_ReturnsTheBlackKnightImage() throws IOException {
        assertImageOf(PieceType.KNIGHT, PieceColor.BLACK, "black-knight.png");
    }

    @Test
    public void ImageOf_WhiteBishop_ReturnsTheWhiteBishopImage() throws IOException {
        assertImageOf(PieceType.BISHOP, PieceColor.WHITE, "white-bishop.png");
    }

    @Test
    public void ImageOf_BlackBishop_ReturnsTheBlackBishopImage() throws IOException {
        assertImageOf(PieceType.BISHOP, PieceColor.BLACK, "black-bishop.png");
    }

    @Test
    public void ImageOf_WhiteQueen_ReturnsTheWhiteQueenImage() throws IOException {
        assertImageOf(PieceType.QUEEN, PieceColor.WHITE, "white-queen.png");
    }

    @Test
    public void ImageOf_BlackQueen_ReturnsTheBlackQueenImage() throws IOException {
        assertImageOf(PieceType.QUEEN, PieceColor.BLACK, "black-queen.png");
    }

    @Test
    public void ImageOf_WhiteKing_ReturnsTheWhiteKingImage() throws IOException {
        assertImageOf(PieceType.KING, PieceColor.WHITE, "white-king.png");
    }

    @Test
    public void ImageOf_BlackKing_ReturnsTheBlackKingImage() throws IOException {
        assertImageOf(PieceType.KING, PieceColor.BLACK, "black-king.png");
    }

    private static void assertImageOf(PieceType type, PieceColor color, String fileName)
            throws IOException {
        BufferedImage expected = readStandardImage(fileName);

        BufferedImage actual = new PieceImages().imageOf(new Piece(type, color));

        assertSamePixels(expected, actual);
    }

    private static BufferedImage readStandardImage(String fileName) throws IOException {
        return ImageIO.read(PieceImagesTests.class.getResource(STANDARD_FOLDER + fileName));
    }

    private static void assertSamePixels(BufferedImage expected, BufferedImage actual) {
        assertEquals(expected.getWidth(), actual.getWidth());
        assertEquals(expected.getHeight(), actual.getHeight());
        assertArrayEquals(pixelsOf(expected), pixelsOf(actual));
    }

    private static int[] pixelsOf(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        return image.getRGB(0, 0, width, height, new int[width * height], 0, width);
    }
}
