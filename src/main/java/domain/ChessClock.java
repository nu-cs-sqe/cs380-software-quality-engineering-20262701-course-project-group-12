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
            return remainingAfter(Duration.between(whiteStartedAt, timeSource.instant()));
        }
        return STARTING_TIME;
    }

    private static Duration remainingAfter(Duration elapsed) {
        Duration left = STARTING_TIME.minus(elapsed);
        if (left.isNegative()) {
            return Duration.ZERO;
        }
        return left;
    }
}
