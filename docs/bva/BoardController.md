# BVA Analysis for BoardController

`BoardController` is the controller for `BoardView`. Its job is to answer with what `Board` reports about a square, and to turn the player's clicks into moves.

- `BoardController(Board board, Game game)`: takes the starting board and the
  started game whose turn it consults. Issue #41 adds `game`. Callers never
  pass null for either argument.
- `boolean isOccupied(Location location)`: returns `board.isOccupied(location)`.
- `Piece pieceAt(Location location)`: returns `board.pieceAt(location)`. It does not choose a different piece. `Board` throws `IllegalArgumentException` for an empty square, and the controller lets that exception through. No method returns null.
- `void clickSquare(Location square)` (issues #34 and #41): handles a click on a square. With nothing selected, a click on a piece of the player to move selects it; a click on an empty square or on an opponent's piece does nothing. With a square selected, a click on the selected square itself clears the selection (US-09 AC 4). A click on any other square is the destination: when the selected piece `canMove` there, the board moves it, the game passes the turn, and the selection is cleared. When it cannot, a click on another piece of the player to move selects that piece instead; a click on an empty square or on an opponent's piece keeps the current selection. It does not tell the player about a refused move (US-10).
- `boolean isSelected(Location square)` (issue #34): returns true when `square` is the selected square, compared by file and rank. `BoardView` uses it to color the selected square.
- `void clickSquare(Location square)`, captures (issue #33 / GitHub US-08, docs US-10): builds on the two-click move from issue #34. When the selected piece `canMove` to a square that holds an opponent's piece, the board moves the selected piece there, and `Board.move` removes the captured piece. The king is never captured: a move that would capture it is one the opponent could only allow by leaving their king in check, which `Board.isLegalMove` refuses (issue #44). Whether a capture is legal for the moving piece, and the refusal of a piece of its own color, are decided by each piece's `canMove`. The fifty-move count is a later story.
- `void clickSquare(Location square)`, promotions (issue #46 / GitHub US-13): when the selected piece `promotesOn` the square it moves to, the move is unfinished. The board moves the pawn there and the selection is cleared, but the game does not pass the turn: a promotion is pending until the player chooses. While a promotion is pending, a click on the board does nothing. A capture onto the last rank is promoted the same way.
- `boolean isPromotionPending()` (issue #46): returns true while a pawn waits on the last rank for the player's choice. `BoardView` uses it to ask for the choice.
- `void choosePromotion(PieceType type)` (issue #46): when a promotion is pending, the board promotes the pawn on the arrival square to `type`, the game passes the turn, and nothing is pending any more. When the board rejects the type (a king or a pawn), its `IllegalArgumentException` goes through, the promotion stays pending, and the turn does not pass.
- `void clickSquare(Location square)`, keeping the king out of check (issue #44 / GitHub US-11): the controller asks `board.isLegalMove(from, to)` instead of asking the selected piece `canMove`. The board answers false for a move the piece cannot make and for a move that leaves the mover's own king in check, so both are refused the same way (US-10): the board is unchanged, the turn does not pass, and the selection follows the rules above. A pawn move onto the last rank is checked the same way before any promotion is pending. Because no legal move leaves a king in check, no move can capture a king (US-11 AC 5), so the controller no longer refuses a king capture itself.
- `void choosePromotion(PieceType type)`, checkmate (issue #44, US-11 AC 8): after the promotion passes the turn, the controller asks `board.isCheckmated(game.colorToMove())`, as after any other move, and calls `game.endByCheckmate()` when the player now to move is checkmated.
- `void clickSquare(Location square)`, checkmate (issue #48 / GitHub US-15, docs US-15): after a move passes the turn, the controller asks `board.isCheckmated(game.colorToMove())`. When the player now to move is checkmated, it calls `game.endByCheckmate()`, so the player who made the move wins. Once `game.isOver()` is true, every click does nothing: no square is selected and no piece moves (US-15 AC 3).
- `boolean canClaimDraw()` (issue #59 / GitHub US-19, docs US-18): returns true when the player to move can claim a draw by repetition now (`game.canClaimDrawByRepetition()`), or while a move that would make a position occur the third time is waiting for the claim. Issue #51 also returns true when `game.canClaimDrawByFiftyMove()` is true, or while a move that would bring the half-move count to 100 is waiting. `BoardView` offers Claim Draw only then, so the claim is offered only when it is correct (US-19 AC 5 and US-19 AC 6 in docs US-19).
- `void claimDraw()` (issue #59): when `canClaimDraw()` is true, the game ends by repetition (`game.endByRepetition()`) if that claim is the repetition, and a waiting move is not made. Issue #51: when the claim is the fifty-move rule, the game ends by `game.endByFiftyMove()` instead. Otherwise nothing happens.
- `void clickSquare(Location square)`, repetition (issue #59): when the board answers true for `isLegalMove` from the selected square to the clicked square and `game.canClaimDrawByRepetitionWith(from, to)` is true, the board does not move yet. The move waits for the player to claim the draw or decline it (US-19 AC 4), and clicks do nothing while it waits. After a move passes the turn and is not checkmate, `game.isFivefoldRepetition()` ends the game by repetition with no claim (US-19 AC 6). Checkmate is checked first, so it still wins (US-19 AC 7).
- `boolean isDrawClaimWithMovePending()` (issue #59): returns true while a move waits for the claim. `BoardView` uses it to ask the player whether to claim the draw.
- `void resign(PieceColor resigningColor)` (issue #60 / GitHub US-20, docs US-20): passes the resignation to `game.resign(resigningColor)`. `Game` decides who wins and ignores a resignation after the game is over, so the controller only forwards it. Once the game is over, `clickSquare` already refuses further moves (US-20 AC 5), and `canClaimDraw` returns false, so a resignation cannot be turned into a draw (US-20 AC 4).
- `void declineDrawClaim()` (issue #59): makes the waiting move as usual: the board moves the piece, the game passes the turn, and the checks for checkmate and fivefold repetition run. Issue #51 also checks `game.isSeventyFiveMoveRule()` after checkmate and before fivefold repetition, and calls `game.endBySeventyFiveMove()` when the count has reached 150.
- `void clickSquare(Location square)`, fifty-move (issue #51 / GitHub US-18, docs US-19): when the board answers true for `isLegalMove` and `game.canClaimDrawByFiftyMoveWith(from, to)` is true, and the move is not also a threefold claim, the board does not move yet. The move waits the same way as a threefold claim (US-19 AC 5). After a move passes the turn and is not checkmate, `game.isSeventyFiveMoveRule()` ends the game with no claim (US-19 AC 7). Checkmate is checked first, so it still wins (US-19 AC 8).
- `boolean canOfferDraw()` (issue #61 / GitHub US-21, docs US-21): returns `game.canOfferDraw()`. The view offers Offer Draw only then.
- `void offerDraw(PieceColor offeringColor)` (issue #61): passes the offer to `game.offerDraw(offeringColor)`.
- `boolean isDrawOffered()` (issue #61): returns `game.isDrawOffered()`. The view shows the offer to the opponent, with Accept and Decline, only then.
- `void acceptDrawOffer()` and `void declineDrawOffer()` (issue #61): pass the answer to `game.acceptDrawOffer()` and `game.declineDrawOffer()`. `Game` decides when an offer can be made, ends it after the opponent's move, and ends the game on an acceptance, so the controller only forwards. Once the game is drawn by agreement, `clickSquare` already refuses further moves through `game.isOver()` (US-21 AC 7).
- `boolean canClaimDraw()`, after the game is over (issue #61): TC58 (issue #60) already returns false once `game.isOver()` is true, so after a draw by agreement a position that occurred three times cannot be claimed, and the window shows a draw by agreement, not a draw by repetition (US-21 AC 7).
- `void clickSquare(Location square)`, the piece makes the move (issue #45 / US-12): when the board answers true for `isLegalMove` from the selected square to the clicked square, the controller calls the selected piece's `move(from, to, board)` instead of `board.move(from, to)`, so a castling king also moves its rook (`King.md`). A waiting move that the player declines to claim is made the same way. In every earlier case, "the board moves the piece" now means the selected piece's `move` is called with the controller's board.

No method checks its arguments for null, and no case passes null: our code never passes null (instructor code review, Week 6). TC2, TC5, and TC8 used to pass a null board or location; they were removed with the board check.

Refreshing the current-player line on `GameStatsView` is a view change after
the cases, the way `BoardView` coloring was for #34. For promotions, an en
passant capture onto the last rank (US-14) and the fifty-move count (US-18)
are later stories; checkmate with the promoted piece is issue #44. Asking the
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
| Input: the occupant of a square the selected piece can reach | Cases | an opponent's piece other than the king (a black pawn on c3); the opponent's king is never reachable (issue #44) |
| Input: `square` | Pointer | a location (no null: callers never pass one) |
| Output: the board | Cases | the selected piece moves from b1 to c3 and replaces the captured piece |
| Output: the selection after the click | Cases | none |

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

### `resign(PieceColor resigningColor)` and `canClaimDraw()` after the game is over

`Game` is mocked with EasyMock, because the controller does not own the result. The Resign buttons are a view change after the cases.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `resigningColor` | Cases | White (the same forwarding holds for Black) |
| Input: whether the game is over when a draw could be claimed | Boolean | false; true, after a resignation |
| Output: the game | Cases | `resign` with the same color |
| Output: `canClaimDraw()` | Boolean | true; false, once the game is over |

A resignation can end the game while the position has occurred three times. Claim Draw must then be refused, or the game would also end by repetition and the window would show a draw instead of the winner (US-20 AC 4). The case where the game is not over is TC45.

### `canOfferDraw()`, `offerDraw(PieceColor offeringColor)`, `isDrawOffered()`, `acceptDrawOffer()`, and `declineDrawOffer()`

`Game` is mocked with EasyMock, because the controller does not own the offer or the result. Each boolean answer is checked both ways, so a controller that always answered the same would fail. The Offer Draw, Accept, and Decline buttons are a view change after the cases. A draw claim after a draw by agreement is refused by TC58, which covers any game that is over.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: what the game answers for `canOfferDraw` | Boolean | false; true |
| Input: what the game answers for `isDrawOffered` | Boolean | false; true |
| Input: `offeringColor` | Cases | White (the same forwarding holds for Black) |
| Output: `canOfferDraw()` and `isDrawOffered()` | Boolean | the game's answer |
| Output: the game | Cases | `offerDraw` with the same color; `acceptDrawOffer`; `declineDrawOffer` |

### `clickSquare(Location square)`, the piece makes the move (issue #45)

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: what the board answers for `isLegalMove` from the selected square to the clicked square | Boolean | true (a castle, e1 `(4, 1)` to g1 `(6, 1)`) |
| Input: `square` | Pointer | a location (no null: callers never pass one) |
| Output: who makes the move | Cases | the selected piece's `move` with the controller's board; never `board.move` directly |

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
| TC20: removed (issue #44) | it expected the controller to refuse capturing the king itself. No legal move can reach a king, because `Board.isLegalMove` refuses every move that leaves a king in check (US-11 AC 5), so the controller no longer looks for one | — | removed |
| TC21: removed (issue #44) | it expected the selection to stay after that refusal; see TC20 | — | removed |

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

### Method under test: `clickSquare(Location square)` and `choosePromotion(PieceType type)`, keeping the king out of check

Issue #44 (GitHub US-11). The case numbers start at TC41, after the checkmate cases (TC38 to TC40, issue #48). `Board` and `Game` are mocked with EasyMock, because the controller owns neither the move rules nor the result.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: what the board answers for `isLegalMove` from the selected square to the clicked square | Boolean | true; false (for example, the move leaves the mover's king in check) |
| Input: whether the move reaches the last rank | Boolean | no (b1 `(1, 1)` to c3 `(2, 3)`); yes (a7 `(0, 7)` to a8 `(0, 8)`) |
| Input: what the board answers for `isCheckmated` of the player to move after a promotion | Boolean | true; false |
| Input: `square` | Pointer | a location (no null: callers never pass one) |
| Output: the board | Cases | the piece moves; unchanged |
| Output: the game | Cases | `passTurn`, then `endByCheckmate`; `passTurn` only; unchanged |
| Output: `isPromotionPending()` | Boolean | false, after a refused move onto the last rank |

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC41: a move the board refuses leaves the board unchanged | White is to move; b1 `(1, 1)` holds a White piece; the board answers false for `isLegalMove(b1, c3)` and false for `isOccupied(c3)`; click b1, then c3 `(2, 3)` | the board does not move any piece; the game does not pass the turn; the piece is not asked `canMove` | :white_check_mark: |
| TC42: a refused move onto the last rank leaves nothing pending | White is to move; a7 `(0, 7)` holds a White piece; the board answers false for `isLegalMove(a7, a8)` and false for `isOccupied(a8)`; click a7, then a8 `(0, 8)`, then ask `isPromotionPending` | false; the board does not move any piece | :white_check_mark: |
| TC43: a promotion that checkmates ends the game | as TC32; after the choice, Black is to move, and the board answers true for `isCheckmated(BLACK)` | the board promotes a8 to `QUEEN`; the game passes the turn, then ends by checkmate | :white_check_mark: |
| TC44: a promotion that does not checkmate does not end the game | as TC32; after the choice, Black is to move, and the board answers false for `isCheckmated(BLACK)` | the board promotes a8 to `QUEEN`; the game passes the turn and does not end | :white_check_mark: |

TC9 through TC40 asked the mocked piece `canMove`. From TC41 on, they ask the mocked board `isLegalMove` with the same answer and expect the same result. A legal move no longer asks the board what stands on the arrival square, because the controller no longer looks for a king there.

### Method under test: `canClaimDraw()`, `claimDraw()`, `clickSquare(Location square)`, repetition, `isDrawClaimWithMovePending()`, and `declineDrawClaim()`

The case numbers start at TC45, after the cases that keep the king out of check (TC41 to TC44, issue #44).

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC45: the player to move can claim a repetition | a controller for the mocked board and game; the game answers true for `canClaimDrawByRepetition`; ask `canClaimDraw` | true | :white_check_mark: |
| TC46: the player to move cannot claim, and no move waits | as TC45, but the game answers false; ask `canClaimDraw` | false | :white_check_mark: |
| TC47: a correct claim ends the game by repetition | as TC45; `claimDraw` | the game ends by repetition | :white_check_mark: |
| TC48: an incorrect claim does nothing | as TC46; `claimDraw` | the game does not end | :white_check_mark: |
| TC49: a move that makes a fivefold repetition ends the game | as TC22; after the move, Black is to move, the board answers false for `isCheckmated(BLACK)`, and the game answers true for `isFivefoldRepetition` | the board moves the piece from b1 to c3; the game passes the turn, then ends by repetition | :white_check_mark: |
| TC50: a move that does not make a fivefold repetition does not end the game | as TC49, but the game answers false for `isFivefoldRepetition` | the board moves the piece from b1 to c3; the game passes the turn and does not end | :white_check_mark: |
| TC51: checkmate wins over a fivefold repetition | as TC49, but the board answers true for `isCheckmated(BLACK)` | the game passes the turn, then ends by checkmate, not by repetition | :white_check_mark: |
| TC52: a move that would make the third occurrence waits for the claim | as TC22, but the game answers true for `canClaimDrawByRepetitionWith(b1, c3)`; click b1, then c3; ask `isDrawClaimWithMovePending` | true; the board does not move any piece; the game does not pass the turn | :white_check_mark: |
| TC53: a waiting move can be claimed | as TC52, and the game answers false for `canClaimDrawByRepetition`; ask `canClaimDraw` | true | :white_check_mark: |
| TC54: claiming with a waiting move ends the game by repetition | as TC52; `claimDraw` | the game ends by repetition; the board does not move any piece | :white_check_mark: |
| TC55: declining makes the waiting move | as TC52; `declineDrawClaim`, then ask `isDrawClaimWithMovePending` | false; the board moves the piece from b1 to c3; the game passes the turn | :white_check_mark: |
| TC56: a click while a move waits does nothing | as TC52; click b1, then ask `isSelected` about b1 | false; the board is not asked about b1 | :white_check_mark: |

### Method under test: `resign(PieceColor resigningColor)` and `canClaimDraw()` after the game is over

The case number continues after the repetition cases (TC45 to TC56, issue #59).

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC57: a resignation is passed to the game | a controller for the mocked board and game; `resign(WHITE)` | the game is asked to `resign(WHITE)` | :white_check_mark: |
| TC58: a draw cannot be claimed once the game is over | as TC45, but the game answers true for `isOver`; ask `canClaimDraw` | false | :white_check_mark: |

### Method under test: `canOfferDraw()`, `offerDraw(PieceColor offeringColor)`, `isDrawOffered()`, `acceptDrawOffer()`, and `declineDrawOffer()`

The case numbers continue after the resignation cases (TC57 and TC58, issue #60).

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC59: no draw can be offered | a controller for the mocked board and game; the game answers false for `canOfferDraw`; ask `canOfferDraw` | false | :white_check_mark: |
| TC60: a draw can be offered | as TC59, but the game answers true | true | :white_check_mark: |
| TC61: an offer is passed to the game | a controller for the mocked board and game; `offerDraw(WHITE)` | the game is asked to `offerDraw(WHITE)` | :white_check_mark: |
| TC62: no draw is offered | a controller for the mocked board and game; the game answers false for `isDrawOffered`; ask `isDrawOffered` | false | :white_check_mark: |
| TC63: a draw is offered | as TC62, but the game answers true | true | :white_check_mark: |
| TC64: an acceptance is passed to the game | a controller for the mocked board and game; `acceptDrawOffer` | the game is asked to `acceptDrawOffer` | :white_check_mark: |
| TC65: a decline is passed to the game | a controller for the mocked board and game; `declineDrawOffer` | the game is asked to `declineDrawOffer` | :white_check_mark: |

### Method under test: `clickSquare(Location square)`, the piece makes the move (issue #45)

The case number continues after the draw offer cases (TC59 to TC65, issue #61).

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC66: a castle is made by the selected king | a controller for the mocked board and game; White is to move; the board answers true for e1 `(4, 1)`, returns a mocked White king that does not promote on g1 `(6, 1)`, and answers true for `isLegalMove(e1, g1)`; click e1, then g1 | the king is asked once to `move` from e1 to g1 on the controller's board, so it can move its rook too; the board is not asked to `move` directly; the game passes the turn | :white_check_mark: |

### Method under test: `canClaimDraw()`, `claimDraw()`, `clickSquare(Location square)`, fifty-move rule

Issue #51 (GitHub US-18, docs US-19). The case numbers start at TC67, after
the castle case (TC66, issue #45). They skip the stalemate and dead-position
numbers on issues #49 and #50. `Board` and `Game` are mocked. Checkmate is
asked first, then the seventy-five-move rule, then fivefold repetition. A
click after the game is over is already TC40. A waiting move already uses
TC55 and TC56.

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC67: the player to move can claim a fifty-move draw | a controller for the mocked board and game; the game answers false for `isOver` and for `canClaimDrawByRepetition`, and true for `canClaimDrawByFiftyMove`; ask `canClaimDraw` | true | :white_check_mark: |
| TC68: the player to move cannot claim a fifty-move draw, and no move waits | as TC67, but the game answers false for `canClaimDrawByFiftyMove`; ask `canClaimDraw` | false | :white_check_mark: |
| TC69: a correct fifty-move claim ends the game as that draw | as TC67; `claimDraw` | the game ends by fifty-move; the game does not end by repetition | :white_check_mark: |
| TC70: a move that reaches 150 ends the game by the seventy-five-move rule | as TC22; after the move, Black is to move, the board answers false for `isCheckmated(BLACK)`, the game answers false for `isFivefoldRepetition` and true for `isSeventyFiveMoveRule` | the board moves the piece from b1 to c3; the game passes the turn, then ends by seventy-five-move | :white_check_mark: |
| TC71: a move that does not reach 150 does not end the game | as TC70, but the game answers false for `isSeventyFiveMoveRule` | the board moves the piece from b1 to c3; the game passes the turn and does not end | :white_check_mark: |
| TC72: checkmate wins over the seventy-five-move rule | as TC70, but the board answers true for `isCheckmated(BLACK)` | the game passes the turn, then ends by checkmate, not by seventy-five-move | :white_check_mark: |
| TC73: a move that would make the count 100 waits for the claim | as TC22, but the game answers false for `canClaimDrawByRepetitionWith(b1, c3)` and true for `canClaimDrawByFiftyMoveWith(b1, c3)`; click b1, then c3; ask `isDrawClaimWithMovePending` | true; the board does not move any piece; the game does not pass the turn | :white_check_mark: |
| TC74: a waiting fifty-move can be claimed | as TC73, and the game answers false for `canClaimDrawByRepetition` and for `canClaimDrawByFiftyMove`; ask `canClaimDraw` | true | :white_check_mark: |
| TC75: claiming with a waiting fifty-move ends that draw | as TC73; `claimDraw` | the game ends by fifty-move; the board does not move any piece | :white_check_mark: |
