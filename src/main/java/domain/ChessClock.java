package domain;

import domain.piece.PieceColor;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class ChessClock {
    public static final Duration STARTING_TIME = Duration.ofMinutes(10);

    private final Clock timeSource;
    private final Instant whiteStartedAt;

    public ChessClock(Clock timeSource) {
        this.timeSource = timeSource;
        whiteStartedAt = timeSource.instant();
    }

    public Duration remaining(PieceColor color) {
        if (color == PieceColor.WHITE) {
            Duration elapsed = Duration.between(whiteStartedAt, timeSource.instant());
            return STARTING_TIME.minus(elapsed);
        }
        return STARTING_TIME;
    }
}
