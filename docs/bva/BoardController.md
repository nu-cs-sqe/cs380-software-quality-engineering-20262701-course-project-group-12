# BVA Analysis for BoardController

`BoardController` is the controller for `BoardView`. Its job is to answer with what `Board` reports about a square.

- `BoardController(Board board)`: requires the starting board. Throws `IllegalArgumentException` if `board` is null.
- `boolean isOccupied(Location location)`: returns `board.isOccupied(location)`.
- `Piece pieceAt(Location location)`: returns `board.pieceAt(location)`. It does not choose a different piece. `Board` throws `IllegalArgumentException` for an empty square or a null location, and the controller lets that exception through. No method returns null.

The starting position itself is specified on `Board`. These cases check the pass-through. Each test mocks `Board` with EasyMock, because the controller does not own the board's behavior.

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
| Input: what the board does | Cases | returns a piece; throws `IllegalArgumentException` for an empty square |
| Input: `location` | Pointer | null; a location |
| Output: the returned occupant | Pointer | the same piece instance the board returned |
| Output: signal when the board rejects the location | Cases | the same `IllegalArgumentException` the board throws |

### `isOccupied(Location location)`

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: what the board answers | Boolean | true; false |
| Input: `location` | Pointer | null; a location |
| Output: the answer | Boolean | the same answer the board gave |
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
| TC4: the square is empty | controller from TC1; the board throws `IllegalArgumentException` for the empty location | throws that same `IllegalArgumentException` | :white_check_mark: |
| TC5: the location is null | controller from TC1; the board throws `IllegalArgumentException` for that call | throws that same `IllegalArgumentException` | :white_check_mark: |

### Method under test: `isOccupied(Location location)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC6: the square has a piece | controller from TC1; the board answers true for a1 `(0, 1)` | returns true | :white_check_mark: |
| TC7: the square is empty | controller from TC1; the board answers false for a3 `(0, 3)` | returns false | :white_check_mark: |
| TC8: the location is null | controller from TC1; the board throws `IllegalArgumentException` for that call | throws that same `IllegalArgumentException` | :white_check_mark: |
