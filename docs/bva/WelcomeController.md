# BVA Analysis for WelcomeController

`WelcomeController` receives the two player names from the welcome view and
coordinates starting the game. The test cases below use the controller's
public behavior and mocked view/model collaborators to observe the result.

## Boundaries Considered

### Step 1: Inputs and outputs

- **Input: Player 1 name**
  - empty
  - only whitespace
  - one non-whitespace character
  - a name with leading or trailing whitespace
- **Input: Player 2 name**
  - empty
  - only whitespace
  - one non-whitespace character
  - a name with leading or trailing whitespace
- **Input: game start result**
  - unsuccessful start
  - successful start
- **Output: unsuccessful start**
  - the required-names message is shown
  - the welcome view remains open
- **Output: successful start**
  - the welcome view is closed
  - the game window is opened

### Step 2: Categories

- `startGame(String, String)` passes both field values to `Game.start`.
- The controller does not trim or validate names; `Game.start` owns that rule.
- A failed game start keeps the welcome window open and shows the required-name
  message.
- A successful game start closes the welcome window and opens the game window.
- Duplicate names are valid because the game accepts any two non-blank names.

### Step 3: Boundary choices

- Check each name independently when it is empty.
- Check names containing only whitespace.
- Check the smallest valid input: one non-whitespace character.
- Check names with leading or trailing whitespace to verify that the original
  field values are passed to `Game.start`.
- Check both unsuccessful and successful results from `Game.start`.
- Check duplicate valid names because duplicate names are explicitly allowed.

## Step 4: Test Cases

### Method under test: `startGame(String player1Name, String player2Name)`

- **TC-STARTGAME-01 — Reject an empty Player 1 name** (implemented)
  - **Method(s) under test**: `startGame(String, String)`, `Game.start`
  - **State of the system**: The controller has an unstarted game and a
    welcome view. Player 1's name is empty and Player 2's name is `Bob`.
    `Game.start` returns `false`.
  - **Expected output**: The controller passes both names to `Game.start`,
    calls `showNamesRequired()`, and does not close the welcome view or open
    the game window.

- **TC-STARTGAME-02 — Reject an empty Player 2 name** (implemented)
  - **Method(s) under test**: `startGame(String, String)`, `Game.start`
  - **State of the system**: The controller has an unstarted game and a
    welcome view. Player 1's name is `Alice` and Player 2's name is empty.
    `Game.start` returns `false`.
  - **Expected output**: The controller passes both names to `Game.start`,
    calls `showNamesRequired()`, and does not close the welcome view or open
    the game window.

- **TC-STARTGAME-03 — Reject whitespace-only names** (implemented)
  - **Method(s) under test**: `startGame(String, String)`, `Game.start`
  - **State of the system**: The controller has an unstarted game and a
    welcome view. Player 1's name is `"   "` and Player 2's name is `"\t"`.
    `Game.start` returns `false`.
  - **Expected output**: The controller calls `showNamesRequired()` and does
    not close the welcome view or open the game window.

- **TC-STARTGAME-04 — Start with the smallest valid names**
  (implemented)
  - **Method(s) under test**: `startGame(String, String)`, `Game.start`
  - **State of the system**: The controller has an unstarted game and a
    welcome view. Player 1's name is `A` and Player 2's name is `B`.
    `Game.start` returns `true`.
  - **Expected output**: The controller closes the welcome view and opens the
    game window.

- **TC-STARTGAME-05 — Pass names with surrounding whitespace**
  (implemented)
  - **Method(s) under test**: `startGame(String, String)`, `Game.start`
  - **State of the system**: The controller has an unstarted game and a
    welcome view. Player 1's name is `" Alice "` and Player 2's name is
    `"\tBob\n"`. `Game.start` returns `true`.
  - **Expected output**: The controller passes the field values to
    `Game.start`, closes the welcome view, and opens the game window.

- **TC-STARTGAME-06 — Allow duplicate valid names**
  (implemented)
  - **Method(s) under test**: `startGame(String, String)`, `Game.start`
  - **State of the system**: The controller has an unstarted game and a
    welcome view. Both player names are `Alex`. `Game.start` returns `true`.
  - **Expected output**: The controller closes the welcome view and opens the
    game window.

- **TC-STARTGAME-07 — Start after both names are valid**
  (implemented)
  - **Method(s) under test**: `startGame(String, String)`, `Game.start`
  - **State of the system**: The controller has an unstarted game and a
    welcome view. Player 1's name is `Alice` and Player 2's name is `Bob`.
    `Game.start` returns `true`.
  - **Expected output**: The controller closes the welcome view and opens the
    game window for the started game.
