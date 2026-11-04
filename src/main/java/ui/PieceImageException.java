package ui;

public class PieceImageException extends RuntimeException {
    public PieceImageException(String message) {
        super(message);
    }

    public PieceImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
