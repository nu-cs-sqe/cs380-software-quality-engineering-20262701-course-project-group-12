package ui;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PieceImagesTests {
    private static final String TRUNCATED_FOLDER = "/pieces-truncated/";

    @Test
    public void PieceImages_TruncatedImageFiles_ThrowsWithTheReadFailureAsCause() {
        PieceImageException thrown =
                assertThrows(PieceImageException.class, () -> new PieceImages(TRUNCATED_FOLDER));

        assertInstanceOf(IOException.class, thrown.getCause());
    }
}
