# BVA Analysis for Game

`Game` decides whether a new chess game can start and, after a valid start,
stores the two players, the starting board, and the player to move. The test
cases below use the game's public behavior to observe its state.

## Boundaries Considered

### Step 1: Inputs and outputs

- **Input: Player 1 name**
  - empty
  - only whitespace
  - one non-whitespace character
  - leading or trailing whitespace
- **Input: Player 2 name**
  - empty
  - only whitespace
  - one non-whitespace character
  - leading or trailing whitespace
- **Input: name relationship**
  - duplicate valid names
  - different valid names
- **Output: unsuccessful start**
  - `false`
  - no players or board are created
  - the game remains unstarted
- **Output: successful start**
  - `true`
  - both players and the starting board are created
  - Player 1 is White
  - Player 2 is Black
  - Player 1 is to move

### Step 2: Categories

- `start(String, String)` trims both names before checking them.
- A name is invalid when it is empty after trimming.
- A name is valid when it contains at least one non-whitespace character.
- Duplicate names are allowed.
- `isStarted()` reports whether a valid start has completed.
- The player, board, and turn accessors are available after a successful
  start and are unavailable before one.

### Step 3: Boundary choices

- Check both names empty and each name empty independently.
- Check names containing only whitespace.
- Check the smallest valid input: one non-whitespace character.
- Check names with leading or trailing whitespace.
- Check duplicate and different valid names.
- Check the state before and after a successful start.
- Check access to started-game data before and after setup.

## Step 4: Test Cases

### Method under test: `start(String player1Name, String player2Name)`

- **TC-START-01 — Reject two empty names** (not implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is empty and Player
    2's name is empty.
  - **Expected output**: `start` returns `false`; `isStarted()` returns
    `false`; no players or board are created.

- **TC-START-02 — Reject an empty Player 1 name** (not implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is empty and Player
    2's name is `Bob`.
  - **Expected output**: `start` returns `false`; the game remains
    unstarted.

- **TC-START-03 — Reject an empty Player 2 name** (not implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `Alice` and Player
    2's name is empty.
  - **Expected output**: `start` returns `false`; the game remains
    unstarted.

- **TC-START-04 — Reject whitespace-only names** (not implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `"   "` and Player
    2's name is `"\t"`.
  - **Expected output**: `start` returns `false`; no players or board are
    created; the game remains unstarted.

- **TC-START-05 — Accept one-character names** (not implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `A` and Player 2's
    name is `B`.
  - **Expected output**: `start` returns `true`; `isStarted()` returns
    `true`; setup completes.

- **TC-START-06 — Trim surrounding whitespace** (implemented)
  - **Method(s) under test**: `start(String, String)`, `playerOne()`,
    `playerTwo()`
  - **State of the system**: New game; Player 1's name is `" Alice "` and
    Player 2's name is `"\tBob\n"`.
  - **Expected output**: `start` returns `true`; the stored names are
    `Alice` and `Bob`.

- **TC-START-07 — Allow duplicate names** (implemented)
  - **Method(s) under test**: `start(String, String)`, `playerOne()`,
    `playerTwo()`
  - **State of the system**: New game; both names are `Alex`.
  - **Expected output**: `start` returns `true`; both players are created
    with the name `Alex`.

- **TC-START-08 — Start with two valid names** (not implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: New game; Player 1's name is `Alice` and Player
    2's name is `Bob`.
  - **Expected output**: `start` returns `true`; the game is started with
    both players, a board, and Player 1 to move.

- **TC-START-09 — Create Player 2 as Black** (implemented)
  - **Method(s) under test**: `start(String, String)`, `playerTwo()`
  - **State of the system**: New game; `start("Alice", " Bob ")` returns
    `true`.
  - **Expected output**: `playerTwo()` returns a player named `Bob` with
    color `BLACK`.

- **TC-START-10 — Set Player 1 as the player to move** (implemented)
  - **Method(s) under test**: `start(String, String)`, `playerOne()`,
    `playerToMove()`
  - **State of the system**: New game; `start("Alice", "Bob")` returns
    `true`.
  - **Expected output**: `playerToMove()` returns the same Player 1 object
    returned by `playerOne()`.

- **TC-START-11 — Create a board after a valid start** (implemented)
  - **Method(s) under test**: `start(String, String)`, `board()`
  - **State of the system**: New game; `start("Alice", "Bob")` returns
    `true`.
  - **Expected output**: `board()` returns a non-null starting board.

### Method under test: `isStarted()`

- **TC-IS-STARTED-01 — Check a new game** (not implemented)
  - **Method(s) under test**: `isStarted()`
  - **State of the system**: Newly constructed game before `start` succeeds.
  - **Expected output**: Returns `false`.

- **TC-IS-STARTED-02 — Check a successfully started game**
  (not implemented)
  - **Method(s) under test**: `start(String, String)`, `isStarted()`
  - **State of the system**: `start("Alice", "Bob")` has returned `true`.
  - **Expected output**: Returns `true`.

### Methods under test: `playerOne()`, `playerTwo()`, `playerToMove()`, and
`board()`

- **TC-ACCESSORS-01 — Reject access before setup** (not implemented)
  - **Method(s) under test**: `playerOne()`, `playerTwo()`, `playerToMove()`,
    `board()`
  - **State of the system**: New game before a successful `start`.
  - **Expected output**: Each accessor throws `IllegalStateException`.

- **TC-ACCESSORS-02 — Return setup state after a valid start**
  (not implemented)
  - **Method(s) under test**: `start(String, String)`, `playerOne()`,
    `playerTwo()`, `playerToMove()`, `board()`
  - **State of the system**: `start("Alice", "Bob")` has returned `true`.
  - **Expected output**: The accessors return the two created players, the
    starting board, and Player 1 as the player to move.
