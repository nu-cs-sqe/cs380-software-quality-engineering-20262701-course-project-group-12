package ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PieceImagesTests {
    private static final String MISSING_FOLDER = "/no-piece-images/";

    @Test
    public void PieceImages_MissingImageFiles_ThrowsPieceImageException() {
        assertThrows(PieceImageException.class, () -> new PieceImages(MISSING_FOLDER));
    }
}
