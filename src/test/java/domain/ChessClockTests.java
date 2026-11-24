package domain;

import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChessClockTests {
    private static final Instant START = Instant.parse("2026-01-01T00:00:00Z");
    private static final Duration ONE_MILLISECOND = Duration.ofMillis(1);
    private static final Duration TWO_MILLISECONDS = Duration.ofMillis(2);

    @Test
    public void Remaining_AtConstruction_IsStartingTimeForBothPlayers() {
        ControllableClock clock = new ControllableClock(START);

        ChessClock chessClock = new ChessClock(clock);

        assertEquals(ChessClock.STARTING_TIME, chessClock.remaining(PieceColor.WHITE));
        assertEquals(ChessClock.STARTING_TIME, chessClock.remaining(PieceColor.BLACK));
    }

    @Test
    public void Remaining_AfterOneMillisecond_WhiteHasUsedOneMillisecond() {
        ControllableClock clock = new ControllableClock(START);
        ChessClock chessClock = new ChessClock(clock);

        clock.advance(ONE_MILLISECOND);

        assertEquals(ChessClock.STARTING_TIME.minus(ONE_MILLISECOND),
                chessClock.remaining(PieceColor.WHITE));
        assertEquals(ChessClock.STARTING_TIME, chessClock.remaining(PieceColor.BLACK));
    }

    @Test
    public void Remaining_OneMillisecondBeforeExpiry_WhiteHasOneMillisecond() {
        ControllableClock clock = new ControllableClock(START);
        ChessClock chessClock = new ChessClock(clock);

        clock.advance(ChessClock.STARTING_TIME.minus(ONE_MILLISECOND));

        assertEquals(ONE_MILLISECOND, chessClock.remaining(PieceColor.WHITE));
        assertEquals(ChessClock.STARTING_TIME, chessClock.remaining(PieceColor.BLACK));
    }

    @Test
    public void Remaining_AtStartingTime_WhiteHasZero() {
        ControllableClock clock = new ControllableClock(START);
        ChessClock chessClock = new ChessClock(clock);

        clock.advance(ChessClock.STARTING_TIME);

        assertEquals(Duration.ZERO, chessClock.remaining(PieceColor.WHITE));
        assertEquals(ChessClock.STARTING_TIME, chessClock.remaining(PieceColor.BLACK));
    }

    @Test
    public void Remaining_PastStartingTime_WhiteStaysAtZero() {
        ControllableClock clock = new ControllableClock(START);
        ChessClock chessClock = new ChessClock(clock);

        clock.advance(ChessClock.STARTING_TIME.plus(ONE_MILLISECOND));

        assertEquals(Duration.ZERO, chessClock.remaining(PieceColor.WHITE));
    }

    @Test
    public void SwitchTurn_AfterWhiteUsedOneMillisecond_StartsBlack() {
        ControllableClock clock = new ControllableClock(START);
        ChessClock chessClock = new ChessClock(clock);
        clock.advance(ONE_MILLISECOND);

        chessClock.switchTurn();
        clock.advance(ONE_MILLISECOND);

        assertEquals(ChessClock.STARTING_TIME.minus(ONE_MILLISECOND),
                chessClock.remaining(PieceColor.WHITE));
        assertEquals(ChessClock.STARTING_TIME.minus(ONE_MILLISECOND),
                chessClock.remaining(PieceColor.BLACK));
    }

    @Test
    public void SwitchTurn_SecondSwitch_ReturnsTheTurnToWhite() {
        ControllableClock clock = new ControllableClock(START);
        ChessClock chessClock = new ChessClock(clock);
        clock.advance(ONE_MILLISECOND);
        chessClock.switchTurn();
        clock.advance(ONE_MILLISECOND);

        chessClock.switchTurn();
        clock.advance(ONE_MILLISECOND);

        assertEquals(ChessClock.STARTING_TIME.minus(ONE_MILLISECOND),
                chessClock.remaining(PieceColor.BLACK));
        assertEquals(ChessClock.STARTING_TIME.minus(TWO_MILLISECONDS),
                chessClock.remaining(PieceColor.WHITE));
    }

    @Test
    public void Stop_AfterWhiteUsedOneMillisecond_FreezesBothRemainingTimes() {
        ControllableClock clock = new ControllableClock(START);
        ChessClock chessClock = new ChessClock(clock);
        clock.advance(ONE_MILLISECOND);

        chessClock.stop();
        clock.advance(ChessClock.STARTING_TIME);

        assertEquals(ChessClock.STARTING_TIME.minus(ONE_MILLISECOND),
                chessClock.remaining(PieceColor.WHITE));
        assertEquals(ChessClock.STARTING_TIME, chessClock.remaining(PieceColor.BLACK));
    }

    private static final class ControllableClock extends Clock {
        private Instant instant;

        private ControllableClock(Instant start) {
            instant = start;
        }

        private void advance(Duration amount) {
            instant = instant.plus(amount);
        }

        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return Clock.fixed(instant, zone);
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
