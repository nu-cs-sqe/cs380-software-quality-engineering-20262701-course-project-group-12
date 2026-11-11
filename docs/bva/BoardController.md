# BVA Analysis for BoardController

`BoardController` is the controller for `BoardView`. Its job is to answer with what `Board` reports about a square, and to turn the player's clicks into moves.

- `BoardController(Board board, Game game)`: takes the starting board and the
  started game whose turn it consults. Issue #41 adds `game`. Callers never
  pass null for either argument.
- `boolean isOccupied(Location location)`: returns `board.isOccupied(location)`.
- `Piece pieceAt(Location location)`: returns `board.pieceAt(location)`. It does not choose a different piece. `Board` throws `IllegalArgumentException` for an empty square, and the controller lets that exception through. No method returns null.
- `void clickSquare(Location square)` (issues #34 and #41): handles a click on a square. With nothing selected, a click on a piece of the player to move selects it; a click on an empty square or on an opponent's piece does nothing. With a square selected, a click on the selected square itself clears the selection (US-09 AC 4). A click on any other square is the destination: when the selected piece `canMove` there, the board moves it, the game passes the turn, and the selection is cleared. When it cannot, a click on another piece of the player to move selects that piece instead; a click on an empty square or on an opponent's piece keeps the current selection. It does not tell the player about a refused move (US-10).
- `boolean isSelected(Location square)` (issue #34): returns true when `square` is the selected square, compared by file and rank. `BoardView` uses it to color the selected square.
- `void clickSquare(Location square)`, captures (issue #33 / GitHub US-08, docs US-10): builds on the two-click move from issue #34. When the selected piece `canMove` to a square that holds an opponent's piece, the board moves the selected piece there, and `Board.move` removes the captured piece. The king is never captured: when the clicked square holds the king, the board is unchanged and the selection stays on the moving piece, as for any refused move. Whether a capture is legal for the moving piece, and the refusal of a piece of its own color, are decided by each piece's `canMove`. The fifty-move count is a later story.
- `void clickSquare(Location square)`, promotions (issue #46 / GitHub US-13): when the selected piece `promotesOn` the square it moves to, the move is unfinished. The board moves the pawn there and the selection is cleared, but the game does not pass the turn: a promotion is pending until the player chooses. While a promotion is pending, a click on the board does nothing. A capture onto the last rank is promoted the same way.
- `boolean isPromotionPending()` (issue #46): returns true while a pawn waits on the last rank for the player's choice. `BoardView` uses it to ask for the choice.
- `void choosePromotion(PieceType type)` (issue #46): when a promotion is pending, the board promotes the pawn on the arrival square to `type`, the game passes the turn, and nothing is pending any more. When the board rejects the type (a king or a pawn), its `IllegalArgumentException` goes through, the promotion stays pending, and the turn does not pass.
- `void clickSquare(Location square)`, checkmate (issue #48 / GitHub US-15, docs US-15): after a move passes the turn, the controller asks `board.isCheckmated(game.colorToMove())`. When the player now to move is checkmated, it calls `game.endByCheckmate()`, so the player who made the move wins. Once `game.isOver()` is true, every click does nothing: no square is selected and no piece moves (US-15 AC 3).
- `boolean canClaimDraw()` (issue #59 / GitHub US-19, docs US-18): returns true when the player to move can claim a draw by repetition now (`game.canClaimDrawByRepetition()`), or while a move that would make a position occur the third time is waiting for the claim. `BoardView` offers Claim Draw only then, so the claim is offered only when it is correct (US-19 AC 5).
- `void claimDraw()` (issue #59): when `canClaimDraw()` is true, the game ends by repetition (`game.endByRepetition()`), and a waiting move is not made. Otherwise nothing happens.
- `void clickSquare(Location square)`, repetition (issue #59): when the selected piece `canMove` to the clicked square and `game.canClaimDrawByRepetitionWith(from, to)` is true, the board does not move yet. The move waits for the player to claim the draw or decline it (US-19 AC 4), and clicks do nothing while it waits. After a move passes the turn and is not checkmate, `game.isFivefoldRepetition()` ends the game by repetition with no claim (US-19 AC 6). Checkmate is checked first, so it still wins (US-19 AC 7).
- `boolean isDrawClaimWithMovePending()` (issue #59): returns true while a move waits for the claim. `BoardView` uses it to ask the player whether to claim the draw.
- `void declineDrawClaim()` (issue #59): makes the waiting move as usual: the board moves the piece, the game passes the turn, and the checks for checkmate and fivefold repetition run.

No method checks its arguments for null, and no case passes null: our code never passes null (instructor code review, Week 6). TC2, TC5, and TC8 used to pass a null board or location; they were removed with the board check.

Refreshing the current-player line on `GameStatsView` is a view change after
the cases, the way `BoardView` coloring was for #34. For promotions, an en
passant capture onto the last rank (US-14), giving check with the promoted
piece (US-11), and the fifty-move count (US-18) are later stories. Asking the
player for the choice is a `BoardView` change after the cases.

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

### `clickSquare(Location square)`, captures

The capture cases start from the state issue #34 leaves: b1 `(1, 1)` is selected, and the selected piece can move to c3 `(2, 3)`. `Board` and the selected `Piece` are mocked with EasyMock, because the controller owns neither the board nor the move rules. The piece on c3 is a real `Piece`, because the controller only reads its type.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the occupant of a square the selected piece can reach | Cases | an opponent's piece other than the king (a black pawn on c3); the opponent's king (the black king on c3) |
| Input: `square` | Pointer | a location (no null: callers never pass one) |
| Output: the board | Cases | the selected piece moves from b1 to c3 and replaces the captured piece; unchanged |
| Output: the selection after the click | Cases | none; unchanged (still b1) |

### `clickSquare(Location square)`, promotions, `isPromotionPending()`, and `choosePromotion(PieceType type)`

White is to move, and the selected piece is a mocked White piece on a7 `(0, 7)`. The mocked piece answers `promotesOn`, because the pawn's last rank is specified on `Pawn`. `Board`, `Game`, and the piece are strict mocks, so an unexpected `move`, `promote`, `passTurn`, or board question fails the test.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: what the moving piece answers for `promotesOn` the arrival square | Boolean | true (a7 to a8 `(0, 8)`); false (b1 `(1, 1)` to c3 `(2, 3)`) |
| Input: occupant of the arrival square | Cases | empty (a8); an opponent's piece (a black knight on b8 `(1, 8)`) |
| Input: whether a promotion is pending | Boolean | nothing pending; pending on the arrival square |
| Input: `type` passed to `choosePromotion` | Cases | a choice (`QUEEN`); not a choice (`KING`, which the board rejects) |
| Input: a click while a promotion is pending | Cases | a click on a White piece (b1) |
| Output: the board | Cases | the pawn is promoted on the arrival square; nothing is promoted |
| Output: the game | Cases | `passTurn` after the choice; unchanged while the promotion is pending |
| Output: `isPromotionPending()` | Boolean | true; false |
| Output: signal for a rejected choice | Cases | none; the board's `IllegalArgumentException` |

### `clickSquare(Location square)`, checkmate

The checkmate cases start from TC22: White moves the piece on b1 `(1, 1)` to c3 `(2, 3)`, and the game passes the turn, so Black is to move. `Board` and `Game` are mocked with EasyMock, because the controller owns neither checkmate detection nor the result. In every earlier case the game is not over, and after a move the board answers that the player to move is not checkmated.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: what the board answers for `isCheckmated` of the player to move after the move | Boolean | true; false |
| Input: `game.isOver()` when the click arrives | Boolean | false; true |
| Input: `square` | Pointer | a location (no null: callers never pass one) |
| Output: the game | Cases | `passTurn`, then `endByCheckmate`; `passTurn` only; unchanged |
| Output: the selection after a click on a piece of the player to move | Cases | the clicked square; none, when the game is over |

### `canClaimDraw()`, `claimDraw()`, `clickSquare(Location square)`, repetition, `isDrawClaimWithMovePending()`, and `declineDrawClaim()`

The move cases start from TC22: White moves the piece on b1 `(1, 1)` to c3 `(2, 3)`, and the game passes the turn, so Black is to move. `Board` and `Game` are mocked with EasyMock, because the controller owns neither the position history nor the result. In every earlier case the game answers false for `canClaimDrawByRepetitionWith` and for `isFivefoldRepetition`, so those cases still check what they did. Asking the player whether to claim with a waiting move, and the Claim Draw button, are `BoardView` changes after the cases.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: what the game answers for `canClaimDrawByRepetition` | Boolean | true; false |
| Input: what the game answers for `canClaimDrawByRepetitionWith(b1, c3)` | Boolean | true; false |
| Input: whether a move waits for the claim | Boolean | nothing waits; the b1 to c3 move waits |
| Input: what the game answers for `isFivefoldRepetition` after the move | Boolean | true; false |
| Input: what the board answers for `isCheckmated` after the move | Boolean | false; true, when the move is also a fivefold repetition |
| Input: a click while a move waits | Cases | a click on a White piece (b1) |
| Output: `canClaimDraw()` and `isDrawClaimWithMovePending()` | Boolean | true; false |
| Output: the board | Cases | the piece moves from b1 to c3; unchanged |
| Output: the game | Cases | `endByRepetition`; `passTurn`, then `endByRepetition`; `passTurn`, then `endByCheckmate`; `passTurn` only; unchanged |

## Step 4: Test cases (each-choice strategy)

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
| TC22: a legal move passes the turn | as TC9; White is to move; the piece on b1 is White | the board moves the piece from b1 to c3; the game passes the turn | :white_check_mark: |
| TC23: a refused move onto an empty square does not pass the turn | as TC10; White is to move; the piece on b1 is White | the board does not move any piece; the game does not pass the turn | :white_check_mark: |
| TC24: a click on an opponent's piece with nothing selected selects nothing | a controller for the mocked board and game; White is to move; the board answers true for b8 `(1, 8)` and returns a Black piece; click b8, then ask `isSelected` about b8 | false; the game does not pass the turn | :white_check_mark: |
| TC25: after the turn passes, a click on the previous player's piece selects nothing | as TC22; after the move, Black is to move; the board answers true for a2 `(0, 2)` and returns a White piece; click a2, then ask `isSelected` about a2 | false | :white_check_mark: |
| TC26: after the turn passes, a click on the new player's piece selects it | as TC22; after the move, Black is to move; the board answers true for b8 and returns a Black piece; click b8, then ask `isSelected` about b8 | true | :white_check_mark: |
| TC27: a click on an opponent's piece the selected one cannot reach keeps the selection | a controller for the mocked board and game; White is to move; the board answers true for b1 and for b8 and returns a White piece on b1 and a Black piece on b8; the selected piece cannot move from b1 to b8; click b1, then b8, then ask `isSelected` about b1 | true; `isSelected(b8)` is false; the board does not move any piece; the game does not pass the turn | :white_check_mark: |

TC9 through TC16 still hold. Their pieces are the player to move's color (White), which the new cases make explicit. TC17 used to select any occupied square the selected piece could not reach; US-09 AC 3 and AC 5 limit that to another piece of the player to move. TC13 used to expect a refused move to clear the selection. The player asked for the selected square to stay highlighted until a move or another piece is chosen, which is also US-10 AC 5, so TC13 now expects the selection to stay.

### Method under test: `isSelected(Location square)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC14: nothing selected | a controller for the mocked board, no clicks yet; ask about b1 `(1, 1)` | false | :white_check_mark: |
| TC15: the selected square | a controller for the mocked board; the board answers true for b1; click b1, then ask about a second b1 object | true | :white_check_mark: |
| TC16: another square while one is selected | a controller for the mocked board; the board answers true for b1; click b1, then ask about c3 `(2, 3)` | false | :white_check_mark: |

### Method under test: `clickSquare(Location square)`, captures

TC9 through TC18 belong to issue #34. These cases continue its numbering, and the turn cases from issue #41 follow as TC22 through TC27. White is to move and the piece on b1 is White.

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC19: capture an opponent's piece the selected piece can reach | a controller for the mocked board; the board answers true for b1 `(1, 1)` and returns a mocked piece that can move from b1 to c3 `(2, 3)`, and c3 holds a black pawn; click b1, then c3 | the board moves the piece from b1 to c3; the game passes the turn | :white_check_mark: |
| TC20: refuse to capture the king | a controller for the mocked board; the board answers true for b1 and returns a mocked piece that can move from b1 to c3, and c3 holds the black king; click b1, then c3 | the board does not move any piece; the game does not pass the turn | :white_check_mark: |
| TC21: a refused king capture keeps the selection | as TC20; click b1, then c3, then ask `isSelected` about b1 | true | :white_check_mark: |

### Method under test: `clickSquare(Location square)`, promotions, `isPromotionPending()`, and `choosePromotion(PieceType type)`

White is to move. The board answers true for a7 `(0, 7)` and returns a mocked White piece.

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC28: a move onto the last rank leaves a promotion pending | the piece can move from a7 to a8 `(0, 8)` and `promotesOn` a8; a8 is empty; click a7, then a8, then ask `isPromotionPending` | true; the board moves the piece from a7 to a8 | :white_check_mark: |
| TC29: a pending promotion does not pass the turn | as TC28 | the game does not pass the turn | :white_check_mark: |
| TC30: a capture onto the last rank leaves a promotion pending | the piece can move from a7 to b8 `(1, 8)` and `promotesOn` b8; b8 holds a black knight; click a7, then b8, then ask `isPromotionPending` | true; the board moves the piece from a7 to b8 | :white_check_mark: |
| TC31: a move that is not a promotion leaves nothing pending | as TC22, and the piece does not `promotesOn` c3 `(2, 3)`; ask `isPromotionPending` | false | :white_check_mark: |
| TC32: choosing a queen promotes the pawn on the arrival square | as TC28; then choose `QUEEN` | the board promotes a8 to `QUEEN` | :white_check_mark: |
| TC33: choosing passes the turn | as TC32 | the game passes the turn once | :white_check_mark: |
| TC34: nothing is pending after the choice | as TC32; ask `isPromotionPending` | false | :white_check_mark: |
| TC35: a click while a promotion is pending does nothing | as TC28; then click b1 `(1, 1)`, then ask `isSelected` about b1 | false; the board is not asked about b1 | :white_check_mark: |
| TC36: a king is not a choice | as TC28; the board throws `IllegalArgumentException` when asked to promote a8 to `KING`; choose `KING` | throws that `IllegalArgumentException` | :white_check_mark: |
| TC37: a rejected choice keeps the promotion pending | as TC36; ask `isPromotionPending` | true; the game does not pass the turn | :white_check_mark: |

After a move, the controller now asks the moving piece whether it `promotesOn` the arrival square. The mocked pieces in TC9, TC12, TC19, TC22, TC25, and TC26 answer false, so those cases still check what they did before.

### Method under test: `clickSquare(Location square)`, checkmate

The case numbers start at TC38, after the promotion cases (TC28 to TC37, issue #46).

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC38: a move that checkmates ends the game | as TC22; after the move, Black is to move, and the board answers true for `isCheckmated(BLACK)` | the board moves the piece from b1 to c3; the game passes the turn, then ends by checkmate | :white_check_mark: |
| TC39: a move that does not checkmate does not end the game | as TC22; after the move, Black is to move, and the board answers false for `isCheckmated(BLACK)` | the board moves the piece from b1 to c3; the game passes the turn and does not end | :white_check_mark: |
| TC40: a click after the game is over selects nothing | a controller for the mocked board and game; the game is over; White is to move, and b1 holds a White piece; click b1, then ask `isSelected` about b1 | false; the board is not asked about b1 | :white_check_mark: |

### Method under test: `canClaimDraw()`, `claimDraw()`, `clickSquare(Location square)`, repetition, `isDrawClaimWithMovePending()`, and `declineDrawClaim()`

The case numbers start at TC41, after the checkmate cases (TC38 to TC40, issue #48).

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC41: the player to move can claim a repetition | a controller for the mocked board and game; the game answers true for `canClaimDrawByRepetition`; ask `canClaimDraw` | true | :white_check_mark: |
| TC42: the player to move cannot claim, and no move waits | as TC41, but the game answers false; ask `canClaimDraw` | false | :white_check_mark: |
| TC43: a correct claim ends the game by repetition | as TC41; `claimDraw` | the game ends by repetition | :white_check_mark: |
| TC44: an incorrect claim does nothing | as TC42; `claimDraw` | the game does not end | :white_check_mark: |
| TC45: a move that makes a fivefold repetition ends the game | as TC22; after the move, Black is to move, the board answers false for `isCheckmated(BLACK)`, and the game answers true for `isFivefoldRepetition` | the board moves the piece from b1 to c3; the game passes the turn, then ends by repetition | :white_check_mark: |
| TC46: a move that does not make a fivefold repetition does not end the game | as TC45, but the game answers false for `isFivefoldRepetition` | the board moves the piece from b1 to c3; the game passes the turn and does not end | :white_check_mark: |
| TC47: checkmate wins over a fivefold repetition | as TC45, but the board answers true for `isCheckmated(BLACK)` | the game passes the turn, then ends by checkmate, not by repetition | :white_check_mark: |
| TC48: a move that would make the third occurrence waits for the claim | as TC22, but the game answers true for `canClaimDrawByRepetitionWith(b1, c3)`; click b1, then c3; ask `isDrawClaimWithMovePending` | true; the board does not move any piece; the game does not pass the turn | :white_check_mark: |
| TC49: a waiting move can be claimed | as TC48, and the game answers false for `canClaimDrawByRepetition`; ask `canClaimDraw` | true | :white_check_mark: |
| TC50: claiming with a waiting move ends the game by repetition | as TC48; `claimDraw` | the game ends by repetition; the board does not move any piece | :x: |
| TC51: declining makes the waiting move | as TC48; `declineDrawClaim`, then ask `isDrawClaimWithMovePending` | false; the board moves the piece from b1 to c3; the game passes the turn | :x: |
| TC52: a click while a move waits does nothing | as TC48; click b1, then ask `isSelected` about b1 | false; the board is not asked about b1 | :x: |
