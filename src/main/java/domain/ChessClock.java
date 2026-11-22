package domain;

import domain.piece.PieceColor;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class ChessClock {
    public static final Duration STARTING_TIME = Duration.ofMinutes(10);

    private final Clock timeSource;
    private Duration whiteRemaining = STARTING_TIME;
    private Duration blackRemaining = STARTING_TIME;
    private PieceColor active = PieceColor.WHITE;
    private Instant segmentStartedAt;

    public ChessClock(Clock timeSource) {
        this.timeSource = timeSource;
        segmentStartedAt = timeSource.instant();
    }

    public Duration remaining(PieceColor color) {
        if (color == active) {
            return remainingAfter(banked(color), elapsed());
        }
        return banked(color);
    }

    public void switchTurn() {
        whiteRemaining = remaining(PieceColor.WHITE);
        active = PieceColor.BLACK;
        segmentStartedAt = timeSource.instant();
    }

    private Duration banked(PieceColor color) {
        if (color == PieceColor.WHITE) {
            return whiteRemaining;
        }
        return blackRemaining;
    }

    private Duration elapsed() {
        return Duration.between(segmentStartedAt, timeSource.instant());
    }

    private static Duration remainingAfter(Duration banked, Duration elapsed) {
        Duration left = banked.minus(elapsed);
        if (left.isNegative()) {
            return Duration.ZERO;
        }
        return left;
    }
}
