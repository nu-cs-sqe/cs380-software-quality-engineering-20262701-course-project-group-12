# BVA Analysis for Player

`Player` stores a player's trimmed name and assigned color. The test cases
below use the player's public behavior to observe the stored values.

## Boundaries Considered

### Step 1: Inputs and outputs

- **Input: name**
  - one character
  - leading or trailing whitespace
  - a longer valid name
- **Input: color**
  - White
  - Black
- **Output: player state**
  - the stored name
  - the stored color
  - unchanged values after construction

### Step 2: Categories

- The constructor receives a name and a color.
- The name supplied by `Game.start` is already trimmed.
- Both chess colors are valid player colors.
- `getName()` returns the player's name.
- `getColor()` returns the player's color.
- A player's name and color do not change after construction.

### Step 3: Boundary choices

- Check the smallest valid name: one character.
- Check a name with surrounding whitespace after `Game.start` trims it.
- Check both `PieceColor.WHITE` and `PieceColor.BLACK`.
- Check that getters return the values supplied at construction.

## Step 4: Test Cases

### Method under test: `Player(String name, PieceColor color)`

- **TC-PLAYER-CONSTRUCTOR-01 — Construct a White player**
  (not implemented)
  - **Method(s) under test**: `Player(String, PieceColor)`, `getName()`,
    `getColor()`
  - **State of the system**: Construct a player with name `A` and color
    `PieceColor.WHITE`.
  - **Expected output**: The player stores name `A` and color `WHITE`.

- **TC-PLAYER-CONSTRUCTOR-02 — Construct a Black player**
  (not implemented)
  - **Method(s) under test**: `Player(String, PieceColor)`, `getName()`,
    `getColor()`
  - **State of the system**: Construct a player with name `B` and color
    `PieceColor.BLACK`.
  - **Expected output**: The player stores name `B` and color `BLACK`.

### Method under test: `getName()`

- **TC-PLAYER-NAME-01 — Return a one-character name** (not implemented)
  - **Method(s) under test**: `Player(String, PieceColor)`, `getName()`
  - **State of the system**: Player constructed with name `A` and color
    `WHITE`.
  - **Expected output**: Returns `A`.

- **TC-PLAYER-NAME-02 — Return the trimmed name from setup**
  (not implemented)
  - **Method(s) under test**: `Player(String, PieceColor)`, `getName()`
  - **State of the system**: `Game.start` receives `" Alice "` and creates
    the player with the trimmed name `Alice`.
  - **Expected output**: Returns `Alice`.

### Method under test: `getColor()`

- **TC-PLAYER-COLOR-01 — Return White** (not implemented)
  - **Method(s) under test**: `Player(String, PieceColor)`, `getColor()`
  - **State of the system**: Player constructed with name `Alice` and color
    `PieceColor.WHITE`.
  - **Expected output**: Returns `PieceColor.WHITE`.

- **TC-PLAYER-COLOR-02 — Return Black** (not implemented)
  - **Method(s) under test**: `Player(String, PieceColor)`, `getColor()`
  - **State of the system**: Player constructed with name `Bob` and color
    `PieceColor.BLACK`.
  - **Expected output**: Returns `PieceColor.BLACK`.
