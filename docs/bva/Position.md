# BVA Analysis for Position

`Position` holds what two positions must share to count as the same position
for the repetition rules (issue #59 / GitHub US-19, docs US-18).
`Board.position(PieceColor colorToMove)` builds it from the board and the
player to move. It keeps its own copy of what it compares, so later moves on
the board do not change it.

- `boolean isSameAs(Position other)`: returns true when both positions have
  the same player to move, the same type and color of piece on every square,
  and the same en passant capture available. Pieces are compared by type and
  color, not by object, so two knights that swap squares leave the position
  the same. An en passant capture is available when the pawn that just moved
  two ranks has a pawn of the player to move beside it, on the same rank and
  an adjacent file.

Out of scope: castling rights (acceptance criteria 1 and 2). Castling is
issue #45, which is not merged yet. Castling rights join the comparison once
it is, using its `Board.holdsUnmovedPiece(square)`.

No method checks its argument for null, and no case passes null: our code
never passes null (instructor code review, Week 6).

Squares use file `0` = a-file through file `7` = h-file, and rank `1`
through `8`. Each position is built with `Board.move` from a new `Board`, then
`position(colorToMove)`.

## Steps 1-3: Characteristics and partitions

### `isSameAs(Position other)`

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the player to move | Cases | the same; different (White and Black) |
| Input: the pieces on the squares | Cases | the same type and color on every square; the same, with the two white knights swapped; one piece on another square (the g1 knight on f3 `(5, 3)`); a square holding a piece of another type (a queen on a2 `(0, 2)`); squares holding a piece of the other color (e4 `(4, 4)` and e5 `(4, 5)`) |
| Input: the en passant capture | Cases | none in either; available in one only; a pawn moved two ranks with no pawn beside it; a pawn moved two ranks with a pawn of the other player beside it |
| Input: `other` | Pointer | a position (no null: callers never pass one) |
| Output: the answer | Boolean | true; false |

## Step 4: Test cases (each-choice strategy)

### Method under test: `isSameAs(Position other)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC1: the same pieces and player to move | two new `Board`s; White to move in both | true | :white_check_mark: |
| TC2: another player to move | two new `Board`s; White to move in one, Black in the other | false | :white_check_mark: |
| TC3: a piece on another square | a new `Board`, and a new `Board` after `move` of g1 `(6, 1)` to f3 `(5, 3)`; White to move in both | false | :white_check_mark: |
| TC4: two knights of the same color swap squares | a new `Board`, and a new `Board` after `move` of b1 `(1, 1)` to a3 `(0, 3)`, g1 to b1, and a3 to g1; White to move in both | true | :white_check_mark: |
| TC5: a square holds a piece of another type | a new `Board`, and a new `Board` after `promote` of a2 `(0, 2)` to `QUEEN`; White to move in both | false | :white_check_mark: |
| TC6: squares hold pieces of the other color | a new `Board` after `move` of e2 `(4, 2)` to e4 `(4, 4)` and e7 `(4, 7)` to e5 `(4, 5)`, and a new `Board` after `move` of e7 to e4 and e2 to e5; White to move in both | false | :x: |
| TC7: an en passant capture available in one only | a new `Board` after `move` of d7 `(3, 7)` to d4 `(3, 4)` and e2 to e4, and a new `Board` after `move` of d7 to d4, e2 to e3 `(4, 3)`, and e3 to e4; Black to move in both | false | :x: |
| TC8: a two-rank pawn move with no pawn beside it | a new `Board` after `move` of e2 to e4, and a new `Board` after `move` of e2 to e3 and e3 to e4; Black to move in both | true | :x: |
| TC9: the pawn beside belongs to the other player | a new `Board` after `move` of f2 `(5, 2)` to f4 `(5, 4)` and e2 to e4, and a new `Board` after `move` of f2 to f4, e2 to e3, and e3 to e4; Black to move in both | true | :x: |
