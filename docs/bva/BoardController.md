# BVA Analysis for BoardController

`BoardController` is the controller for `BoardView`. Its job is to answer with what `Board` reports about a square, and to turn the player's clicks into moves.

- `BoardController(Board board)`: takes the starting board.
- `boolean isOccupied(Location location)`: returns `board.isOccupied(location)`.
- `Piece pieceAt(Location location)`: returns `board.pieceAt(location)`. It does not choose a different piece. `Board` throws `IllegalArgumentException` for an empty square, and the controller lets that exception through. No method returns null.
- `void clickSquare(Location square)` (issue #34): handles a click on a square. With nothing selected, a click on an occupied square selects it, and a click on an empty square does nothing. With a square selected, the click is the destination: when the selected piece `canMove` there, the board moves it. Either way the selection is then cleared. It does not check whose turn it is (US-09) and does not tell the player about a refused move (US-10).
- `boolean isSelected(Location square)` (issue #34): returns true when `square` is the selected square, compared by file and rank. `BoardView` uses it to color the selected square.

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

### `clickSquare(Location square)`

The selection is private, so it is observed through the next click: a selected square makes the next click ask the piece about a move, and no selection makes the next click ask the board whether that square is occupied. `Board` and the selected `Piece` are mocked with EasyMock, because the controller owns neither the board nor the move rules. The mocks are strict, so an unexpected `move` or `canMove` fails the test.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the selection before the click | Cases | none; a square |
| Input: occupancy of the clicked square, with nothing selected | Boolean | occupied (b1 `(1, 1)`); empty (a3 `(0, 3)`) |
| Input: what the selected piece answers for the clicked square | Boolean | can move (c3 `(2, 3)`); cannot move (b3 `(1, 3)`) |
| Input: `square` | Pointer | a location (no null: callers never pass one) |
| Output: the board | Cases | the piece moves from the selected square to the clicked square; unchanged |
| Output: the selection after the click | Cases | the clicked square; none |


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

### Method under test: `clickSquare(Location square)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC9: select a piece, then a square it can reach | a controller for the mocked board; the board answers true for b1 `(1, 1)` and returns a mocked piece that can move from b1 to c3 `(2, 3)`; click b1, then c3 | the board moves the piece from b1 to c3 | :white_check_mark: |
| TC10: select a piece, then a square it cannot reach | a controller for the mocked board; the board answers true for b1 and returns a mocked piece that cannot move from b1 to b3 `(1, 3)`; click b1, then b3 | the board does not move any piece | :white_check_mark: |
| TC11: a click on an empty square selects nothing | a controller for the mocked board; the board answers false for a3 `(0, 3)` and for c3; click a3, then c3 | no piece is asked about a move; the board does not move any piece | :white_check_mark: |
| TC12: a move clears the selection | as TC9, and the board answers false for a3; click b1, then c3, then a3 | the board moves the piece from b1 to c3 once; the click on a3 asks no piece about a move | :white_check_mark: |
| TC13: a refused move clears the selection | as TC10, and the board answers false for c3; click b1, then b3, then c3 | no piece is asked about c3; the board does not move any piece | :white_check_mark: |

### Method under test: `isSelected(Location square)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC14: nothing selected | a controller for the mocked board, no clicks yet; ask about b1 `(1, 1)` | false | :white_check_mark: |
| TC15: the selected square | a controller for the mocked board; the board answers true for b1; click b1, then ask about a second b1 object | true | :x: |
| TC16: another square while one is selected | a controller for the mocked board; the board answers true for b1; click b1, then ask about c3 `(2, 3)` | false | :x: |
