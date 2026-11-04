package ui;

import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PieceImagesTests {
    private static final String STANDARD_FOLDER = "/pieces/";
    private static final String MISSING_FOLDER = "/no-piece-images/";

    @Test
    public void PieceImages_MissingImageFiles_ThrowsPieceImageException() {
        assertThrows(PieceImageException.class, () -> new PieceImages(MISSING_FOLDER));
    }

    @Test
    public void PieceImages_MissingImageFiles_NamesTheMissingFileInTheMessage() {
        PieceImageException thrown =
                assertThrows(PieceImageException.class, () -> new PieceImages(MISSING_FOLDER));

        assertTrue(thrown.getMessage().contains(MISSING_FOLDER));
    }

    @ParameterizedTest
    @CsvSource({
        "WHITE, KING, white-king.png",
        "WHITE, QUEEN, white-queen.png",
        "WHITE, ROOK, white-rook.png",
        "WHITE, BISHOP, white-bishop.png",
        "WHITE, KNIGHT, white-knight.png",
        "WHITE, PAWN, white-pawn.png",
        "BLACK, KING, black-king.png",
        "BLACK, QUEEN, black-queen.png",
        "BLACK, ROOK, black-rook.png",
        "BLACK, BISHOP, black-bishop.png",
        "BLACK, KNIGHT, black-knight.png",
        "BLACK, PAWN, black-pawn.png"
    })
    public void ImageOf_EachPiece_ReturnsThatPiecesImage(
            PieceColor color, PieceType type, String fileName) throws IOException {
        PieceImages images = new PieceImages();
        URL expectedFile = PieceImagesTests.class.getResource(STANDARD_FOLDER + fileName);
        BufferedImage expected = ImageIO.read(expectedFile);

        BufferedImage actual = images.imageOf(new Piece(type, color));

        assertSamePixels(expected, actual);
    }

    @Test
    public void ImageOf_NullPiece_ThrowsIllegalArgumentException() {
        PieceImages images = new PieceImages();

        assertThrows(IllegalArgumentException.class, () -> images.imageOf(null));
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
