# BVA Analysis for Game

`Game` decides whether a new chess game can start and, after a valid start,
stores the two players, the starting board, and the player to move. Issue #41
adds `passTurn`, which gives the turn to the other player after a legal move.
Issue #48 adds `endByCheckmate`, which ends the game when the player to move
is checkmated. The test cases below use the game's public behavior to observe
its state.

`start` returns nothing. It throws when a start is rejected. `passTurn`
returns nothing. It throws when the game has not started.

## Boundaries Considered

### Step 1: Inputs and outputs

- **Input: Player 1 name**
  - empty
  - only whitespace
  - one non-whitespace character
  - leading or trailing whitespace
  - mixed case
- **Input: Player 2 name**
  - empty
  - only whitespace
  - one non-whitespace character
  - leading or trailing whitespace
  - mixed case
- **Input: name relationship (after trim, ignoring case)**
  - the same name
  - the same name in a different case
  - the same name with surrounding whitespace
  - different valid names
- **Output: unsuccessful start**
  - `MissingPlayerNameException` when a trimmed name is empty
  - `DuplicatePlayerNameException` when the trimmed names match ignoring case
  - no players or board are created
  - the game remains unstarted
- **Output: successful start**
  - `start` completes
  - both players and the starting board are created
  - stored names are the trimmed input, with the entered case kept
  - Player 1 is White
  - Player 2 is Black
  - Player 1 is to move

### Step 2: Categories

- `start(String, String)` trims both names before the remaining checks.
- A name is missing when it is empty after trimming.
- The missing-name check runs before the duplicate-name check.
- Two names are duplicates when they are equal after trimming and ignoring
  case.
- A name is stored as the trimmed input. Only the comparison ignores case.
- A name is valid when it contains at least one non-whitespace character and
  the two names are not duplicates.
- `isStarted()` reports whether a valid start has completed.
- The player, board, and turn accessors are available after a successful
  start and are unavailable before one.

### Step 3: Boundary choices

- Check both names empty and each name empty independently.
- Check names containing only whitespace, including two whitespace-only
  names that would also match as duplicates if the empty check were skipped.
- No null names: the welcome view passes the text fields' contents, which are never
  null, so `start` does not check for null (instructor code review, Week 6).
- Check the smallest valid input: one non-whitespace character.
- Check names with leading or trailing whitespace.
- Check exact duplicates, case-only duplicates, and the same name with
  surrounding whitespace.
- Check that a successful start keeps the entered case after trimming.
- Check the state before and after a successful start.
- Check access to started-game data before and after setup.

## Step 4: Test Cases

### Method under test: `start(String player1Name, String player2Name)`

