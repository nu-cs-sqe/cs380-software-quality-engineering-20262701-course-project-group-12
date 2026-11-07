# BVA Analysis for BoardController

`BoardController` is the controller for `BoardView`. Its job is to answer with what `Board` reports about a square.

- `BoardController(Board board)`: takes the starting board.
- `boolean isOccupied(Location location)`: returns `board.isOccupied(location)`.
- `Piece pieceAt(Location location)`: returns `board.pieceAt(location)`. It does not choose a different piece. `Board` throws `IllegalArgumentException` for an empty square, and the controller lets that exception through. No method returns null.
- `void clickSquare(Location square)`, captures (issue #33 / GitHub US-08, docs US-10): builds on the two-click move from issue #34. When the selected piece `canMove` to a square that holds an opponent's piece, the board moves the selected piece there, and `Board.move` removes the captured piece. The king is never captured: when the clicked square holds the king, the board is unchanged and the selection stays on the moving piece, as for any refused move. Whether a capture is legal for the moving piece, and the refusal of a piece of its own color, are decided by each piece's `canMove`. The fifty-move count is a later story.

No method checks its arguments for null, and no case passes null: our code never passes null (instructor code review, Week 6). TC2, TC5, and TC8 used to pass a null board or location; they were removed with the board check.

The starting position itself is specified on `Board`. These cases check the pass-through. Each test mocks `Board` with EasyMock, because the controller does not own the board's behavior.

## Steps 1-3: Characteristics and partitions

### `BoardController(Board board)`

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `board` | Pointer | a board (no null: callers never pass one) |
| Output: the controller | Cases | the controller is created |

### `pieceAt(Location location)`

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: what the board does | Cases | returns a piece; throws `IllegalArgumentException` for an empty square |
| Input: `location` | Pointer | a location (no null: callers never pass one) |
| Output: the returned occupant | Pointer | the same piece instance the board returned |
| Output: signal when the board rejects the location | Cases | the same `IllegalArgumentException` the board throws |

### `isOccupied(Location location)`

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: what the board answers | Boolean | true; false |
| Input: `location` | Pointer | a location (no null: callers never pass one) |
| Output: the answer | Boolean | the same answer the board gave |

### `clickSquare(Location square)`, captures

The capture cases start from the state issue #34 leaves: b1 `(1, 1)` is selected, and the selected piece can move to c3 `(2, 3)`. `Board` and the selected `Piece` are mocked with EasyMock, because the controller owns neither the board nor the move rules. The piece on c3 is a real `Piece`, because the controller only reads its type.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the occupant of a square the selected piece can reach | Cases | an opponent's piece other than the king (a black pawn on c3); the opponent's king (the black king on c3) |
| Input: `square` | Pointer | a location (no null: callers never pass one) |
| Output: the board | Cases | the selected piece moves from b1 to c3 and replaces the captured piece; unchanged |
| Output: the selection after the click | Cases | none; unchanged (still b1) |

## Step 4: Test cases (each-choice strategy)

### Method under test: `BoardController(Board board)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC1: a board is provided | `board` is a `Board` | the controller is created | implemented in TC3 |

### Method under test: `pieceAt(Location location)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC3: the square has a piece | a controller for the mocked board; the board returns a white rook for the location | returns that same white rook | :white_check_mark: |
| TC4: the square is empty | a controller for the mocked board; the board throws `IllegalArgumentException` for the empty location | throws that same `IllegalArgumentException` | :white_check_mark: |

### Method under test: `isOccupied(Location location)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC6: the square has a piece | a controller for the mocked board; the board answers true for a1 `(0, 1)` | returns true | :white_check_mark: |
| TC7: the square is empty | a controller for the mocked board; the board answers false for a3 `(0, 3)` | returns false | :white_check_mark: |

### Method under test: `clickSquare(Location square)`, captures

TC9 through TC18 belong to issue #34. These cases continue its numbering.

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC19: capture an opponent's piece the selected piece can reach | a controller for the mocked board; the board answers true for b1 `(1, 1)` and returns a mocked piece that can move from b1 to c3 `(2, 3)`, and c3 holds a black pawn; click b1, then c3 | the board moves the piece from b1 to c3 | :x: |
| TC20: refuse to capture the king | a controller for the mocked board; the board answers true for b1 and returns a mocked piece that can move from b1 to c3, and c3 holds the black king; click b1, then c3 | the board does not move any piece | :x: |
| TC21: a refused king capture keeps the selection | as TC20; click b1, then c3, then ask `isSelected` about b1 | true | :x: |
