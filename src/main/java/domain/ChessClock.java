package domain;

import domain.piece.PieceColor;

import java.time.Clock;
import java.time.Duration;

public class ChessClock {
    public static final Duration STARTING_TIME = Duration.ofMinutes(10);

    public ChessClock(Clock timeSource) {
    }

    public Duration remaining(PieceColor color) {
        return STARTING_TIME;
    }
}