- **TC-START-01 — Reject two empty names** (implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is empty and Player
    2's name is empty.
  - **Expected output**: `start` throws `MissingPlayerNameException`;
    `isStarted()` returns `false`; no players or board are created.

- **TC-START-02 — Reject an empty Player 1 name** (implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is empty and Player
    2's name is `Bob`.
  - **Expected output**: `start` throws `MissingPlayerNameException`; the
    game remains unstarted.

- **TC-START-03 — Reject an empty Player 2 name** (implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `Alice` and Player
    2's name is empty.
  - **Expected output**: `start` throws `MissingPlayerNameException`; the
    game remains unstarted.

- **TC-START-04 — Reject whitespace-only names** (implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `"   "` and Player
    2's name is `"\t"`.
  - **Expected output**: `start` throws `MissingPlayerNameException`; no
    players or board are created; the game remains unstarted. The
    missing-name check wins over the duplicate check.

- **TC-START-05 — Accept one-character names** (implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `A` and Player 2's
    name is `B`.
  - **Expected output**: `start` completes; `isStarted()` returns `true`;
    setup completes.

- **TC-START-06 — Trim surrounding whitespace** (implemented)
  - **Method(s) under test**: `start(String, String)`, `playerOne()`,
    `playerTwo()`
  - **State of the system**: New game; Player 1's name is `" Alice "` and
    Player 2's name is `"\tBob\n"`.
  - **Expected output**: `start` completes; the stored names are `Alice`
    and `Bob`.

- **TC-START-07 — Reject duplicate names** (implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; both names are `Alex`.
  - **Expected output**: `start` throws `DuplicatePlayerNameException`;
    `isStarted()` returns `false`; no players or board are created.

- **TC-START-08 — Start with two valid names** (implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `Alice` and Player
    2's name is `Bob`.
  - **Expected output**: `start` completes; the game is started with both
    players, a board, and Player 1 to move.

- **TC-START-09 — Create Player 2 as Black** (implemented)
  - **Method(s) under test**: `start(String, String)`, `playerTwo()`
  - **State of the system**: New game; `start("Alice", " Bob ")` completes.
  - **Expected output**: `playerTwo()` returns a player named `Bob` with
    color `BLACK`.

- **TC-START-10 — Set Player 1 as the player to move** (implemented)
  - **Method(s) under test**: `start(String, String)`, `playerOne()`,
    `playerToMove()`
  - **State of the system**: New game; `start("Alice", "Bob")` completes.
  - **Expected output**: `playerToMove()` returns the same Player 1 object
    returned by `playerOne()`.

- **TC-START-11 — Create a board after a valid start** (implemented)
  - **Method(s) under test**: `start(String, String)`, `board()`
  - **State of the system**: New game; `start("Alice", "Bob")` completes.
  - **Expected output**: `board()` returns the starting board; a1 is occupied.

- **TC-START-12 — Reject the same name in a different case** (implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `Alice` and Player
    2's name is `ALICE`.
  - **Expected output**: `start` throws `DuplicatePlayerNameException`; the
    game remains unstarted.

- **TC-START-13 — Reject the same name after trim** (implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `" Alice "` and
    Player 2's name is `Alice`.
  - **Expected output**: `start` throws `DuplicatePlayerNameException`; the
    game remains unstarted.

- **TC-START-14 and TC-START-15** were removed. They passed a null name and
  expected `IllegalArgumentException`; `start` no longer checks for null.

- **TC-START-16 — Keep the entered case after trim** (implemented)
  - **Method(s) under test**: `start(String, String)`, `playerOne()`,
    `playerTwo()`
  - **State of the system**: New game; Player 1's name is `ALICE` and Player
    2's name is `bob`.
  - **Expected output**: `start` completes; the stored names are `ALICE`
    and `bob`.

### Method under test: `isStarted()`

- **TC-IS-STARTED-01 — Check a new game** (implemented)
  - **Method(s) under test**: `isStarted()`
  - **State of the system**: Newly constructed game before `start` succeeds.
  - **Expected output**: Returns `false`.

- **TC-IS-STARTED-02 — Check a successfully started game**
  (implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Returns `true`.

### Methods under test: `playerOne()`, `playerTwo()`, `playerToMove()`, and
`board()`

- **TC-ACCESSORS-01 — Reject access before setup** (implemented)
  - **Method(s) under test**: `playerOne()`, `playerTwo()`, `playerToMove()`,
    `board()`
  - **State of the system**: New game before a successful `start`.
  - **Expected output**: Each accessor throws `IllegalStateException`.

- **TC-ACCESSORS-02 — Return setup state after a valid start**
  (implemented)
  - **Method(s) under test**: `start(String, String)`, `playerOne()`,
    `playerTwo()`, `playerToMove()`, `board()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: The accessors return the two created players, the
    starting board, and Player 1 as the player to move.

### Methods under test: `playerOneName()`, `playerTwoName()`, and `playerToMoveName()`

These let a caller show a player's name without reaching through `Game` into
`Player`.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the game's state | Cases | unstarted; started with `Alice` and `Bob` |
| Output: the name | Cases | the stored, trimmed name of that player |
| Output: signal for an unstarted game | Cases | `IllegalStateException` |

The player to move is Player 1 right after a start. After `passTurn` it is
Player 2, then Player 1 again. `playerToMoveName()` and `colorToMove()`
follow that player. `colorToMove()` is a query so `BoardController` can
compare a piece's color without reaching through `Player`.

- **TC-NAMES-01 — Read Player 1's name after a start** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `playerOneName()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Returns `Alice`.

- **TC-NAMES-02 — Reject reading Player 1's name before a start** ( :white_check_mark: )
  - **Method(s) under test**: `playerOneName()`
  - **State of the system**: New game before a successful `start`.
  - **Expected output**: Throws `IllegalStateException`.

- **TC-NAMES-03 — Read Player 2's name after a start** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `playerTwoName()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Returns `Bob`.

- **TC-NAMES-04 — Reject reading Player 2's name before a start** ( :white_check_mark: )
  - **Method(s) under test**: `playerTwoName()`
  - **State of the system**: New game before a successful `start`.
  - **Expected output**: Throws `IllegalStateException`.

- **TC-NAMES-05 — Read the name of the player to move after a start** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `playerToMoveName()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Returns `Alice`.

- **TC-NAMES-06 — Reject reading the name of the player to move before a
  start** ( :white_check_mark: )
  - **Method(s) under test**: `playerToMoveName()`
  - **State of the system**: New game before a successful `start`.
  - **Expected output**: Throws `IllegalStateException`.

### Method under test: `passTurn()`

Issue #41. `Game` owns whose turn it is. `BoardController` calls `passTurn`
after a legal move. Callers never pass arguments, so there is no null case.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the game's state | Cases | unstarted; started with Player 1 to move; started after one pass (Player 2 to move) |
| Output: the player to move | Cases | Player 2; Player 1 |
| Output: the color to move | Cases | Black; White |
| Output: signal for an unstarted game | Cases | `IllegalStateException` |

Out of scope: refusing a pass after the game has ended (US-09 AC 8).
`BoardController` refuses every click once the game is over (issue #48), so
it never passes the turn after the end.

- **TC-TURN-01 — Read the color to move after a start** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `colorToMove()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Returns `WHITE`.

- **TC-TURN-02 — Pass the turn after a start** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `passTurn()`,
    `playerToMove()`, `playerToMoveName()`, `colorToMove()`
  - **State of the system**: `start("Alice", "Bob")` has completed, then
    `passTurn()`.
  - **Expected output**: `playerToMove()` returns the same object as
    `playerTwo()`; `playerToMoveName()` returns `Bob`; `colorToMove()`
    returns `BLACK`.

- **TC-TURN-03 — Pass the turn back to Player 1** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `passTurn()`,
    `playerToMove()`, `colorToMove()`
  - **State of the system**: `start("Alice", "Bob")` has completed, then
    `passTurn()` twice.
  - **Expected output**: `playerToMove()` returns the same object as
    `playerOne()`; `colorToMove()` returns `WHITE`.

- **TC-TURN-04 — Reject passing the turn before a start** ( :white_check_mark: )
  - **Method(s) under test**: `passTurn()`
  - **State of the system**: New game before a successful `start`.
  - **Expected output**: Throws `IllegalStateException`.

- **TC-TURN-05 — Reject reading the color to move before a start** ( :white_check_mark: )
  - **Method(s) under test**: `colorToMove()`
  - **State of the system**: New game before a successful `start`.
  - **Expected output**: Throws `IllegalStateException`.

### Methods under test: `endByCheckmate()`, `isOver()`, `winnerName()`, and `winnerColor()`

Issue #48 (GitHub US-15, docs US-15). Checkmate occurs when the player to move
is in check and has no legal move, so `BoardController` calls `endByCheckmate`
after `passTurn` when `Board.isCheckmated(colorToMove())` is true. The player
to move then loses, and the other player, who made the mating move, wins.
`winnerName()` and `winnerColor()` let `GameStatsController` show the winner
without reaching through `Player`. Before the game is over there is no winner,
so they throw instead of returning null. Callers never pass arguments, so there
is no null case.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the game's state | Cases | unstarted; started with Player 1 to move; started after one pass (Player 2 to move); over |
| Output: `isOver()` | Boolean | false; true |
| Output: the winner's name | Cases | Player 1's name; Player 2's name |
| Output: the winner's color | Cases | White; Black |
| Output: signal for an unstarted game or a game that is not over | Cases | `IllegalStateException` |

Out of scope: refusing resignation and draw offers after checkmate. Those
stories are not built yet, and they will check `isOver()`. Checkmate is checked
before any draw, so its priority over repetition and the seventy-five-move rule
comes with those stories.

- **TC-END-01 — A started game is not over** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `isOver()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Returns `false`.

- **TC-END-02 — Checkmate ends the game** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `endByCheckmate()`,
    `isOver()`
  - **State of the system**: `start("Alice", "Bob")` has completed, then
    `endByCheckmate()`.
  - **Expected output**: `isOver()` returns `true`.

- **TC-END-03 — Player 2 wins when Player 1 is checkmated** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `endByCheckmate()`,
    `winnerName()`, `winnerColor()`
  - **State of the system**: `start("Alice", "Bob")` has completed, so
    Player 1 is to move, then `endByCheckmate()`.
  - **Expected output**: `winnerName()` returns `Bob`; `winnerColor()`
    returns `BLACK`.

- **TC-END-04 — Player 1 wins when Player 2 is checkmated** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `passTurn()`,
    `endByCheckmate()`, `winnerName()`, `winnerColor()`
  - **State of the system**: `start("Alice", "Bob")` has completed, then
    `passTurn()`, so Player 2 is to move, then `endByCheckmate()`.
  - **Expected output**: `winnerName()` returns `Alice`; `winnerColor()`
    returns `WHITE`.

- **TC-END-05 — Reject reading the winner's name before the game is over**
  ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `winnerName()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Throws `IllegalStateException`.

- **TC-END-06 — Reject reading the winner's color before the game is over**
  ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `winnerColor()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Throws `IllegalStateException`.

- **TC-END-07 — Reject ending the game before a start** ( :white_check_mark: )
  - **Method(s) under test**: `endByCheckmate()`
  - **State of the system**: New game before a successful `start`.
  - **Expected output**: Throws `IllegalStateException`.

### Method under test: `isPlayerToMoveInCheck()`

Issue #44 (GitHub US-11 AC 6). `GameStatsController` shows that the player to
move is in check. `Game` owns the board and knows who is to move, so it asks
`board().isInCheck(colorToMove())` and the controller does not reach through
`board()`. The positions are played with `board().move` on the game's own
board, and the game passes the turn after each move, as `BoardController`
does. Fool's mate: f2 to f3, e7 to e5, g2 to g4, d8 to h4, so the black queen
checks the white king and White is to move. Queen check on Black: e2 to e4,
f7 to f6, d1 to h5, so the white queen checks the black king and Black is to
move. The method takes no arguments, so there is no null case.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the game's state | Cases | unstarted; started |
| Input: the color to move | Cases | White; Black |
| Input: whether that player's king is in check | Boolean | no (the starting board); yes (Fool's mate for White; queen check on Black) |
| Output: the answer | Boolean | true; false |
| Output: signal for an unstarted game | Cases | `IllegalStateException` |

- **TC-CHECK-01 — The player to move is not in check at the start** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `isPlayerToMoveInCheck()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Returns `false`.

- **TC-CHECK-02 — White to move is in check** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `passTurn()`,
    `isPlayerToMoveInCheck()`
  - **State of the system**: `start("Alice", "Bob")` has completed, then
    Fool's mate is played on `board()` with `passTurn()` after each of the
    four moves, so White is to move.
  - **Expected output**: Returns `true`.

- **TC-CHECK-03 — Black to move is in check** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `passTurn()`,
    `isPlayerToMoveInCheck()`
  - **State of the system**: `start("Alice", "Bob")` has completed, then the
    queen check on Black is played on `board()` with `passTurn()` after each
    of the three moves, so Black is to move.
  - **Expected output**: Returns `true`.

- **TC-CHECK-04 — Reject looking for check before a start** ( :white_check_mark: )
  - **Method(s) under test**: `isPlayerToMoveInCheck()`
  - **State of the system**: New game before a successful `start`.
  - **Expected output**: Throws `IllegalStateException`.

### Methods under test: `canClaimDrawByRepetition()`, `canClaimDrawByRepetitionWith(Location from, Location to)`, `isFivefoldRepetition()`, `endByRepetition()`, and `isDrawnByRepetition()`

Issue #59 (GitHub US-19, docs US-18). `Game` keeps the position after `start`
and after every `passTurn`, from `board().position(colorToMove())`. The last
one kept is the current position. Two positions are the same when
`Position.isSameAs` says so.

- `canClaimDrawByRepetition()` is true when the current position has occurred
  at least three times, counting the current one.
- `canClaimDrawByRepetitionWith(from, to)` is true when the position after
  that move, with the other player to move, has already occurred at least
  twice, so the move would make it occur a third time. The position comes from
  `board().positionAfterMove(from, to, ...)`, so the board does not change.
- `isFivefoldRepetition()` is true when the current position has occurred at
  least five times.
- `endByRepetition()` ends the game as a draw. `isOver()` is then true and
  `isDrawnByRepetition()` is true. A draw has no winner, so `winnerName()` and
  `winnerColor()` still throw instead of returning null.

The three and five occurrences are named constants, not magic numbers
(instructor code review, Week 6). `BoardController` decides when a claim is
made and checks checkmate before a fivefold repetition, so `Game` only answers
and records.

Every case repeats the starting position with the knights. One round trip is
g1 `(6, 1)` to f3 `(5, 3)`, g8 `(6, 8)` to f6 `(5, 6)`, f3 to g1, and f6 to
g8, each made with `board().move` and followed by `passTurn()`. It brings
back the starting position with White to move, so after n round trips the
starting position has occurred n + 1 times.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: occurrences of the current position, counting itself | Interval at the edges 3 and 5 | 1 (a new game); 2 (one round trip); 3 (two); 4 (three); 5 (four) |
| Input: the current position after a repetition | Cases | the repeated position; a new position after one more move (e2 `(4, 2)` to e4 `(4, 4)`) |
| Input: occurrences the position after a selected move would have | Cases | the second (one round trip less its last move, then f6 to g8); the third (two round trips less the last move, then f6 to g8); a new position (two round trips less the last move, then b8 `(1, 8)` to c6 `(2, 6)`) |
| Input: the game's state | Cases | unstarted; started; drawn by repetition |
| Output: each answer | Boolean | true; false |
| Output: signal for an unstarted game | Cases | `IllegalStateException` |

- **TC-REP-01 — A new game cannot claim a repetition** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`,
    `canClaimDrawByRepetition()`
  - **State of the system**: `start("Alice", "Bob")` has completed, so the
    starting position has occurred once.
  - **Expected output**: Returns `false`.

- **TC-REP-02 — A position seen twice cannot be claimed** ( :white_check_mark: )
  - **Method(s) under test**: `canClaimDrawByRepetition()`
  - **State of the system**: Started game after one round trip, so the
    starting position has occurred twice.
  - **Expected output**: Returns `false`.

- **TC-REP-03 — A position seen three times can be claimed** ( :white_check_mark: )
  - **Method(s) under test**: `canClaimDrawByRepetition()`
  - **State of the system**: Started game after two round trips, so the
    starting position has occurred three times.
  - **Expected output**: Returns `true`.

- **TC-REP-04 — A new position after a repetition cannot be claimed** ( :white_check_mark: )
  - **Method(s) under test**: `canClaimDrawByRepetition()`
  - **State of the system**: Started game after two round trips, then e2 to
    e4 and `passTurn()`.
  - **Expected output**: Returns `false`.

- **TC-REP-05 — A move that makes the third occurrence can be claimed**
  ( :white_check_mark: )
  - **Method(s) under test**: `canClaimDrawByRepetitionWith(Location,
    Location)`
  - **State of the system**: Started game after two round trips less the
    last move, so Black is to move; ask about f6 to g8.
  - **Expected output**: Returns `true`.

- **TC-REP-06 — A move that makes the second occurrence cannot be claimed**
  ( :white_check_mark: )
  - **Method(s) under test**: `canClaimDrawByRepetitionWith(Location,
    Location)`
  - **State of the system**: Started game after one round trip less the last
    move, so Black is to move; ask about f6 to g8.
  - **Expected output**: Returns `false`.

- **TC-REP-07 — A move to a new position cannot be claimed** ( :white_check_mark: )
  - **Method(s) under test**: `canClaimDrawByRepetitionWith(Location,
    Location)`
  - **State of the system**: Started game after two round trips less the
    last move, so Black is to move; ask about b8 to c6.
  - **Expected output**: Returns `false`.

- **TC-REP-08 — A position seen four times is not a fivefold repetition**
  ( :white_check_mark: )
  - **Method(s) under test**: `isFivefoldRepetition()`
  - **State of the system**: Started game after three round trips, so the
    starting position has occurred four times.
  - **Expected output**: Returns `false`.

- **TC-REP-09 — A position seen five times is a fivefold repetition** ( :white_check_mark: )
  - **Method(s) under test**: `isFivefoldRepetition()`
  - **State of the system**: Started game after four round trips, so the
    starting position has occurred five times.
  - **Expected output**: Returns `true`.

- **TC-REP-10 — A started game is not drawn by repetition** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`,
    `isDrawnByRepetition()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Returns `false`.

- **TC-REP-11 — A repetition ends the game as a draw** ( :white_check_mark: )
  - **Method(s) under test**: `endByRepetition()`, `isOver()`,
    `isDrawnByRepetition()`
  - **State of the system**: `start("Alice", "Bob")` has completed, then
    `endByRepetition()`.
  - **Expected output**: `isOver()` returns `true`; `isDrawnByRepetition()`
    returns `true`.

- **TC-REP-12 — Reject ending the game by repetition before a start** ( :white_check_mark: )
  - **Method(s) under test**: `endByRepetition()`
  - **State of the system**: New game before a successful `start`.
  - **Expected output**: Throws `IllegalStateException`.

### Methods under test: `canOfferDraw()`, `offerDraw(PieceColor offeringColor)`, `isDrawOffered()`, `acceptDrawOffer()`, `declineDrawOffer()`, `isDrawnByAgreement()`, and `passTurn()` with an offer

Issue #61 (GitHub US-21, docs US-21). `Game` keeps at most one draw offer and
the color of the player who made it.

- `canOfferDraw()` is true when both players have made at least one move, the
  game is not over, and no offer is pending (US-21 AC 1 and AC 6). A move is a
  `passTurn()`, so both players have moved after two. The two moves are a
  named constant, not a magic number (instructor code review, Week 6).
- `offerDraw(offeringColor)` makes an offer from that player when
  `canOfferDraw()` is true. Otherwise it is refused and nothing changes
  (alternate flow 1.a). Either color may offer, whoever is to move (AC 2).
- `isDrawOffered()` is true while an offer is pending. An offer ends when the
  game ends for another reason, so it is false once the game is over (AC 3).
- `acceptDrawOffer()` ends the game as a draw when an offer is pending.
  `isOver()` and `isDrawnByAgreement()` are then true (AC 4). With no offer,
  it does nothing.
- `declineDrawOffer()` ends the offer, and the game continues (AC 5).
- `passTurn()` ends the offer when the player who moved is the opponent of
  the player who offered (AC 3 and AC 5). The offering player's own move
  keeps it.

An offer cannot be withdrawn, so there is no method for that (AC 3). A draw
has no winner, so `winnerName()` and `winnerColor()` still throw instead of
returning null. In the hot-seat window, the opponent answers with Accept or
Decline, so `Game` does not ask who accepts. `offerDraw` throws before a
start, like the other actions; the queries need no started-game check,
because a new `Game` has no moves and no offer.

Each move below is a `passTurn()`, because the offer depends on who moved,
not on which piece. Every other way the game ends also ends the offer,
through `isOver()`, as checkmate does in TC-AGREE-15.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: moves made, as `passTurn()` calls | Interval at the edge 2 | 0 (a new game); 1 (White has moved); 2 (both have moved) |
| Input: `offeringColor` | Cases | White; Black |
| Input: the player who moves while an offer is pending | Cases | the opponent of the offering player; the offering player |
| Input: the game's state | Cases | unstarted; started; offer pending; drawn by agreement; over by checkmate; drawn by repetition |
| Output: each answer | Boolean | true; false |
| Output: signal for an unstarted game | Cases | `IllegalStateException` |

- **TC-AGREE-01 — A new game cannot offer a draw** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `canOfferDraw()`
  - **State of the system**: `start("Alice", "Bob")` has completed, so no
    move has been made.
  - **Expected output**: Returns `false`.

- **TC-AGREE-02 — A draw cannot be offered after one move** ( :white_check_mark: )
  - **Method(s) under test**: `passTurn()`, `canOfferDraw()`
  - **State of the system**: `start("Alice", "Bob")` has completed, then
    `passTurn()` once, so only White has moved.
  - **Expected output**: Returns `false`.

- **TC-AGREE-03 — A draw can be offered after both players have moved** ( :white_check_mark: )
  - **Method(s) under test**: `passTurn()`, `canOfferDraw()`
  - **State of the system**: `start("Alice", "Bob")` has completed, then
    `passTurn()` twice, so both players have moved.
  - **Expected output**: Returns `true`.

- **TC-AGREE-04 — A started game has no draw offer** ( :white_check_mark: )
  - **Method(s) under test**: `start(String, String)`, `isDrawOffered()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Returns `false`.

- **TC-AGREE-05 — An offer after both players have moved is pending** ( :white_check_mark: )
  - **Method(s) under test**: `offerDraw(PieceColor)`, `isDrawOffered()`
  - **State of the system**: As TC-AGREE-03, then `offerDraw(WHITE)`.
  - **Expected output**: Returns `true`.

- **TC-AGREE-06 — An offer before both players have moved is refused** ( :white_check_mark: )
  - **Method(s) under test**: `offerDraw(PieceColor)`, `isDrawOffered()`
  - **State of the system**: As TC-AGREE-02, so Black is to move, then
    `offerDraw(BLACK)`.
  - **Expected output**: Returns `false`.

- **TC-AGREE-07 — A second offer cannot be made while one is pending** ( :white_check_mark: )
  - **Method(s) under test**: `offerDraw(PieceColor)`, `canOfferDraw()`
  - **State of the system**: As TC-AGREE-05.
  - **Expected output**: `canOfferDraw()` returns `false`.

- **TC-AGREE-08 — Reject offering a draw before a start** ( :white_check_mark: )
  - **Method(s) under test**: `offerDraw(PieceColor)`
  - **State of the system**: New game before a successful `start`; then
    `offerDraw(WHITE)`.
  - **Expected output**: Throws `IllegalStateException`.

- **TC-AGREE-09 — A started game is not drawn by agreement** ( :x: )
  - **Method(s) under test**: `start(String, String)`, `isDrawnByAgreement()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Returns `false`.

- **TC-AGREE-10 — Accepting an offer ends the game as a draw** ( :x: )
  - **Method(s) under test**: `acceptDrawOffer()`, `isOver()`,
    `isDrawnByAgreement()`
  - **State of the system**: As TC-AGREE-05, then `acceptDrawOffer()`.
  - **Expected output**: `isOver()` returns `true`; `isDrawnByAgreement()`
    returns `true`.

- **TC-AGREE-11 — Accepting with no offer does nothing** ( :x: )
  - **Method(s) under test**: `acceptDrawOffer()`, `isOver()`
  - **State of the system**: As TC-AGREE-03, then `acceptDrawOffer()`.
  - **Expected output**: `isOver()` returns `false`.

- **TC-AGREE-12 — Declining ends the offer** ( :x: )
  - **Method(s) under test**: `declineDrawOffer()`, `isDrawOffered()`,
    `isOver()`
  - **State of the system**: As TC-AGREE-05, then `declineDrawOffer()`.
  - **Expected output**: `isDrawOffered()` returns `false`; `isOver()`
    returns `false`.

- **TC-AGREE-13 — The opponent's move ends the offer** ( :x: )
  - **Method(s) under test**: `passTurn()`, `isDrawOffered()`
  - **State of the system**: As TC-AGREE-03, so White is to move, then
    `offerDraw(BLACK)`, then `passTurn()` for White's move.
  - **Expected output**: Returns `false`.

- **TC-AGREE-14 — The offering player's own move keeps the offer** ( :x: )
  - **Method(s) under test**: `passTurn()`, `isDrawOffered()`
  - **State of the system**: As TC-AGREE-03, so White is to move, then
    `offerDraw(WHITE)`, then `passTurn()` for White's move.
  - **Expected output**: Returns `true`.

- **TC-AGREE-15 — An offer ends when the game ends another way** ( :x: )
  - **Method(s) under test**: `endByCheckmate()`, `isDrawOffered()`
  - **State of the system**: As TC-AGREE-05, then `endByCheckmate()`.
  - **Expected output**: Returns `false`.

- **TC-AGREE-16 — A draw cannot be offered once the game is over** ( :x: )
  - **Method(s) under test**: `endByRepetition()`, `canOfferDraw()`
  - **State of the system**: As TC-AGREE-03, then `endByRepetition()`.
  - **Expected output**: Returns `false`.
