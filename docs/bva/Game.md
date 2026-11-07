# BVA Analysis for Game

`Game` decides whether a new chess game can start and, after a valid start,
stores the two players, the starting board, and the player to move. Issue #41
adds `passTurn`, which gives the turn to the other player after a legal move.
The test cases below use the game's public behavior to observe its state.

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

Out of scope: ending the game so further passes are refused (US-09 AC 8).
Resign, checkmate, and the draw stories will add that state.

- **TC-TURN-01 — Read the color to move after a start** ( :x: )
  - **Method(s) under test**: `start(String, String)`, `colorToMove()`
  - **State of the system**: `start("Alice", "Bob")` has completed.
  - **Expected output**: Returns `WHITE`.

- **TC-TURN-02 — Pass the turn after a start** ( :x: )
  - **Method(s) under test**: `start(String, String)`, `passTurn()`,
    `playerToMove()`, `playerToMoveName()`, `colorToMove()`
  - **State of the system**: `start("Alice", "Bob")` has completed, then
    `passTurn()`.
  - **Expected output**: `playerToMove()` returns the same object as
    `playerTwo()`; `playerToMoveName()` returns `Bob`; `colorToMove()`
    returns `BLACK`.

- **TC-TURN-03 — Pass the turn back to Player 1** ( :x: )
  - **Method(s) under test**: `start(String, String)`, `passTurn()`,
    `playerToMove()`, `colorToMove()`
  - **State of the system**: `start("Alice", "Bob")` has completed, then
    `passTurn()` twice.
  - **Expected output**: `playerToMove()` returns the same object as
    `playerOne()`; `colorToMove()` returns `WHITE`.

- **TC-TURN-04 — Reject passing the turn before a start** ( :x: )
  - **Method(s) under test**: `passTurn()`
  - **State of the system**: New game before a successful `start`.
  - **Expected output**: Throws `IllegalStateException`.

- **TC-TURN-05 — Reject reading the color to move before a start** ( :x: )
  - **Method(s) under test**: `colorToMove()`
  - **State of the system**: New game before a successful `start`.
  - **Expected output**: Throws `IllegalStateException`.
