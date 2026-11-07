package domain;

public class DuplicatePlayerNameException extends RuntimeException {
    public DuplicatePlayerNameException() {
        super("Failed to start the game because both players have the same name.");
    }
}
