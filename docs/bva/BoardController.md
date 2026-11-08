# BVA Analysis for BoardController

`BoardController` is the controller for `BoardView`. Its job is to answer with what `Board` reports about a square, and to turn the player's clicks into moves.

- `BoardController(Board board, Game game)`: takes the starting board and the
  started game whose turn it consults. Issue #41 adds `game`. Callers never
  pass null for either argument.
- `boolean isOccupied(Location location)`: returns `board.isOccupied(location)`.
- `Piece pieceAt(Location location)`: returns `board.pieceAt(location)`. It does not choose a different piece. `Board` throws `IllegalArgumentException` for an empty square, and the controller lets that exception through. No method returns null.
- `void clickSquare(Location square)` (issues #34 and #41): handles a click on a square. With nothing selected, a click on a piece of the player to move selects it; a click on an empty square or on an opponent's piece does nothing. With a square selected, a click on the selected square itself clears the selection (US-09 AC 4). A click on any other square is the destination: when the selected piece `canMove` there, the board moves it, the game passes the turn, and the selection is cleared. When it cannot, a click on another piece of the player to move selects that piece instead; a click on an empty square or on an opponent's piece keeps the current selection. It does not tell the player about a refused move (US-10).
- `boolean isSelected(Location square)` (issue #34): returns true when `square` is the selected square, compared by file and rank. `BoardView` uses it to color the selected square.

No method checks its arguments for null, and no case passes null: our code never passes null (instructor code review, Week 6). TC2, TC5, and TC8 used to pass a null board or location; they were removed with the board check.

Out of scope: blocking clicks after the game has ended (US-09 AC 8). Nothing
on `Game` ends the game yet; resign, checkmate, and the draw stories will.
Refreshing the current-player line on `GameStatsView` is a view change after
the cases, the way `BoardView` coloring was for #34.

The starting position itself is specified on `Board`. These cases check the pass-through. Each test mocks `Board` and `Game` with EasyMock, because the controller does not own the board's behavior or the turn.

## Steps 1-3: Characteristics and partitions

### `BoardController(Board board, Game game)`

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `board` | Pointer | a board (no null: callers never pass one) |
| Input: `game` | Pointer | a started game (no null: callers never pass one) |
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

The selection is private, so it is observed through the next click: a selected square makes the next click ask the piece about a move, and no selection makes the next click ask the board whether that square is occupied. `Board`, `Game`, and the selected `Piece` are mocked with EasyMock, because the controller owns neither the board, the turn, nor the move rules. The mocks are strict, so an unexpected `move`, `canMove`, or `passTurn` fails the test.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the selection before the click | Cases | none; a square |
| Input: occupancy of the clicked square, with nothing selected | Boolean | occupied (b1 `(1, 1)`); empty (a3 `(0, 3)`) |
| Input: what the selected piece answers for the clicked square | Boolean | can move (c3 `(2, 3)`); cannot move (b3 `(1, 3)`) |
| Input: `square` | Pointer | a location (no null: callers never pass one) |
| Output: the board | Cases | the piece moves from the selected square to the clicked square; unchanged |
| Input: the clicked square compared with the selection | Cases | the selected square (a second b1 object); another square |
| Input: occupancy of a clicked square the selected piece cannot reach | Boolean | occupied (d2 `(3, 2)`); empty (b3) |
| Output: the selection after the click | Cases | the clicked square; unchanged; none |
| Input: color of the player to move | Cases | White; Black |
| Input: color of the occupant of the clicked square | Cases | the player to move; the opponent |
| Output: the game | Cases | `passTurn`; unchanged |


### Method under test: `BoardController(Board board, Game game)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC1: a board and a game are provided | `board` is a `Board`; `game` is a started `Game` | the controller is created | implemented in TC3 |

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
| TC10: select a piece, then a square it cannot reach | a controller for the mocked board; the board answers true for b1 and returns a mocked piece that cannot move from b1 to b3 `(1, 3)`, and answers false for b3; click b1, then b3 | the board does not move any piece | :white_check_mark: |
| TC11: a click on an empty square selects nothing | a controller for the mocked board; the board answers false for a3 `(0, 3)` and for c3; click a3, then c3 | no piece is asked about a move; the board does not move any piece | :white_check_mark: |
| TC12: a move clears the selection | as TC9, and the board answers false for a3; click b1, then c3, then a3 | the board moves the piece from b1 to c3 once; the click on a3 asks no piece about a move | :white_check_mark: |
| TC13: a refused move onto an empty square keeps the selection | as TC10; click b1, then b3, then ask `isSelected` about b1 | true | :white_check_mark: |
| TC17: a click on another piece of the player to move selects that piece | a controller for the mocked board and game; White is to move; the board answers true for b1 and for d2 `(3, 2)` and returns White pieces; the selected piece cannot move from b1 to d2; click b1, then d2, then ask `isSelected` about d2 | true; the board does not move any piece; the game does not pass the turn | :white_check_mark: |
| TC18: a click on the selected square clears the selection | a controller for the mocked board and game; White is to move; the board answers true for b1 and returns a White piece; click b1, then click a second b1 object, then ask `isSelected` about b1 | false; no piece is asked about a move; the game does not pass the turn | :white_check_mark: |
| TC19: a legal move passes the turn | as TC9; White is to move; the piece on b1 is White | the board moves the piece from b1 to c3; the game passes the turn | :white_check_mark: |
| TC20: a refused move onto an empty square does not pass the turn | as TC10; White is to move; the piece on b1 is White | the board does not move any piece; the game does not pass the turn | :white_check_mark: |
| TC21: a click on an opponent's piece with nothing selected selects nothing | a controller for the mocked board and game; White is to move; the board answers true for b8 `(1, 8)` and returns a Black piece; click b8, then ask `isSelected` about b8 | false; the game does not pass the turn | :x: |
| TC22: after the turn passes, a click on the previous player's piece selects nothing | as TC19; after the move, Black is to move; the board answers true for a2 `(0, 2)` and returns a White piece; click a2, then ask `isSelected` about a2 | false | :x: |
| TC23: after the turn passes, a click on the new player's piece selects it | as TC19; after the move, Black is to move; the board answers true for b8 and returns a Black piece; click b8, then ask `isSelected` about b8 | true | :x: |
| TC24: a click on an opponent's piece the selected one cannot reach keeps the selection | a controller for the mocked board and game; White is to move; the board answers true for b1 and for b8 and returns a White piece on b1 and a Black piece on b8; the selected piece cannot move from b1 to b8; click b1, then b8, then ask `isSelected` about b1 | true; `isSelected(b8)` is false; the board does not move any piece; the game does not pass the turn | :x: |

TC9 through TC16 still hold. Their pieces are the player to move's color (White), which the new cases make explicit. TC17 used to select any occupied square the selected piece could not reach; US-09 AC 3 and AC 5 limit that to another piece of the player to move. TC13 used to expect a refused move to clear the selection. The player asked for the selected square to stay highlighted until a move or another piece is chosen, which is also US-10 AC 5, so TC13 now expects the selection to stay.

### Method under test: `isSelected(Location square)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC14: nothing selected | a controller for the mocked board, no clicks yet; ask about b1 `(1, 1)` | false | :white_check_mark: |
| TC15: the selected square | a controller for the mocked board; the board answers true for b1; click b1, then ask about a second b1 object | true | :white_check_mark: |
| TC16: another square while one is selected | a controller for the mocked board; the board answers true for b1; click b1, then ask about c3 `(2, 3)` | false | :white_check_mark: |
