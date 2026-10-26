# BVA Analysis for BoardController

`BoardController` is the controller for `BoardView`. In this feature its only job is to answer with the piece `Board` reports.

- `BoardController(Board board)`: requires the starting board. Throws `IllegalArgumentException` if `board` is null.
- `Piece pieceAt(Location location)`: returns `board.pieceAt(location)`. It does not choose a different piece.

The starting position itself is specified on `Board`. These cases check the pass-through. A test can supply a board double, because the controller does not place pieces.

## Steps 1-3: Characteristics and partitions

### `BoardController(Board board)`

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `board` | Pointer | null; a board |
| Output: the controller | Cases | the controller is created |
| Output: signal for a missing board | Cases | `IllegalArgumentException` |

### `pieceAt(Location location)`

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: what the board returns | Pointer, and cases | a piece; null |
| Input: `location` | Pointer | null; a location |
| Output: the returned occupant | Pointer | the same piece instance the board returned; null |
| Output: signal when the board rejects the location | Cases | the same `IllegalArgumentException` the board throws |

## Step 4: Test cases (each-choice strategy)

### Method under test: `BoardController(Board board)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC1: a board is provided | `board` is a `Board` | the controller is created | :white_check_mark: |
| TC2: the board is missing | `board` is null | throws `IllegalArgumentException` | :white_check_mark: |

### Method under test: `pieceAt(Location location)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC3: the square has a piece | controller from TC1; the board returns a white rook for the location | returns that same white rook | :white_check_mark: |
| TC4: the square is empty | controller from TC1; the board returns null for the location | returns null | :white_check_mark: |
| TC5: the location is null | controller from TC1; the board throws `IllegalArgumentException` for that call | throws that same `IllegalArgumentException` | :x: |
