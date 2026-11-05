# BVA Analysis for Game

`Game` decides whether a new chess game can start and, after a valid start,
stores the two players, the starting board, and the player to move. The test
cases below use the game's public behavior to observe its state.

`start` returns nothing. It throws when a start is rejected.

## Boundaries Considered

### Step 1: Inputs and outputs

- **Input: Player 1 name**
  - null
  - empty
  - only whitespace
  - one non-whitespace character
  - leading or trailing whitespace
  - mixed case
- **Input: Player 2 name**
  - null
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
  - `IllegalArgumentException` when a name is null
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

- A null name is rejected before trim.
- `start(String, String)` then trims both names before the remaining checks.
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
- Check one null name at a time.
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

- **TC-START-07 — Reject duplicate names** (:x:)
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
  - **Expected output**: `board()` returns a non-null starting board.

- **TC-START-12 — Reject the same name in a different case** (:x:)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `Alice` and Player
    2's name is `ALICE`.
  - **Expected output**: `start` throws `DuplicatePlayerNameException`; the
    game remains unstarted.

- **TC-START-13 — Reject the same name after trim** (:x:)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `" Alice "` and
    Player 2's name is `Alice`.
  - **Expected output**: `start` throws `DuplicatePlayerNameException`; the
    game remains unstarted.

- **TC-START-14 — Reject a null Player 1 name** (:x:)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `null` and Player
    2's name is `Bob`.
  - **Expected output**: `start` throws `IllegalArgumentException`; the game
    remains unstarted.

- **TC-START-15 — Reject a null Player 2 name** (:x:)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `Alice` and Player
    2's name is `null`.
  - **Expected output**: `start` throws `IllegalArgumentException`; the game
    remains unstarted.

- **TC-START-16 — Keep the entered case after trim** (:x:)
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
