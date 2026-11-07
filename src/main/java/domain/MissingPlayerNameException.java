package domain;

public class MissingPlayerNameException extends RuntimeException {
    public MissingPlayerNameException() {
        super("Failed to start the game because a player name is missing.");
    }
}
