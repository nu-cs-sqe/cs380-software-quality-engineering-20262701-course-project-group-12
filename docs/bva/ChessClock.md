# BVA Analysis for ChessClock

Issue #72 (GitHub US-23, docs US-23). `ChessClock` is the two players'
chess clock. It reads the current instant from an injected
`java.time.Clock`, so tests can advance time without waiting ten minutes.
There is no increment or delay: completing a move adds no time. The starting
time for each player is a named constant of ten minutes, not a magic number
(instructor code review, Week 6).

`ChessClock(Clock timeSource)` gives both players the starting time and
starts White's clock at once (US-23 AC 1 and AC 2). Only the active player's
clock counts down. `remaining(PieceColor)` never returns less than zero
(AC 5). `switchTurn()` stops the active clock and starts the other player's
(AC 3). `stop()` freezes both remaining times (AC 8). Callers never pass
null: our code never passes null (instructor Week 6).

Constructors and getters are not their own cases (Session 08). Observing
`remaining` after construction or after `switchTurn` / `stop` is how those
cases are checked.

## Steps 1–3: Characteristics and partitions

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: elapsed time on the active clock, as the injected `Clock` advances | Interval at the edges 0 and the starting time | 0 ms; 1 ms; starting time − 1 ms; starting time; starting time + 1 ms |
| Input: `color` for `remaining` | Cases | White; Black |
| Input: whose clock is active | Cases | White (after construction); Black (after one `switchTurn`); neither (after `stop`) |
| Input: whether `stop` has been called | Boolean | false; true |
| Output: `remaining(color)` | Interval floored at zero | starting time; starting time − 1 ms; 1 ms; 0 (never negative) |
| Output: the inactive player's remaining time | Cases | unchanged while the other clock runs; frozen after `switchTurn` or `stop` |

## Step 4: Test Cases

### Method under test: `ChessClock(Clock timeSource)`, `remaining(PieceColor color)`, `switchTurn()`, and `stop()`

- **TC-CLOCK-01 — Both clocks start at ten minutes** ( :white_check_mark: )
  - **Method(s) under test**: `ChessClock(Clock)`, `remaining(PieceColor)`
  - **State of the system**: A fixed clock at instant `T0`; then
    `new ChessClock(thatClock)`.
  - **Expected output**: `remaining(WHITE)` is the starting time;
    `remaining(BLACK)` is the starting time.

- **TC-CLOCK-02 — White's clock has run for one millisecond** ( :white_check_mark: )
  - **Method(s) under test**: `remaining(PieceColor)`
  - **State of the system**: As TC-CLOCK-01, then the clock advances by 1 ms.
  - **Expected output**: `remaining(WHITE)` is the starting time minus 1 ms;
    `remaining(BLACK)` is still the starting time.

- **TC-CLOCK-03 — White's clock has one millisecond left** ( :white_check_mark: )
  - **Method(s) under test**: `remaining(PieceColor)`
  - **State of the system**: As TC-CLOCK-01, then the clock advances by the
    starting time minus 1 ms.
  - **Expected output**: `remaining(WHITE)` is 1 ms; `remaining(BLACK)` is
    still the starting time.

- **TC-CLOCK-04 — White's clock reaches exactly zero** ( :white_check_mark: )
  - **Method(s) under test**: `remaining(PieceColor)`
  - **State of the system**: As TC-CLOCK-01, then the clock advances by the
    starting time.
  - **Expected output**: `remaining(WHITE)` is zero; `remaining(BLACK)` is
    still the starting time.

- **TC-CLOCK-05 — A clock never shows less than zero** ( :white_check_mark: )
  - **Method(s) under test**: `remaining(PieceColor)`
  - **State of the system**: As TC-CLOCK-01, then the clock advances by the
    starting time plus 1 ms.
  - **Expected output**: `remaining(WHITE)` is zero.

- **TC-CLOCK-06 — Switching stops White and starts Black** ( :white_check_mark: )
  - **Method(s) under test**: `switchTurn()`, `remaining(PieceColor)`
  - **State of the system**: As TC-CLOCK-02, so White has used 1 ms, then
    `switchTurn()`, then the clock advances by 1 ms more.
  - **Expected output**: `remaining(WHITE)` stays at the starting time minus
    1 ms; `remaining(BLACK)` is the starting time minus 1 ms.

- **TC-CLOCK-07 — A second switch returns the turn to White** ( :white_check_mark: )
  - **Method(s) under test**: `switchTurn()`, `remaining(PieceColor)`
  - **State of the system**: As TC-CLOCK-06, then `switchTurn()`, then the
    clock advances by 1 ms more.
  - **Expected output**: `remaining(BLACK)` stays at the starting time minus
    1 ms; `remaining(WHITE)` is the starting time minus 2 ms.

- **TC-CLOCK-08 — Stopping freezes both remaining times** ( :x: )
  - **Method(s) under test**: `stop()`, `remaining(PieceColor)`
  - **State of the system**: As TC-CLOCK-02, then `stop()`, then the clock
    advances by the starting time.
  - **Expected output**: `remaining(WHITE)` stays at the starting time minus
    1 ms; `remaining(BLACK)` stays at the starting time.
